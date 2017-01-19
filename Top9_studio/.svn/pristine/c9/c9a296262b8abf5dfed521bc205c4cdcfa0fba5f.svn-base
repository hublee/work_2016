package com.zeustel.top9.utils.gif;

import android.content.Context;

import com.zeustel.top9.utils.Tools;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/17 14:38
 */
public class GifData {
    /**
     * 名称,地址
     */
    public static final HashMap<String, String> gifFace = new HashMap();
    public static final HashMap<String, String> gifFaceReverse = new HashMap();
    public static final ArrayList<String> gifFacePaths = new ArrayList();

    /**
     * 加载本地表情
     *
     * @param context
     */
    public static void loadNativeFace(Context context) {
        File cacheDirFace = Tools.getCacheDirFace(context);
        //        PATH_CONFIG_FACE
        File facedoc = new File(cacheDirFace, "doc.xml");
        if (facedoc == null || !facedoc.exists()) {
            return;
        }
        FileInputStream inputStream = null;
        String prefix = cacheDirFace.getAbsolutePath();
        try {
            inputStream = new FileInputStream(facedoc);
            XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
            //获取XmlPullParser的实例
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            String name = null, value = null;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT://文档开始事件,可以进行数据初始化处理
                        gifFace.clear();
                        gifFacePaths.clear();
                        gifFaceReverse.clear();
                        break;
                    case XmlPullParser.START_TAG://开始元素事件
                        name = parser.getName();
                        if (name.equals("ChatFace")) {
                            break;
                        }
                        if (name.equals("next")) {
                            try {
                                if (parser.getAttributeCount() > 0) {
                                    name = parser.getAttributeValue(0);
                                    Tools.log_i(GifData.class, "loadNativeFace", "name : " + name);
                                }
                                value = parser.nextText();


                                if (name != null && value != null && !gifFace.containsKey(name)) {
                                    value = prefix + File.separator + value;
                                    gifFace.put(name, value);
                                    gifFacePaths.add(value);
                                    gifFaceReverse.put(value, name);
                                }

                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                                value = null;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Tools.log_i(GifData.class, "loadNativeFace", "value : " + value);
                            if (parser.getEventType() != XmlPullParser.END_TAG) {
                                parser.nextTag();
                            }
                        }

                        break;
                    case XmlPullParser.END_TAG://结束元素事件
                        name = null;
                        value = null;
                        break;
                }
                eventType = parser.next();
            }
            Tools.log_i(GifData.class, "loadNativeFace", "done ..");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputStream = null;
            }
        }
    }
}
