package com.zeustel.gallery;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageGridActivity extends Activity {
    public static final int MSG_SELECT_IMG = 1;
    protected static final int MSG_GET_FAILD = 3;
    public static int ITEM_MEASURE = 0;
    public static int ITEM_FRAME_MEASURE = 0;
    private List<ImageItem> dataList;
    private GridView gallery2_select_gridview;
    private ImageGridAdapter adapter;
    private Button gallery2_select_commit;
    private Intent intent = null;
    private ArrayList<String> selectImgs = new ArrayList<String>();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_SELECT_IMG:
                    int size = selectImgs.size();
                    String text = getString(R.string.confirm);
                    gallery2_select_commit.setText(size == 0 ? text : text + " ( " + size + " )");
                    break;

                case MSG_GET_FAILD:
                    break;
            }

        }
    };
    private ImageView mGallery2_select_img_frame = null;
    private ImageView mGallery2_select_flag = null;
    private ImageItem mImageItem = null;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery2_select);
        intent = getIntent();
        if (intent == null) {
            finish();
        }
        Serializable mSerializable = intent.getSerializableExtra(Gallery2Utils.EXTRA_IMAGE_LIST);
        if (mSerializable == null) {
            finish();
        }
        dataList = (List<ImageItem>) mSerializable;
        if (dataList == null || (dataList != null && dataList.size() <= 0)) {
            finish();
        }
        int dip_5 = Tool.dip2px(this, 5);
        ITEM_FRAME_MEASURE = (Tool.SCREEN_WIDTH - (dip_5 * 4)) / 3;
        ITEM_MEASURE = ITEM_FRAME_MEASURE - Tool.dip2px(this, 4);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        gallery2_select_gridview = (GridView) findViewById(R.id.gallery2_select_gridview);
        gallery2_select_commit = (Button) findViewById(R.id.gallery2_select_commit);
        adapter = new ImageGridAdapter(ImageGridActivity.this, R.layout.gallery2_select_item, dataList);
        gallery2_select_gridview.setAdapter(adapter);
        gallery2_select_commit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Gallery2Utils.setResult(ImageGridActivity.this, selectImgs);
                finish();
            }
        });
        gallery2_select_gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter != null && view != null) {
                    Object obj = adapter.getItem(position);
                    if (obj != null) {
                        mImageItem = (ImageItem) obj;
                        mGallery2_select_img_frame = adapter.getGallery2_select_img_frame(view);
                        mGallery2_select_flag = adapter.getGallery2_select_flag(view);
                        if (mGallery2_select_img_frame != null && mGallery2_select_flag != null) {
                            if (!mImageItem.loadFailed) {

                                if (mImageItem.isSelected) {
                                    mGallery2_select_img_frame.setSelected(false);
                                    mGallery2_select_flag.setVisibility(View.INVISIBLE);
                                    mImageItem.isSelected = false;
                                    if (selectImgs.contains(mImageItem.imagePath)) {
                                        selectImgs.remove(mImageItem.imagePath);
                                    }
                                } else {
                                    if (selectImgs.size() < Gallery2Utils.MAX_SELECT_COUNT) {
                                        mGallery2_select_img_frame.setSelected(true);
                                        mGallery2_select_flag.setVisibility(View.VISIBLE);
                                        mImageItem.isSelected = true;
                                        if (mImageItem.imagePath != null) {
                                            selectImgs.add(mImageItem.imagePath);
                                        }
                                    } else {
                                        Tool.showToast(ImageGridActivity.this, getString(R.string.gallery2_select_max, Gallery2Utils.MAX_SELECT_COUNT));
                                    }
                                }
                                handler.sendEmptyMessage(MSG_SELECT_IMG);

                            } else {
                                Tool.showToast(ImageGridActivity.this, R.string.invalid_img);
                            }
                        }

                    }

                }

            }

        });
    }
}
