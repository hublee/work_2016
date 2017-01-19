package com.zeustel.cp.bean;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/18 12:19
 */
public class AngleFactory {
    public static Angle getInstance(float angle, int len, int x, int y,int sign) {
        final float tempAngle = angle % 90;
        final float tempangle2 = angle / 360;
        if (tempangle2 > 1) {
            angle = angle % 360;
            len = (int) (len * 1.5f);
//            len = (int) (len * (1 + (tempangle2 * 0.5f)));
        }


        Angle instance = null;
//        if (angle > 90 && angle <= 180) {
//            instance = new Angle90_180(tempAngle, len, x, y);
//        } else if (angle > 180 && angle <= 270) {
//            instance = new Angle180_270(tempAngle, len, x, y);
//        } else if (angle > 270 && angle <= 360) {
//            instance = new Angle270_360(tempAngle, len, x, y);
//        } else {
//            instance = new Angle0_90(tempAngle, len, x, y);
//        }


        switch (sign){
            case 1://上
                    if (angle > 90 && angle <= 180) {
                        instance = new Angle0_90(tempAngle-5, len, x, y);
                    } else{
                        instance = new Angle270_360(tempAngle-5, len, x, y);
                    }
                break;
            case 2://下
                    if (angle < 90) {
                        instance = new Angle90_180(tempAngle-5, len, x, y);
                    } else{
                        instance = new Angle180_270(tempAngle-5, len, x, y);
                    }
                break;
            case 3://左
                    if (angle < 90) {
                        instance = new Angle180_270(tempAngle-5, len, x, y);
                    } else{
                        instance = new Angle270_360(tempAngle-5, len, x, y);
                    }
                break;
            case 4://右
                    if (angle < 90) {
                        instance = new Angle0_90(tempAngle-5, len, x, y);
                    } else{
                        instance = new Angle90_180(tempAngle-5, len, x, y);
                    }
                break;
            case 5://左上
//                    if (angle > 100) {
//                        instance = new Angle270_360(tempAngle+10, len, x, y);
//                    }
//                    else{
                        instance = new Angle270_360(angle, len, x, y);
//                    }
                break;
            case 6://右上
//                    if (angle > 100) {
//                        instance = new Angle0_90(tempAngle+10, len, x, y);
//                    } else{
                        instance = new Angle0_90(angle, len, x, y);
//                    }
                break;
            case 7://左下
//                    if (angle > 100) {
//                        instance = new Angle180_270(tempAngle+10, len, x, y);
//                    } else{
                        instance = new Angle180_270(angle, len, x, y);
//                    }
                break;
            case 8://右下
//                    if (angle > 100) {
//                        instance = new Angle90_180(tempAngle, len, x, y);
//                    } else{
                        instance = new Angle90_180(angle, len, x, y);
//                    }
                break;
        }






        return instance;

    }
}
