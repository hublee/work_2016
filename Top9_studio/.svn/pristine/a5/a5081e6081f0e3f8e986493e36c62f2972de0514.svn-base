package com.zeustel.gallery;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.Serializable;
import java.util.List;

public class Gallery2Utils {
    public static final String MAX_SELECT = "max_select";
    public static final int RESULT_CODE = 12;
    public static final String EXTRA_NAME = "select_img";
    protected static final String EXTRA_IMAGE_LIST = "imagelist";
    public static int REQUEST_CODE = 11;
    protected static int MAX_SELECT_COUNT = 7;

    protected static void setResult(Activity mActivity, Serializable mSerializable) {
        Intent data = new Intent();
        data.putExtra(Gallery2Utils.EXTRA_NAME, mSerializable);
        mActivity.setResult(Gallery2Utils.RESULT_CODE, data);
    }

    protected static void setImg_gallery2(final ImageLoader mImageLoader, final ImageView mImageView, final ImageItem mImageItem) {
        final String sourcePath = mImageItem.imagePath;
        mImageLoader.displayImage("file://" + sourcePath, mImageView, Tool.options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                // TODO Auto-generated method stub
                super.onLoadingFailed(imageUri, view, failReason);
                mImageItem.loadFailed = true;
            }
        });
    }

    /**
     * activity跳转相册
     *
     * @param activity  activity对象
     * @param maxSelect 最多可选
     */
    public static void startGallery(Activity activity, int maxSelect) {
        Intent selectIntent = new Intent(activity, Gallery2MainActivity.class);
        selectIntent.putExtra(Gallery2Utils.MAX_SELECT, maxSelect);
        activity.startActivityForResult(selectIntent, Gallery2Utils.REQUEST_CODE);
    }

    /**
     * fragment跳转相册
     *
     * @param fragment  fragment对象
     * @param maxSelect 最多可选
     */
    public static void startGallery(Fragment fragment, int maxSelect) {
        Intent selectIntent = new Intent(fragment.getContext(), Gallery2MainActivity.class);
        selectIntent.putExtra(Gallery2Utils.MAX_SELECT, maxSelect);
        fragment.startActivityForResult(selectIntent, Gallery2Utils.REQUEST_CODE);
    }

    /**
     * 根据回调获取返回图片路径集合
     */
    public static List<String> onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Gallery2Utils.REQUEST_CODE && resultCode == Gallery2Utils.RESULT_CODE && null != data) {
            return (List<String>) data.getSerializableExtra(Gallery2Utils.EXTRA_NAME);
        }
        return null;
    }
}
