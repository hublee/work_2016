package com.zeustel.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ImageBucketAdapter extends BaseAdapter {
    private Context context = null;
    private int resource = 0;
    private List<ImageBucket> list;
    private LayoutInflater inflater = null;
    private ImageLoader mImageLoader = ImageLoader.getInstance();

    public ImageBucketAdapter(Context context, int resource, List<ImageBucket> list) {
        this.context = context;
        this.resource = resource;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int location) {
        // TODO Auto-generated method stub
        return list == null ? null : list.get(location);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int location, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        Holder holder;
        if (arg1 == null) {
            holder = new Holder();
            arg1 = inflater.inflate(resource, null);
            holder.gallery2_bucket_img = (ImageView) arg1.findViewById(R.id.gallery2_bucket_img);
            holder.gallery2_bucket_name = (TextView) arg1.findViewById(R.id.gallery2_bucket_name);
            holder.gallery2_bucket_hasCount = (TextView) arg1.findViewById(R.id.gallery2_bucket_hasCount);
            arg1.setTag(holder);
        } else {
            holder = (Holder) arg1.getTag();
        }
        ImageBucket mImageBucket = (ImageBucket) getItem(location);
        if (mImageBucket != null) {
            List<ImageItem> mImageItems = mImageBucket.imageList;
            if (mImageItems != null && mImageItems.size() > 0) {
                ImageItem mImageItem = mImageBucket.imageList.get(0);
                if (mImageItem != null) {
                    Gallery2Utils.setImg_gallery2(mImageLoader, holder.gallery2_bucket_img, mImageItem);
                    holder.gallery2_bucket_name.setText(mImageBucket.bucketName);
                    holder.gallery2_bucket_hasCount.setText("( " + mImageBucket.count + " )");
                }
            }
        }
        return arg1;
    }

    class Holder {
        private ImageView gallery2_bucket_img;
        private TextView gallery2_bucket_name;
        private TextView gallery2_bucket_hasCount;
    }
}
