package com.zeustel.top9;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.zeustel.top9.assist.OpenPlatform;
import com.zeustel.top9.temp.TempNativeFragment;
import com.zeustel.top9.utils.DButilsHelper;
import com.zeustel.top9.utils.NativeUtils;
import com.zeustel.top9.utils.SharedPreferencesUtils;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.gif.GifData;
import com.zeustel.top9.widgets.RecycleImageView;


public class NavigationActivity extends AppCompatActivity {
    private static final boolean BAK_ABLE = false;
    private Handler handler = new Handler();
    private RecycleImageView mRecycleImageView;

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        DButilsHelper.getDbUtils(getApplicationContext());
        //FM TEST
        //        FMHelper.getInstance(getApplicationContext());
        if (BAK_ABLE) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.navigation, new TempNativeFragment()).commit();
            }
            return;
        }
        Tools.loadAnim(getApplicationContext());
        OpenPlatform.initUmengSDK(getApplicationContext(), true);
        OpenPlatform.initSharedSDK(getApplicationContext());
        if (false) {
            //第一次缓存
            if (SharedPreferencesUtils.isFirstlaunch(getApplicationContext())) {
                Tools.pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        NativeUtils.importChatFaces(getApplicationContext());
                        GifData.loadNativeFace(getApplicationContext());
                    }
                });
                Tools.pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        Tools.log_i(NavigationActivity.class, "run", "import local cache");
                        NativeUtils.importCacheImgs(getApplicationContext());
                        try {
                            NativeUtils.importCache2Database(getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        toContainerActivity(true);
                    }
                });

                SharedPreferencesUtils.firstLaunch(getApplicationContext());
            } else {
                toContainerActivity(false);
            }
        } else {
            toContainerActivity(false);
        }
    }

    private void toContainerActivity(boolean isFirst) {
        mRecycleImageView = (RecycleImageView) findViewById(R.id.navigation_img);
        mRecycleImageView.setIsAutoRecycle(true);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
                startActivity(intent);
                finish();
            }
        }, isFirst ? 500 : 1000);
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
        System.gc();
    }
}
