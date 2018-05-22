package love.wintrue.com.lovestaff.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import love.wintrue.com.lovestaff.R;

/**
 * @author th
 * @desc 可控制drawableTop大小的RadioButton
 * @time 2016/12/19 17:01
 */
public class MyRadioButton extends AppCompatRadioButton {
    //图片大小
    private int drawableSize;

    public MyRadioButton(Context context) {
        this(context,null);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyRadioButton);
        drawableSize = a.getDimensionPixelSize(R.styleable.MyRadioButton_rbDrawableTopSize, 50);
        Drawable drawableTop = a.getDrawable(R.styleable.MyRadioButton_rbDrawableTop);

        //释放资源
        a.recycle();

        setCompoundDrawablesWithIntrinsicBounds(null,drawableTop,null,null);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        if(top != null){
            //这里只要改后面两个参数就好了，一个宽一个是高，如果想知道为什么可以查找源码
            top.setBounds(0,0,drawableSize,drawableSize);
        }
        setCompoundDrawables(left,top,right,bottom);
    }
}