package com.zeustel.top9.utils;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;

public class SensorListener implements SensorEventListener {
    public static final int MSG_WHAT = 11124;
    private static final int _DATA_X = 0;
    private static final int _DATA_Y = 1;
    private static final int _DATA_Z = 2;
    private static final int ORIENTATION_UNKNOWN = -1;
    private float[] values = null;
    private int orientation = ORIENTATION_UNKNOWN;
    private float x, y, z;
    private Handler handler = null;
    private OnSensorListener mOnSensorListener = null;
    private int fromOrientation, toOrientation;

    public SensorListener(Handler handler, OnSensorListener mOnSensorListener) {
        this.handler = handler;
        this.mOnSensorListener = mOnSensorListener;
    }

    public static float getTagDegrees(int orientation, boolean isDifferent) {
        switch (orientation) {
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:// indicate_anim_reverse_landscape
                if (isDifferent) {
                    return -90;
                } else {
                    return 270;
                }
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:// indicate_anim_reverse_portrait
                return 180;
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:// indicate_anim_landscape
                return 90;
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:// indicate_anim_portrait
                return 0;
        }
        return 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        values = event.values;
        orientation = ORIENTATION_UNKNOWN;
        x = -values[_DATA_X];
        y = -values[_DATA_Y];
        z = -values[_DATA_Z];
        float magnitude = x * x + y * y;
        // Don't trust the angle if the magnitude is small compared to the y
        // value
        if (magnitude * 4 >= z * z) {
            float OneEightyOverPi = 57.29577957855f;
            float angle = (float) Math.atan2(-y, x) * OneEightyOverPi;
            orientation = 90 - (int) Math.round(angle);
            // normalize to 0 - 359 range
            while (orientation >= 360) {
                orientation -= 360;
            }
            while (orientation < 0) {
                orientation += 360;
            }
        }
        if (handler != null) {
            handler.obtainMessage(MSG_WHAT, orientation).sendToTarget();
        }
    }

    public void handleSensorMessage(Message msg) {
        int orientation = (Integer) msg.obj;
        if (orientation > 45 && orientation < 135) {
            toOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
        } else if (orientation > 135 && orientation < 225) {
            toOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
        } else if (orientation > 225 && orientation < 315) {
            toOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
            toOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        if (fromOrientation != toOrientation) {
            if (mOnSensorListener != null) {
                mOnSensorListener.onSensorListener(fromOrientation, toOrientation);
            }
            fromOrientation = toOrientation;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    public static interface OnSensorListener {
        public void onSensorListener(int fromOrientation, int toOrientation);
    }

}
