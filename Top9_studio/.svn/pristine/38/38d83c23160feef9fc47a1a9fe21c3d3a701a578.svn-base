package com.zeustel.top9;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zeustel.top9.base.WrapNotAndHandleActivity;
import com.zeustel.top9.bean.GameImageButtons;
import com.zeustel.top9.bean.GameImages;
import com.zeustel.top9.bean.ScreenShotControl;
import com.zeustel.top9.result.GameImagesListResult;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.ScreenshotProvider;
import com.zeustel.top9.utils.SensorListener;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBGameImagesImp;
import com.zeustel.top9.utils.operate.DataGameImages;

import java.util.ArrayList;
import java.util.List;

public class ScreenshotExhibition extends WrapNotAndHandleActivity implements OnClickListener, SensorListener.OnSensorListener {
    public static final int DEF_TYPE_3_WIDTH = 40;
    public static final String EXTRA_NAME_URL = "requestUrl";
    public static final String EXTRA_NAME_ID = "id";
    private static final int MSG_DATA_PREPARED = 11;
    private static final int MSG_GROUP_UPDATE = 12;
    public int anim_Type = 3;// 1、箭头；2、方形；3、闪烁
    public int def_width_px = 0;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<GameImages> mGameImageList = new ArrayList<GameImages>();
    private List<GameImageButtons> mControlList = null;
    // 存储打开的截图
    private ArrayList<GameImages> wait_repeal = new ArrayList<GameImages>();
    // 方便根据gameId快速查找
    private ArrayList<Integer> ids = new ArrayList<Integer>();
    private GameImages mGameImages = null;
    private ImageView screenshot_img = null;
    private ImageView screenshot_img_spare = null;
    private RelativeLayout controlGroup = null;
    private RelativeLayout mScreenshotLayout = null;
    private ScreenShotInListener mScreenShotInListener = null;
    private ScreenShotOutListener mScreenShotOutListener = null;
    private double zoom;
    private ScreenshotProvider mScreenshotProvider = null;
    private int displayIndex = 0;
    private SensorListener mSensorListener;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private DataGameImages dataGameImages;
    private DBGameImagesImp dbGameImagesImp;
    private int[] displayMetrics;
    private int id = -1;
    private String url;
    private String requestUrl;
    private float tagDegrees, toDegrees;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DATA_PREPARED:
                    // 设置第一张图
                    if (mGameImageList != null && !mGameImageList.isEmpty()) {
                        wait_repeal.add(mGameImageList.get(displayIndex));
                        updateUI();
                        return;
                    }
                    finish();
                    break;
                case MSG_GROUP_UPDATE:
                    if (msg.obj != null) {
                        ScreenShotControl mScreenShotControl = (ScreenShotControl) msg.obj;
                        controlGroup.addView(mScreenShotControl.getmButton());
                        if (mScreenShotControl.getW() == mScreenShotControl.getH() || 3 == anim_Type) {
                            // 闪烁
                            mScreenshotProvider.startFlickerAnim((int) tagDegrees, mScreenShotControl.getmButton());
                        } else if (1 == anim_Type) {
                            // 箭头动画
                            mScreenshotProvider.startArrowsAnim(controlGroup, mScreenShotControl.getLeft(), mScreenShotControl.getTop(), mScreenShotControl.getW(), mScreenShotControl.getH());
                        } else if (2 == anim_Type) {
                            // 方形渐隐
                            mScreenshotProvider.startRectangleAnim(mScreenShotControl, 1000);
                        }
                        mScreenShotControl = null;
                    }
                    break;
                case SensorListener.MSG_WHAT:
                    // 获取数据成工
                    mSensorListener.handleSensorMessage(msg);
                    break;
            }
        }

        ;
    };

    @Override
    public void onHandleListUpdate(Result result, Object obj) {
        super.onHandleListUpdate(result, obj);
        if (obj != null) {
            ArrayList<GameImages> temp = (ArrayList<GameImages>) obj;
            if (!Tools.isEmpty(temp)) {
                mGameImageList.clear();
                mGameImageList.addAll(temp);
                prepare();
            }
        }
    }

    @Override
    public void onHandleListNo(Result result) {
        super.onHandleListNo(result);
        //无数据
        finish();
    }

    @Override
    public void onHandleListFailed(Result result) {
        super.onHandleListFailed(result);
        //获取失败
        finish();
    }

    // List--->HashMap
    private void prepare() {
        Tools.pool.submit(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mGameImageList != null && !mGameImageList.isEmpty()) {
                    for (GameImages list_item : mGameImageList) {
                        if (list_item != null) {
                            int imgId = list_item.getId();
                            if (!ids.contains(imgId)) {
                                ids.add(imgId);
                            }
                        }
                    }
                }
                handler.sendEmptyMessage(MSG_DATA_PREPARED);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        displayMetrics = Tools.getDisplayMetrics(getApplicationContext());
        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (ids != null) {
            ids.clear();
        }
        if (wait_repeal != null) {
            wait_repeal.clear();
            wait_repeal = null;
        }
        if (mGameImageList != null) {
            mGameImageList.clear();
            mGameImageList = null;
        }
        if (mControlList != null) {
            mControlList.clear();
        }
        if (screenshot_img != null) {
            screenshot_img.clearAnimation();
        }
        if (screenshot_img_spare != null) {
            screenshot_img.clearAnimation();
        }
        cleanControlItems();
        mScreenshotProvider.exit();
        if (mScreenshotLayout != null) {
            mScreenshotLayout.removeAllViews();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        System.gc();
    }

    private void cleanControlItems() {
        if (controlGroup != null) {
            controlGroup.removeAllViews();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(initView());
        def_width_px = Tools.dip2px(getApplicationContext(), DEF_TYPE_3_WIDTH);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            id = intent.getIntExtra(EXTRA_NAME_ID, -1);
            requestUrl = intent.getStringExtra(EXTRA_NAME_URL);
        }
        if (Tools.isEmpty(requestUrl)) {
            finish();
        }

        //数据加载 本地 网络
        mScreenshotProvider = new ScreenshotProvider(getApplicationContext());
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorListener = new SensorListener(handler, this);
        dbGameImagesImp = new DBGameImagesImp(getApplicationContext());
        dataGameImages = new DataGameImages(getHandler(), dbGameImagesImp);
        try {
            dataGameImages.getListData(dataGameImages.createListBundle(requestUrl, 0, NetHelper.Flag.UPDATE), GameImagesListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View initView() {
        View screenShotView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.screenshot_exhibition, null);
        screenshot_img = (ImageView) screenShotView.findViewById(R.id.screenshot_img);
        screenshot_img_spare = (ImageView) screenShotView.findViewById(R.id.screenshot_img_spare);
        controlGroup = (RelativeLayout) screenShotView.findViewById(R.id.controlGroup);
        mScreenshotLayout = (RelativeLayout) screenShotView.findViewById(R.id.mScreenshotLayout);
        screenshot_img.setOnClickListener(this);
        screenshot_img_spare.setOnClickListener(this);
        mScreenShotInListener = new ScreenShotInListener();
        mScreenShotOutListener = new ScreenShotOutListener();
        return screenShotView;
    }

    private void showPrevious() {
        if (!wait_repeal.isEmpty()) {
            wait_repeal.remove(wait_repeal.size() - 1);
            if (!wait_repeal.isEmpty()) {
                GameImages mGameImages = wait_repeal.get(wait_repeal.size() - 1);
                displayIndex = mGameImageList.indexOf(mGameImages);
                updateUI();
                return;
            }
        }
        finish();
    }

    private void showNext(int nextId) {
        if (ids.contains(nextId)) {
            displayIndex = ids.indexOf(nextId);
            wait_repeal.add(mGameImageList.get(displayIndex));
            updateUI();
        } else {
            finish();
        }
    }

    private void updateUI() {
        // TODO Auto-generated method stub
        if (mGameImageList != null && displayIndex >= 0 && displayIndex < mGameImageList.size()) {
            mGameImages = mGameImageList.get(displayIndex);
            url = mGameImages.getUrl();
            mControlList = mGameImages.getButtons();

            cleanControlItems();
            mScreenshotProvider.clean();

            Animation animIn = AnimationUtils.loadAnimation(getApplicationContext(), Tools.getAnimIn());
            Animation animOut = AnimationUtils.loadAnimation(getApplicationContext(), Tools.getAnimOut());

            if (View.INVISIBLE == screenshot_img.getVisibility()) {
                mScreenShotInListener.update(screenshot_img);
                mScreenShotOutListener.update(screenshot_img_spare);
            } else {
                mScreenShotInListener.update(screenshot_img_spare);
                mScreenShotOutListener.update(screenshot_img);
            }

            animIn.setDuration(1000);
            animIn.setAnimationListener(mScreenShotInListener);
            animOut.setDuration(1000);
            animOut.setAnimationListener(mScreenShotOutListener);
            screenshot_img.clearAnimation();
            screenshot_img_spare.clearAnimation();
            if (View.INVISIBLE == screenshot_img.getVisibility()) {
                screenshot_img.startAnimation(animIn);
                screenshot_img_spare.startAnimation(animOut);
                imageLoader.displayImage(Constants.TEST_IMG + url, screenshot_img, Tools.options, mScreenShotInListener);
            } else {
                screenshot_img_spare.startAnimation(animIn);
                screenshot_img.startAnimation(animOut);
                imageLoader.displayImage(Constants.TEST_IMG + url, screenshot_img_spare, Tools.options, mScreenShotInListener);
            }
        } else {
            finish();
        }

    }

    private void fillInGroup() {
        Tools.pool.submit(new Runnable() {

            @Override
            public void run() {

                // TODO Auto-generated method stub
                double zoomWidth = ((double) (displayMetrics[0] * 100 / mGameImages.getWidth())) / 100;
                double zoomHeight = ((double) (displayMetrics[1] * 100 / mGameImages.getHeight())) / 100;

                zoom = (zoomWidth + zoomHeight) / 2;
                final int marginTop = (displayMetrics[1] - (int) (mGameImages.getHeight() * zoom) - 50) / 2;

                if (mControlList != null && !mControlList.isEmpty()) {
                    for (final GameImageButtons mControlItem : mControlList) {
                        Button mBtn = new Button(getApplicationContext());
                        // mBtn.setBackgroundResource(R.drawable.c);
                        mBtn.setBackgroundColor(Color.TRANSPARENT);
                        // mBtn.setBackgroundResource(R.drawable.rectangle_frame);
                        int w = (int) (mControlItem.getW() * zoom);
                        int h = (int) (mControlItem.getH() * zoom);
                        int left = (int) (mControlItem.getLeft() * zoom);
                        int top = (int) (mControlItem.getTop() * zoom);
                        // + marginTop;
                        RelativeLayout.LayoutParams params = null;
                        if (w == h || 3 == anim_Type) {
                            params = new RelativeLayout.LayoutParams(def_width_px, def_width_px);
                            params.leftMargin = left + ((w - def_width_px) / 2);
                            params.topMargin = top + ((h - def_width_px) / 2);
                        } else {
                            params = new RelativeLayout.LayoutParams(w, h);
                            params.leftMargin = left;
                            params.topMargin = top;
                        }

                        mBtn.setTag(mControlItem.getNextImageId());
                        mBtn.setOnClickListener(ScreenshotExhibition.this);
                        mBtn.setLayoutParams(params);
                        ScreenShotControl mScreenShotControl = new ScreenShotControl(left, top, w, h, mBtn);

                        handler.obtainMessage(MSG_GROUP_UPDATE, mScreenShotControl).sendToTarget();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (screenshot_img.equals(v) || screenshot_img_spare.equals(v)) {
            showPrevious();
        } else {
            Object obj = v.getTag();
            if (obj != null) {
                int nextId = (Integer) obj;
                showNext(nextId);
            } else {
                finish();
            }
        }
    }

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public void onSensorListener(int fromOrientation, int toOrientation) {
        // TODO Auto-generated method stub
        switch (fromOrientation) {
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:// indicate_anim_reverse_landscape
                switch (toOrientation) {
                    case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:// indicate_anim_reverse_portrait
                        toDegrees = -90;
                        break;
                    case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:// indicate_anim_landscape
                        toDegrees = -180;
                        break;
                    case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:// indicate_anim_portrait
                        toDegrees = 90;
                        break;
                }
                break;
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:// indicate_anim_reverse_portrait
                switch (toOrientation) {
                    case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:// indicate_anim_reverse_landscape
                        toDegrees = 90;
                        break;
                    case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:// indicate_anim_landscape
                        toDegrees = -90;
                        break;
                    case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:// indicate_anim_portrait
                        toDegrees = -180;
                        break;
                }
                break;
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:// indicate_anim_landscape
                switch (toOrientation) {
                    case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:// indicate_anim_reverse_landscape
                        toDegrees = -180;
                        break;
                    case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:// indicate_anim_reverse_portrait
                        toDegrees = 90;
                        break;
                    case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:// indicate_anim_portrait
                        toDegrees = -90;
                        break;
                }
                break;
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:// indicate_anim_portrait
                switch (toOrientation) {
                    case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:// indicate_anim_reverse_landscape
                        toDegrees = -90;
                        break;
                    case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:// indicate_anim_reverse_portrait
                        toDegrees = -180;
                        break;
                    case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:// indicate_anim_landscape
                        toDegrees = 90;
                        break;
                }
                break;
        }
        boolean isDifferent = false;
        if ((ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE == fromOrientation && ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == toOrientation) || (ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE == toOrientation && ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == fromOrientation)) {
            isDifferent = true;
        } else {
            isDifferent = false;
        }
        tagDegrees = SensorListener.getTagDegrees(toOrientation, isDifferent);
        mScreenshotProvider.startRotateAnim(tagDegrees, toDegrees);
    }

    class ScreenShotInListener extends SimpleImageLoadingListener implements AnimationListener {
        private View view = null;
        private boolean isLoadingComplete = false;
        private boolean isAnimationEnd = false;

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            // TODO Auto-generated method stub
            super.onLoadingComplete(imageUri, view, loadedImage);
            isLoadingComplete = true;
            if (isLoadingComplete && isAnimationEnd) {
                isLoadingComplete = false;
                isAnimationEnd = false;
                fillInGroup();
            }
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            // TODO Auto-generated method stub
            super.onLoadingFailed(imageUri, view, failReason);
        }

        public void update(View view) {
            this.view = view;
            isLoadingComplete = false;
            isAnimationEnd = false;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            isAnimationEnd = true;
            if (isLoadingComplete && isAnimationEnd) {
                isLoadingComplete = false;
                isAnimationEnd = false;
                fillInGroup();
            }
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
            animation.cancel();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }

    }

    @SuppressLint("NewApi")
    class ScreenShotOutListener implements AnimationListener {
        private View view = null;

        public void update(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
            animation.cancel();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

    }
}
