package com.zeustel.top9.bean;

import com.lidroid.xutils.db.annotation.Column;

/**
 * @author NiuLei
 * @date 2015/6/24 23:14
 */
public class  HtmlPaper extends BaseHtml {
    //发布内容
    @Column(column = "content")
    private String content;
    //html页面类型
    @Column(column = "htmlPaperType")
    private int htmlPaperType = HtmlPaperType.EVALUATING;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /*
        public int getHtmlPaperType() {
            return htmlPaperType;
        }

        public void setHtmlPaperType(int htmlPaperType) {
            this.htmlPaperType = htmlPaperType;
        }*/
    public class HtmlPaperType {
        //评测
        public static final int EVALUATING = 0;
        //社区
        public static final int COMMUNITY = 1;
    }
}
