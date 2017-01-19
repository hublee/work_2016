package com.zeustel.top9.bean.community;

import com.zeustel.top9.bean.SubUserInfo;

import java.io.Serializable;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/10 17:31
 */
public class Content implements Serializable{
    /**
     * 内容类型
     */
    public enum ContentType {
        TEXT(1),//文本
        AUDIO(0)//语音
        ;

        private int type;

        ContentType(int type) {
            this.type = type;
        }
        public int toInt(){
            return this.type;
        }
    }


    private SubUserInfo atUser;
    private String msg;
    //
    private ContentType contentType = ContentType.TEXT;

    public SubUserInfo getAtUser() {
        return atUser;
    }

    public void setAtUser(SubUserInfo atUser) {
        this.atUser = atUser;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
