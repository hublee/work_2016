package com.zeustel.cp.bean;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.zeustel.cp.intf.IView;

/**
 * Created by Administrator on 2015/12/14.
 */
public abstract class PopView extends LinearLayout implements View.OnClickListener{
    protected IView iView;
    private int viewId;

    private View rootView;

    private int width;
    private int height;
    private boolean needMove = true;
    
    protected OnPopListener popListener;
    public interface OnPopListener{
    	void onPop(boolean allow);
    }
    public void setOnPopListener(OnPopListener popListener){
    	this.popListener = popListener;
    }
    public PopView(Context context){
        super(context);
        initView();
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public void addViewListener(IView iView){
        this.iView = iView;
    }

    /**
     * 绑定视图
     */
    protected abstract void initView();


    public void close(){
        ViewFactory.getInstance().closeView(this,viewId);
    }

    public void addViews(int viewId){
        iView.addView(viewId);
    }

    /**
     * 返回上一页
     */
    public void pageBack(){
        ViewFactory.getInstance().pageBack(this, viewId);
    }

    public int getViewWidth(){
        return this.width;
    }

    public void setViewWidth(int width){
        this.width = width;
    }

    public int getViewHeight(){
        return this.height;
    }

    public void setViewHeight(int height){
        this.height = height;
    }

    public void setNeedMove(boolean needMove){
        this.needMove  = needMove;
    }

    public boolean isNeedMove(){
        return this.needMove;
    }
}
