package com.zeustel.cp.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zeustel.cp.intf.IMenu;
import com.zeustel.cp.utils.Tools;
import com.zeustel.foxconn.cp_sdk.R;


/**
 * Created by Do on 2016/2/24.
 */
public class MenuItem extends LinearLayout{
    private ImageView img;
//    private TextView title;

    private int viewId;
    private IMenu iMenu;

    private int viewFlag = 0;

//    public static final int EXCHANGE_VIEW = 1;
    public static final int SHOP_VIEW = 2;
    public static final int cp_sdk_game = 3;
    public static final int cp_sdk_gift = 4;
    public static final int CENTER_VIEW = 5;


    public MenuItem(Context context){
        super(context);

        initView();
    }

    private void initView(){
        inflate(getContext(), Tools.getResourse(getContext(), "layout", "item_menu"),this);
        img = (ImageView)findViewById(R.id.item_menu_img);
//        title = (TextView)findViewById(R.id.item_menu_title);

        this.setOnClickListener(onClickListener);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
           if(viewFlag == SHOP_VIEW){
            	iMenu.toShop();
            }else if(viewFlag == CENTER_VIEW){
            	iMenu.toUserCenter();
            }else if(viewFlag ==cp_sdk_game){
            	iMenu.toGame();
            }else if(viewFlag ==cp_sdk_gift){
            	iMenu.toGift();
            }else{
            	iMenu.toExchange();
            }
        }
    };

    /**
     * 设置视图id
     * @param viewId
     */
    public void setViewId(int viewId){
        this.viewId  = viewId;
    }

    /**
     * 显示图片
     * @param src
     */
    public void displayImg(String src){
//        Tools.displayImg(src,img);
    }
    
    /**
     * 显示图片
     * @param drawable
     */
    public void displayImg(int drawable){
    	img.setImageResource(drawable);
    }

    /**
     * 设置标题
     * @param str
     */
    public void setTitle(String str){
//        title.setText(str);
    }

    public void registCallBack(IMenu iMenu){
        this.iMenu = iMenu;
    }


    public void setViewFlag(int viewFlag){
        this.viewFlag = viewFlag;
    }

}
