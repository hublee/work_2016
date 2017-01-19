package com.zeustel.cp.bean;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/18 11:37
 */
public abstract class Angle {
    /*角度*/
    private float angle;
    /*x坐标*/
    private int x;
    /*y坐标*/
    private int y;
    /*斜边长*/
    private int len;

    public Angle() {
    }

    public Angle(float angle, int len, int x, int y) {
        this.angle = angle;
        this.len = len;
        this.x = x;
        this.y = y;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public abstract int getTagX();
    public abstract int getTagY();
}
