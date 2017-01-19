package com.zeustel.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

public class Gallery2MainActivity extends Activity {
    private List<ImageBucket> dataList;
    private ListView gallery2_main_listView = null;
    private ImageBucketAdapter adapter;// 自定义的适配器
    private AlbumHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery2_main);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        Gallery2Utils.MAX_SELECT_COUNT = intent.getIntExtra(Gallery2Utils.MAX_SELECT, 0);
        if (Gallery2Utils.MAX_SELECT_COUNT == 0) {
            finish();
        }
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (Tool.SCREEN_HEIGHT == 0 || Tool.SCREEN_WIDTH == 0) {
            int metrics[] = Tool.getDisplayMetrics(this);
            Tool.SCREEN_WIDTH = metrics[0];
            Tool.SCREEN_HEIGHT = metrics[1];
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        dataList = helper.getImagesBucketList(true);
    }

    /**
     * 初始化view视图
     */
    private void initView() {
        gallery2_main_listView = (ListView) findViewById(R.id.gallery2_main_listView);
        adapter = new ImageBucketAdapter(Gallery2MainActivity.this, R.layout.gallery2_main_item, dataList);
        gallery2_main_listView.setAdapter(adapter);
        gallery2_main_listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = adapter.getItem(position);
                if (item != null) {
                    ImageBucket mImageBucket = (ImageBucket) item;
                    if (mImageBucket != null) {
                        List<ImageItem> mImageItems = mImageBucket.imageList;
                        if (mImageItems != null && mImageItems.size() > 0) {
                            ImageItem mImageItem = mImageItems.get(0);
                            if (mImageItem != null) {
                                if (!mImageItem.loadFailed) {
                                    Intent intent = new Intent(Gallery2MainActivity.this, ImageGridActivity.class);
                                    intent.putExtra(Gallery2Utils.EXTRA_IMAGE_LIST, (Serializable) mImageItems);
                                    startActivityForResult(intent, Gallery2Utils.REQUEST_CODE);
                                } else {
                                    Tool.showToast(Gallery2MainActivity.this, R.string.invalid_directory);
                                }

                            }

                        }
                    }
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery2Utils.REQUEST_CODE && resultCode == Gallery2Utils.RESULT_CODE && data != null) {
            Serializable mSerializable = data.getSerializableExtra(Gallery2Utils.EXTRA_NAME);
            if (mSerializable != null) {
                Gallery2Utils.setResult(Gallery2MainActivity.this, mSerializable);
                finish();
            }
        }
    }
}
