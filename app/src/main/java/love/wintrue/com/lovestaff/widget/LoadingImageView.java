package love.wintrue.com.lovestaff.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import love.wintrue.com.lovestaff.R;

/**
 * Created by ${尤智君} on 2016/10/19.
 * email 1021690702@qq.com
 */

public class LoadingImageView extends AppCompatImageView implements Runnable{

    private Bitmap bitmap;
    private Paint paint1;
    private Paint paint2;
    public boolean flag = false;
    private int y = 20;
    private int fillColor = Color.RED;
    private int bgColor = Color.WHITE;
    private int picDefaultColor = Color.GRAY;
    private long speed = 54;
    private int resId = R.mipmap.ic_launcher;

    public LoadingImageView(Context context) {
        super(context);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.LoadingAnimatorView);

        // Gets the active circle type, defaulting to "fill"
        resId = a.getResourceId(R.styleable.LoadingAnimatorView_src, R.mipmap.ic_launcher);
        initAnim(context);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    private void initAnim(Context context){
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        paint1 = new Paint();
        paint1.setColor(fillColor);
        paint2 = new Paint();
        paint2.setColor(picDefaultColor);
        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(),resId);
        bitmap = bitmap1.extractAlpha();// 获取一个透明图片
        y = bitmap.getWidth();//初始化y轴坐标
    }

    private void playAnimator() {
        if (y > 0) {
            y-=3;
        }else{
            y = bitmap.getWidth();
        }
    }

    /**
     * 图片动画填充颜色
     * @param color
     */
    public void setFillColor(int color){
        this.fillColor = color;
    }

    /**
     * view整体背景颜色
     * @param color
     */
    public void setBgColor(int color){
        this.bgColor = color;
    }

    /**
     * 图片动画默认颜色
     * @param color
     */
    public void setPicDefaultColor(int color){
        this.picDefaultColor = color;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if(flag){
            canvas.drawBitmap(bitmap, 0, 0,null);
            canvas.drawColor(bgColor);
            canvas.drawBitmap(bitmap, 0, 0, paint2);
            canvas.save();
            //裁剪
            canvas.clipRect(0, y, bitmap.getWidth(),
                    bitmap.getHeight());
            canvas.drawBitmap(bitmap, 0, 0, paint1);
            canvas.restore();
        }else {
            super.onDraw(canvas);
        }


    }

    public void setAnimSpeed(long time){
        this.speed = time;
    }

    //绘制动画线程
    @Override
    public void run() {
        while (flag) {
            postInvalidate();
            playAnimator();
            try {
                Thread.sleep(speed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启动画方法
     */
    public  void startAnim(){
        this.flag = true;
        new Thread(this).start();
    }

    /**
     * 结束动画方法
     */
    public void stopAnim(){
        this.flag = false;
        postInvalidate();
        y = bitmap.getWidth();
    }
}
