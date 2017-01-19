package com.zeustel.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ImageGridAdapter extends BaseAdapter {

    private Context context = null;
    private int resource = 0;
    private List<ImageItem> list;
    private LayoutInflater inflater = null;
    private ImageLoader mImageLoader = ImageLoader.getInstance();

    public ImageGridAdapter(Context context, int resource, List<ImageItem> list) {
        this.context = context;
        this.resource = resource;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    private static void setItemParams(ImageView mframeImageView, ImageView mImageView) {
        RelativeLayout.LayoutParams frame_params = new RelativeLayout.LayoutParams(ImageGridActivity.ITEM_FRAME_MEASURE, ImageGridActivity.ITEM_FRAME_MEASURE);
        frame_params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mframeImageView.setLayoutParams(frame_params);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ImageGridActivity.ITEM_MEASURE, ImageGridActivity.ITEM_MEASURE);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mImageView.setLayoutParams(params);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public ImageView getGallery2_select_img_frame(View view) {
        return (ImageView) view.findViewById(R.id.gallery2_select_img_frame);
    }

    public ImageView getGallery2_select_img(View view) {
        return (ImageView) view.findViewById(R.id.gallery2_select_img);
    }

    public ImageView getGallery2_select_flag(View view) {
        return (ImageView) view.findViewById(R.id.gallery_select_flag);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(resource, null);
            holder.gallery2_select_img_frame = getGallery2_select_img_frame(convertView);
            holder.gallery2_select_img = getGallery2_select_img(convertView);
            holder.gallery_select_flag = getGallery2_select_flag(convertView);
            setItemParams(holder.gallery2_select_img_frame, holder.gallery2_select_img);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final ImageItem mImageItem = (ImageItem) getItem(position);
        if (mImageItem != null) {
            Gallery2Utils.setImg_gallery2(mImageLoader, holder.gallery2_select_img, mImageItem);
            if (mImageItem.isSelected) {
                holder.gallery2_select_img_frame.setSelected(true);
                holder.gallery_select_flag.setVisibility(View.VISIBLE);
            } else {
                holder.gallery2_select_img_frame.setSelected(false);
                holder.gallery_select_flag.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

    static class Holder {
        private ImageView gallery2_select_img_frame;
        private ImageView gallery2_select_img;
        private ImageView gallery_select_flag;
    }
}
