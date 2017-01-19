package com.zeustel.gallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.lang.reflect.Method;

public class Tool {
    protected static int SCREEN_WIDTH;
    protected static int SCREEN_HEIGHT;

    protected static DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_empty).showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_empty).cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).displayer(new SimpleBitmapDisplayer()).build();

    protected static void showToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    protected static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    protected static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取屏幕分辨率
     *
     * @return 宽高数组
     */
    protected static int[] getDisplayMetrics(Context context) {
        try {
            return getRealityDpi(context);
        } catch (Exception e) {
            e.printStackTrace();
            return getNormalDpi(context);
        }
    }

    /**
     * 获取屏幕分辨率 普通屏幕
     */
    private static int[] getNormalDpi(Context context) {
        int dpi[] = new int[2];
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        dpi[0] = dm.widthPixels;
        dpi[1] = dm.heightPixels;
        return dpi;
    }

    /**
     * 获取屏幕分辨率 屏幕上有按键
     */
    private static int[] getRealityDpi(Context context) throws Exception {
        int dpi[] = new int[2];
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        Class c = Class.forName("android.view.Display");
        Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
        method.invoke(display, dm);
        dpi[0] = dm.widthPixels;
        dpi[1] = dm.heightPixels;
        return dpi;
    }
}
