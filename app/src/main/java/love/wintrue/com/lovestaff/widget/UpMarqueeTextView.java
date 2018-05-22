package love.wintrue.com.lovestaff.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * 描述: 跑马灯效果<向上方向>的TextView
 * 作者：th
 * 邮箱：342592622@qq.com
 * 时间: 2015/12/24 17:18
 */
public class UpMarqueeTextView extends AppCompatTextView implements Animator.AnimatorListener {

    private static final String TAG = "UpMarqueeTextView";

    private static final int ANIMATION_DURATION = 200;
    private float height;
    private AnimatorSet mAnimatorStartSet;
    private AnimatorSet mAnimatorEndSet;
    private String mText;
    private static Handler mHandler;
    private String[] mDatas;
    private int mPosition = 0;

    public UpMarqueeTextView(Context context) {
        super(context);
    }

    public UpMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        height = getHeight();// 确保view的高度
    }

    /**--- 初始化向上脱离屏幕的动画效果 ---*/
    private void initStartAnimation() {
        ObjectAnimator translate = ObjectAnimator.ofFloat(this, "translationY", 0, -50);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        mAnimatorStartSet = new AnimatorSet();
        mAnimatorStartSet.play(translate).with(alpha);
        mAnimatorStartSet.setDuration(ANIMATION_DURATION);
        mAnimatorStartSet.addListener(this);
    }

    /**--- 初始化从屏幕下面向上的动画效果 ---*/
    private void initEndAnimation() {
        ObjectAnimator translate = ObjectAnimator.ofFloat(this, "translationY", height, 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        mAnimatorEndSet = new AnimatorSet();
        mAnimatorEndSet.play(translate).with(alpha);
        mAnimatorEndSet.setDuration(ANIMATION_DURATION);
    }


    /**--- 设置内容 ---**/
    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mText = text;
        if (null == mAnimatorStartSet) {
            initStartAnimation();
        }
        mAnimatorStartSet.start();
    }

    @Override
    public boolean isFocused(){
        return true;
    }


    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        super.setText(mText);
        if (null == mAnimatorEndSet) {
            initEndAnimation();
        }
        mAnimatorEndSet.start();
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    public void setDatas(String[] datas){
        mDatas = datas;
    }

    public void startAutoFlowTimer(final long timeSpan){
        if(mHandler!=null) {
            mHandler.removeMessages(0);
        }
        mHandler = null;
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(mDatas != null && mPosition < mDatas.length) {
                    setText(mDatas[mPosition]);
                    mPosition += 1;
                    if (mPosition >= mDatas.length) {
                        mPosition = 0;
                    }
                }
                if(mHandler != null) {
                    Message message = mHandler.obtainMessage(0);
                    sendMessageDelayed(message, timeSpan);
                }
            }
        };
        Message message = mHandler.obtainMessage(0);
        mHandler.sendMessage(message);
    }
    public void stopAutoFlowTimer(){
        if(null!=mAnimatorEndSet){
            mAnimatorEndSet.cancel();
            mAnimatorEndSet = null;
        }
        if(null!=mAnimatorStartSet){
            mAnimatorStartSet.cancel();
            mAnimatorStartSet = null;
        }
        if(mHandler!=null) {
            mHandler.removeMessages(0);
            //mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
    }
    public int getSelectPosition(){
        return mPosition - 1 == -1 ? mDatas.length - 1:mPosition - 1;
    }
}
