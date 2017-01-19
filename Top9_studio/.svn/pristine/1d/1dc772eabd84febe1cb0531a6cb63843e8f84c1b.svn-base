package com.zeustel.top9.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局搜索
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/18 14:14
 */
public class Search implements Serializable {
    public enum Type {
        /*评测*/
        EVALUATING(3),
        /*资讯*/
        INFO(1),
        /*话题*/
        TOPIC(2);
        private static Map<Integer, Type> map = new HashMap();

        static {
            for (Type type : Type.values()) {
                Type.map.put(type.flag, type);
            }
        }

        private int flag;

        Type(int flag) {
            this.flag = flag;
        }

        public static Type getInstance(int flag) {
            return Type.map.get(flag);
        }

        public int value() {
            return this.flag;
        }
    }

    //评测列表
    private ArrayList<GameEvaluating> evaluateList;
    //资讯列表
    private ArrayList<CommunityInfo> newsList;
    //话题列表
    private ArrayList<CommunityTopic> topicList;
    //关键字
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<GameEvaluating> getEvaluateList() {
        return evaluateList;
    }

    public void setEvaluateList(ArrayList<GameEvaluating> evaluateList) {
        this.evaluateList = evaluateList;
    }

    public ArrayList<CommunityInfo> getNewsList() {
        return newsList;
    }

    public void setNewsList(ArrayList<CommunityInfo> newsList) {
        this.newsList = newsList;
    }

    public ArrayList<CommunityTopic> getTopicList() {
        return topicList;
    }


    public void setTopicList(ArrayList<CommunityTopic> topicList) {
        this.topicList = topicList;
    }
}
