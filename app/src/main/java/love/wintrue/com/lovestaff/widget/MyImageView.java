package love.wintrue.com.lovestaff.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * @des:从屏幕中消失时回调，去掉drawable引用，能加快内存的回收
 * @author:th
 * @email:342592622@qq.com
 * @date: 2017/4/25 0025 下午 14:34
 */

public class MyImageView extends AppCompatImageView {

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }
}
