package com.zeustel.cp.bean;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/18 11:47
 */
public class Angle270_360 extends Angle {
    public Angle270_360() {
    }

    public Angle270_360(float angle, int len, int x, int y) {
        super(angle, len, x, y);
    }

    @Override
    public int getTagX() {
        int diffX = (int) (Math.cos(Math.toRadians(getAngle())) * getLen());
        return getX() - diffX;
    }

    @Override
    public int getTagY() {
        int diffY = (int) (Math.sin(Math.toRadians(getAngle())) * getLen());
        return getY() - diffY;
    }
}
