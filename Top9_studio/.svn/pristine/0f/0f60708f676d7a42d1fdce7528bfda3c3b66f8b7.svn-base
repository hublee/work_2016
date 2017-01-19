package com.zeustel.top9.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 版本更新
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/23 17:10
 */
public class Version implements Serializable {
    private String url; //下载地址
    private List<String> remark; //更新说明
    private int channel = Channel.TOP9; //渠道
    private int verCode;//版本号
    private String verifyCode; //校验码
    private long time; //更新时间
    private long len;//文件长度
    private String verName; //版本名称

    public long getLen() {
        return len;
    }

    public void setLen(long len) {
        this.len = len;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public List<String> getRemark() {
        return remark;
    }

    public void setRemark(List<String> remark) {
        this.remark = remark;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVerCode() {
        return verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public class Channel {
        public static final int TOP9 = 0; //top9
        public static final int QIHU = 1; //360
    }
}
