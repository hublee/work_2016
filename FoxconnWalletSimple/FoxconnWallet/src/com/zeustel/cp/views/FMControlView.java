package com.zeustel.cp.views;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zeustel.foxconn.cp_sdk.R;

import java.util.ArrayList;
import java.util.List;


/**
 * FM控制器
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/17 10:53
 */
public class FMControlView extends FrameLayout implements GestureDetector.OnGestureListener{
    private LayoutInflater inflater;
    private View contentView = null;
    //头像
    private ImageView ic_fm_icon;
    /*淡出动画*/
    private AlphaAnimation alphaAnimation = null;
    /*抖动动画*/
    private Animation shakeAnimation = null;
    /*收听按键是否隐藏*/
    private boolean isHide;

    //关注按钮
    private boolean isAttention = false;//关注标记


    private GestureDetector gestureDetector;

    public void setOnFmControlListener(OnFmControlListener onFmControlListener) {
        this.onFmControlListener = onFmControlListener;
    }

    private OnFmControlListener onFmControlListener;

    private final int ERROR_MSG  = 111;

    private final int NET_INFO = 123;

    /*回调*/
    public interface OnFmControlListener {
    	/**
    	 * 单击
    	 */
    	void onClick();
    	
    	/**
    	 * 双击
    	 */
    	void onDoubleClick();
    	
    	/**
    	 * 长按
    	 */
    	void onLongPress();
    	
    	/**
    	 * 外部点击
    	 */
    	void onOutSide();
    }
    
    enum Function {
        SINGLECLICK,
        DOUBLECLICK,
        LONGPRESS
    }

    private GestureDetector.OnDoubleTapListener listener = new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            if (onFmControlListener != null) {
//                onFmControlListener.onListenClick();
            	onFmControlListener.onDoubleClick();
            }
//            Tools.logI("listen");

            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    };


    /*收听按键点击事件*//////////////////////////////////////////////////////////////////////////
    private OnClickListener listen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onFmControlListener != null) {
//                onFmControlListener.onListenClick();
            }
//            Tools.logI("listen");
        }
    };
    /*控制器长按事件*/
    private OnLongClickListener longClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
//            Tools.logI("longClick");
            /*拖动 与长按冲突问题*/
           /* if (Function.UNFOLD == (Function) ic_fm_unfold.getTag()) {
                contentView.startAnimation(shakeAnimation);
                functionSwitch();
                return true;
            }*/
            return false;
        }
    };



    /*收听按键淡出动画监听*/
    private Animation.AnimationListener listenAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
        /*动画完成后 隐藏按键*/

            if (animation != null) {
                animation.cancel();
                animation = null;
            }
            isHide = true;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    private Context context = null;
    private static FMControlView mFMControlView = null;

    /**
     * 懒汉式 单例模式
     * 特点 实例的延迟加载
     * 由于 多线程并发访问会出现安全问题 要加同步锁
     * 同步代码块和 同步关键字都行 <低效>
     * 用双层判断 可以解决 低效 问题
     */
    public static FMControlView getInstance(Context context) {
        if (mFMControlView == null) {
            synchronized (FMControlView.class) {
                if (mFMControlView == null)
                    mFMControlView = new FMControlView(context);
            }
        }
        return mFMControlView;
    }

    private FMControlView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.alpha_anim_out);
        alphaAnimation.setAnimationListener(listenAnimListener);
        shakeAnimation = shakeAnimation(2);

        gestureDetector = new GestureDetector(getContext(),this);

        init();
    }

    private void init() {
        contentView = inflater.inflate(R.layout.fm_control, null);
        //头像
        ic_fm_icon = (ImageView) contentView.findViewById(R.id.ic_fm_icon);

        addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setClickable(true);
        
        LinearLayout layout = (LinearLayout)contentView.findViewById(R.id.fm_layout);
    }

    /*收听按键隐藏*/
    public void hideListen() {
        if (!isHide) {
//            if (ic_fm_listen != null && alphaAnimation != null) {//////////////////////////////////////////////////////////////////
//                ic_fm_listen.startAnimation(alphaAnimation);
//            }

        }
    }

    /*销毁动画*/
    public void destroyAnim() {
        if (alphaAnimation != null) {
            alphaAnimation.cancel();
            alphaAnimation = null;
        }
        if (shakeAnimation != null) {
            shakeAnimation.cancel();
            shakeAnimation = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	int x = (int) event.getX();
        int y = (int) event.getY();
        Rect rect = new Rect();
        this.getGlobalVisibleRect(rect);
        if (!rect.contains(x, y)) {
        	onFmControlListener.onOutSide();
        }
    	
        return gestureDetector.onTouchEvent(event);
    }

    private Handler handle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
//                case ClickEvent.SINGLECLICK:
//                    singleClick();
//                    break;
//                case ClickEvent.DOUBLECLICK:
//                    doubleClick();
//                    break;
//                case ClickEvent.LONGCLICK:
//                    longClick();
//                    break;
                case ERROR_MSG:
                    Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
                case NET_INFO:
                    Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 单击
     */
    private void singleClick(){
//        onFmControlListener.onUnfoldClick();
//        onUnfoldClick();
    	
    	onFmControlListener.onClick();
    }

    /**
     * 双击
     */
    private void doubleClick(){
        if (onFmControlListener != null) {
//            onFmControlListener.onListenClick();
        	onFmControlListener.onDoubleClick();
        }
    }

    /**
     * 长按
     */
    private void longClick(){

    }


    private static boolean isUnfold = false;

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		onFmControlListener.onLongPress();
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		
		startAnim();
		return false;
	}

	//2016年3月18日14:51:52
	private void startAnim(){
		List<Animator> animators = new ArrayList<Animator>();
		AnimatorSet set = new AnimatorSet();  
        //set.addAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.rotate_anim));
        set.setDuration(500);
        //ic_fm_icon.startAnimation(set);
        //缩小
      	animators.add(ObjectAnimator.ofFloat(ic_fm_icon, "scaleX", 1, 0.6F,1));
      	animators.add(ObjectAnimator.ofFloat(ic_fm_icon, "scaleY", 1, 0.6F,1));
      	//翻转
      	//animators.add(ObjectAnimator.ofFloat(ic_fm_icon, "rotationY", 0F, 180F));
		set.playTogether(animators);
		set.setDuration(450);
		set.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				onFmControlListener.onClick();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		set.start();
	}
    
	

}
