package com.zeustel.top9;

import android.content.Intent;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.zeustel.top9.base.WrapNotActivity;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.ExhibitionView;

/**
 * 图片展览
 */
public class ExhibitionActivity extends WrapNotActivity {
    public static final String EXTRA_NAME_PATH = "imgPaths";
    public static final String EXTRA_NAME_SCHEME = "Scheme";
    public static final String EXTRA_NAME_POSITION = "position";
    private String[] imgPaths = null;
    private int position;
    private ImageDownloader.Scheme scheme;
    private ExhibitionView exhibitionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();

        }
        imgPaths = (String[]) intent.getSerializableExtra(EXTRA_NAME_PATH);
        position = intent.getIntExtra(EXTRA_NAME_POSITION, position);
        scheme = (ImageDownloader.Scheme) intent.getSerializableExtra(EXTRA_NAME_SCHEME);
        if (imgPaths == null || imgPaths.length <= 0) {
            finish();
        }
        exhibitionView = new ExhibitionView(getApplicationContext());
        if (scheme == null) {
            exhibitionView.fillContent(imgPaths);
        } else {
            exhibitionView.fillContent(imgPaths, scheme);
        }
        Tools.fullScreen(this);
        setContentView(exhibitionView);
        /*当组件绘制完成后调用*/
        Tools.endCreateOperate(exhibitionView, new Runnable() {
            @Override
            public void run() {
                exhibitionView.setDisplayedChild(position);
            }
        });
    }

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }
}
