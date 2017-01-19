package com.zeustel.cp.bean;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/7/6 13:57
 */
public class Result {
    public static final int SUCCESS = 1;
    private String data;
    private int success;
    private int error;
    private String msg;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Result{" +
                "count=" + count +
                ", data=" + data.toString() +
                ", success=" + success +
                ", error=" + error +
                ", msg='" + msg + '\'' +
                '}';
    }
}
