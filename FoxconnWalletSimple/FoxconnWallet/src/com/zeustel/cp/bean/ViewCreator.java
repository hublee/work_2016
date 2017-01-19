package com.zeustel.cp.bean;

/**
 * Created by Administrator on 2015/12/14.
 */
public abstract class ViewCreator {

        public abstract <T extends PopView> T createView(Class<T> c);

}
