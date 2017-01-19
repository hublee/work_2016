package com.zeustel.top9.utils.gif;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.zeustel.top9.utils.Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GifArms<T extends TextView> {
    /**
     * @author Dragon
     *         SpanInfo 类用于存储一个要显示的图片（动态或静态）的信息，包括分解后的每一帧mapList、替代文字的起始位置、终止位置
     *         、帧的总数、当前需要显示的帧、帧与帧之间的时间间隔
     */
    private class SpanInfo {
        ArrayList<Bitmap> mapList;
        int start, end, frameCount, currentFrameIndex, delay;

        public SpanInfo() {
            mapList = new ArrayList<Bitmap>();
            start = end = frameCount = currentFrameIndex = delay = 0;
        }
    }

    /**
     * spanInfoList 是一个SpanInfo的list ,用于处理一个TextView中出现多个要匹配的图片的情况
     */
    private ArrayList<SpanInfo> spanInfoList = null;
    private Handler handler;           //用于处理从子线程TextView传来的消息
    private T textView;
    private Context context;
    private Resources resources;
    private String myText;

    public GifArms(T textView) {
        this.textView = textView;
        this.context = this.textView.getContext();
        this.resources = this.context.getResources();
        spanInfoList = new ArrayList<SpanInfo>();
    }


    /**
     * 对要显示在textView上的文本进行解析，看看是否文本中有需要与Gif或者静态图片匹配的文本
     * 若有，那么调用parseGif 对该文本对应的Gif图片进行解析 或者嗲用parseBmp解析静态图片
     *
     * @param inputStr
     */
    private void parseText(String inputStr) {
        myText = inputStr;
        Tools.log_i(GifArms.class,"parseText","inputStr : "+inputStr);
        Pattern mPattern = Pattern.compile("#\\d+");
        Matcher mMatcher = mPattern.matcher(inputStr);
        while (mMatcher.find()) {
            String faceName = mMatcher.group();
            Tools.log_i(GifArms.class,"parseText","faceName : "+faceName);
            /**
             * 这里匹配时用到了图片库，即一个专门存放图片id和其匹配的名称的静态对象，这两个静态对象放在了FaceData.java
             * 中，并采用了静态块的方法进行了初始化，不会有空指针异常
             */
            if (GifData.gifFace.containsKey(faceName)) {
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(new File(GifData.gifFace.get(faceName)));
                    parseGif(fileInputStream, mMatcher.start(), mMatcher.end());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 对静态图片进行解析：
     * 创建一个SpanInfo对象，帧数设为1，按照下面的参数设置，最后不要忘记将SpanInfo对象添加进spanInfoList中，
     * 否则不会显示
     *
     * @param resourceId
     * @param start
     * @param end
     */
    private void parseBmp(int resourceId, int start, int end) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
        ImageSpan imageSpan = new ImageSpan(context, bitmap);
        SpanInfo spanInfo = new SpanInfo();
        spanInfo.currentFrameIndex = 0;
        spanInfo.frameCount = 1;
        spanInfo.start = start;
        spanInfo.end = end;
        spanInfo.delay = 100;
        spanInfo.mapList.add(bitmap);
        spanInfoList.add(spanInfo);

    }

    /**
     * 解析Gif图片，与静态图片唯一的不同是这里需要调用GifOpenHelper类读取Gif返回一系一组bitmap（用for 循环把这一
     * 组的bitmap存储在SpanInfo.mapList中，此时的frameCount参数也大于1了）
     *
     * @param start
     * @param end
     */
    private void parseGif(InputStream inputStream, int start, int end) {
        GifOpenHelper helper = new GifOpenHelper();
        helper.read(inputStream);
        SpanInfo spanInfo = new SpanInfo();
        spanInfo.currentFrameIndex = 0;
        spanInfo.frameCount = helper.getFrameCount();
        spanInfo.start = start;
        spanInfo.end = end;
        spanInfo.mapList.add(helper.getImage());
        for (int i = 1; i < helper.getFrameCount(); i++) {
            spanInfo.mapList.add(helper.nextBitmap());
        }
        spanInfo.delay = helper.nextDelay();        //获得每一帧之间的延迟
        spanInfoList.add(spanInfo);
    }

    /**
     * MyTextView 与外部对象的接口，以后设置文本内容时使用setSpanText() 而不再是setText();
     *
     * @param text
     */
    public void setSpanText(Handler handler,String text) {
        this.handler = handler;
        //获得UI的Handler
        parseText(text);           //对String对象进行解析
        if (r != null) {
            handler.removeCallbacks(r);
            r = null;
        }
        r = new TextRunnable();   //生成Runnable对象
        handler.post(r);       //利用UI线程的Handler 将r添加进消息队列中。
    }

    TextRunnable r;

    public class TextRunnable implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            SpannableString sb = new SpannableString("" + myText);   //获得要显示的文本
            int gifCount = 0;
            SpanInfo info = null;
            for (int i = 0; i < spanInfoList.size(); i++) {  //for循环，处理显示多个图片的问题
                info = spanInfoList.get(i);
                if (info.mapList.size() > 1) {
                    /*
                     * gifCount用来区分是Gif还是BMP，若是gif gifCount>0 ,否则gifCount=0
					 */
                    gifCount++;

                }
                Bitmap bitmap = info.mapList.get(info.currentFrameIndex);
                info.currentFrameIndex = (info.currentFrameIndex + 1) % (info.frameCount);
                /**
                 * currentFrameIndex 用于控制当前应该显示的帧的序号，每次显示之后currentFrameIndex
                 * 应该加1 ，加到frameCount后再变成0循环显示
                 */

                if (gifCount != 0) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, 60, 60, true);

                } else {
                    bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
                }
                ImageSpan imageSpan = new ImageSpan(context, bitmap);
                sb.setSpan(imageSpan, info.start, info.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //对所有的图片对应的ImageSpan完成设置后，调用TextView的setText方法设置文本
            textView.setText(sb);

            /**
             * 这一步是为了节省内存而是用，即如果文本中只有静态图片没有动态图片，那么该线程就此终止，不会重复执行
             * 。而如果有动图，那么会一直执行
             */
            if (gifCount != 0) {
                handler.postDelayed(this, info.delay);
            }
        }
    }
}
