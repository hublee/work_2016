package com.zeustel.gallery;

import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

//删除显示不出图片的文件夹
public class Gallery2LoadListener extends SimpleImageLoadingListener {
    private boolean isLoadSourcePath = false;

    public Gallery2LoadListener(boolean isLoadSourcePath) {
        this.isLoadSourcePath = isLoadSourcePath;
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        // TODO Auto-generated method stub
        super.onLoadingFailed(imageUri, view, failReason);
    }

}
