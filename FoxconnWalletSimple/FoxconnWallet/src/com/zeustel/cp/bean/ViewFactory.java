package com.zeustel.cp.bean;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.view.View;

import com.zeustel.cp.intf.IView;
import com.zeustel.cp.utils.Constants;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.utils.Tools;
import com.zeustel.cp.views.Check;
import com.zeustel.cp.views.Exchange;
import com.zeustel.cp.views.ExchangeError;
import com.zeustel.cp.views.ExchangeOk;
import com.zeustel.cp.views.Gift;
import com.zeustel.cp.views.Login;
import com.zeustel.cp.views.PopDiagitalCodeView;
import com.zeustel.cp.views.UserSet;

/**
 * Created by Administrator on 2015/12/14.
 * 视图工厂类
 */
public class ViewFactory extends ViewCreator {
    private static ViewFactory viewFactory = new ViewFactory();
    private PopView popView = null;
    private static Context context;

    private static List<Class> classes = new ArrayList<Class>();
//    private static List<PopView> popViews = new ArrayList<PopView>();
    private static ConcurrentLinkedQueue<Integer> saveViews  = new  ConcurrentLinkedQueue<Integer>();

    private IView iView;

    private ViewFactory(){

    }

    public static ViewFactory getInstance(){
        return viewFactory;
    }

    public static void setContext(Context context){
        ViewFactory.context = context;
    }

    public <T extends PopView> T createView(int viewId){
        PopView popView = null;
        switch (viewId){
            case Constants.EXCHANGE_VIEWID:
                popView = viewFactory.createView(Exchange.class,viewId);
                if(popView!=null){
//                    popView.setViewWidth(800);
//                    popView.setViewHeight(700);
                	//Log.e("CHECK_VIEWID", "width:"+Tools.dip2px(context, 660)+",height:"+Tools.dip2px(context, 500));
                    popView.setViewWidth((int)(Tools.dip2px(context, 560)/2));
                	popView.setViewHeight((int)(Tools.dip2px(context, 580)/2));
                }
                break;
            case Constants.GIFT_VIEWID:
                popView = viewFactory.createView(Gift.class,viewId);
                if(popView != null){
                    popView.setViewWidth(500);
                    popView.setViewHeight(400);
                }
                break;
            case Constants.LOGIN_VIEWID:
                popView = viewFactory.createView(Login.class,viewId);
                if(popView != null){
                    popView.setViewWidth(500);
                    popView.setViewHeight(400);
                }
                break;
            case Constants.USERSET_VIEWID:
                popView = viewFactory.createView(UserSet.class,viewId);
                if(popView != null){
                    popView.setViewWidth(500);
                    popView.setViewHeight(400);
                }
                break;
            case Constants.CHECK_VIEWID:
                popView = viewFactory.createView(Check.class,viewId);
                if(popView!=null){
//                    popView.setViewWidth(800);
//                    popView.setViewHeight(700);
            
                	popView.setViewWidth((int)(Tools.dip2px(context, 560)/2));
                	popView.setViewHeight((int)(Tools.dip2px(context, 580)/2));
                }
                break;
            case Constants.EXCHANGE_VER:
            	popView = viewFactory.createView(PopDiagitalCodeView.class,viewId);
            	if(popView!=null){
            		popView.setViewWidth((int)(Tools.dip2px(context, 560)/2));
            		popView.setViewHeight((int)(Tools.dip2px(context, 580)/2));
            	}
            	break;
            case Constants.EXCHANGE_OK_VIEWID:
                popView = viewFactory.createView(ExchangeOk.class,viewId);
                if(popView!=null){
                	popView.setViewWidth((int)(Tools.dip2px(context, 560)/2));
                	popView.setViewHeight((int)(Tools.dip2px(context, 580)/2));
                }
                break;
            case Constants.EXCHANGE_ERROR_VIEWID:
                popView = viewFactory.createView(ExchangeError.class,viewId);
                if(popView!=null){
                    popView.setViewWidth(400);
                    popView.setViewHeight(400);
                }
                break;
//            case Constants.NEWPOP_VIEWID:
//                popView = viewFactory.createView(NewFmContrller.class,viewId);
//                if(popView!=null){
//                    popView.setViewWidth(800);
//                    popView.setViewHeight(750);
//                    popView.setNeedMove(true);
//                }
//                break;
            default:
                break;

        }

        return  (T)popView;
    }


    /**
     * 根据类名创建视图
     * @param c
     * @param <T>
     * @return
     */
    public <T extends PopView> T createView(Class<T> c){
        try{
            Class tempClass = null;
            if(classes.contains(Class.forName(c.getName()))){
                int index = classes.indexOf(Class.forName(c.getName()));
                tempClass = classes.get(index);
                return null;
            }else{
                tempClass = Class.forName(c.getName());
                classes.add(tempClass);

                Constructor constructor = tempClass.getDeclaredConstructor(new Class[]{Context.class});
                constructor.setAccessible(true);
                popView =(PopView) constructor.newInstance(context);
                popView.addViewListener(iView);

//                popViews.add(popView);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return (T)popView;
    }

    /**
     * 根据类名和id创建视图
     * @param c 类名
     * @param viewId 视图编号
     * @param <T>
     * @return
     */
    public <T extends PopView> T createView(Class<T> c,int viewId){
        try{
            Class tempClass = null;
            if(classes.contains(Class.forName(c.getName()))){
                int index = classes.indexOf(Class.forName(c.getName()));
                tempClass = classes.get(index);
                return null;
            }else{
                tempClass = Class.forName(c.getName());
                classes.add(tempClass);

                Constructor constructor = tempClass.getDeclaredConstructor(new Class[]{Context.class});
                constructor.setAccessible(true);
                popView =(PopView) constructor.newInstance(context);
                popView.setViewId(viewId);
                popView.addViewListener(iView);

//                popViews.add(popView);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return (T)popView;
    }


    public void addViewListener(IView iView){
        this.iView = iView;
    }

    public void closeView(View view){
        if(classes.contains(view.getClass())) {
            int index = classes.indexOf(view.getClass());
            classes.remove(index);

            iView.dropView(view);
        }
    }

    public void closeView(View view,int viewId){
        closeView(view,viewId,true);
    }

    public void closeView(View view,int viewId,boolean flag){
        if(classes.contains(view.getClass())){
            int index = classes.indexOf(view.getClass());
            classes.remove(index);

            iView.dropView(view);

            if(saveViews.isEmpty()) {
//                Tools.showController();
            }

            if(flag) {
                saveViews.add(viewId);
            }
        }
    }

    public void closeController(View view){
        iView.dropView(view);
    }


    public int getSize(){
        return classes.size();
    }


    public void pageBack(View view,int viewId){
        closeView(view,viewId,false);
        synchronized (saveViews){
            if(!saveViews.isEmpty()) {
                int tempViewId = saveViews.poll();
                if (tempViewId != 0)
                iView.addView(tempViewId);
            }else{
            	SdkUtils.LogD("saveViews size","size:"+saveViews.size());
//                Tools.showController();
            }
        }
    }
    
    public void destroy(){
    	saveViews = null;
    	classes = null;
    }

}
