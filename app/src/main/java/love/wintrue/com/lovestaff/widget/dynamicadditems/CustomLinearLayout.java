package love.wintrue.com.lovestaff.widget.dynamicadditems;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lhe on 2017/8/30.
 */

public class CustomLinearLayout extends LinearLayout {

    private CustomAdapter adapter;
    private Context context;
    private int width = 0;
    private LinearLayout itemLayout;
    private LinearLayout[] linearLayouts;

    public CustomLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context =  context;
        setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(width<=0 || getMeasuredWidth() != width){
            width = getMeasuredWidth();
            updateView();
        }
    }

    public void setAdapter(CustomAdapter adapter){
        this.adapter = adapter;
        if(width > 0){
            updateView();
        }
    }

    private int getItemWidth(View item){
        if(item == null || !(item instanceof TextView)){
            return 0;
        }
        Paint paint = new Paint();
        paint.setTextSize(((TextView)item).getTextSize());
        return (int) paint.measureText(((TextView)item).getText().toString())+item.getPaddingLeft()+item.getPaddingRight();
    }

    private void clearAllView(){
        removeAllViews();
        itemLayout = null;
        if(linearLayouts == null){
            if(adapter==null || adapter.getCount()<=0){
                return;
            }
            linearLayouts = new LinearLayout[adapter.getCount()];
            return;
        }
        for(int i=0;i<linearLayouts.length;i++){
            if(linearLayouts[i] != null){
                linearLayouts[i].removeAllViews();
            }
        }
        if(adapter==null || adapter.getCount()<=0){
            return;
        }
        LinearLayout[] tempLinearLayouts = new LinearLayout[adapter.getCount()];
        for(int i=0;i<tempLinearLayouts.length;i++){
            if(linearLayouts.length <= i){
                break;
            }
            tempLinearLayouts[i] = linearLayouts[i];
        }
        linearLayouts = tempLinearLayouts;
    }

    private boolean isUpdate = false;

    private void updateView() {
        if (isUpdate || adapter == null || adapter.getCount() <= 0) {
            return;
        }
        clearAllView();
        isUpdate = true;
        TextView textView;
        int trueWidth = width - getPaddingLeft() - getPaddingLeft();
        int itemWidth;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); // 每行的水平LinearLayout
        LayoutParams itemParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int margin = dip2px(context, 5);
        itemParams.setMargins(margin, margin, margin, margin);
        for (int i = 0; i < adapter.getCount(); i++) {
            textView = adapter.getTextView(i);
            itemWidth = getItemWidth(textView);
            if (itemWidth <= 0) {
                continue;
            }
            if (itemLayout == null) {//没有item
                if(linearLayouts[i] == null){
                    itemLayout = new LinearLayout(context);
                    linearLayouts[i] = itemLayout;
                }else{
                    itemLayout = linearLayouts[i];
                }
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setLayoutParams(layoutParams);
                textView.setLayoutParams(itemParams);
                itemLayout.addView(textView);
                addView(itemLayout);
            } else {
                int countItemWidth = 0;
                for (int j = 0; j < itemLayout.getChildCount(); j++) {
                    countItemWidth += getItemWidth(itemLayout.getChildAt(j)) + dip2px(context, 10);
                }
                if (itemWidth < trueWidth - countItemWidth - dip2px(context, 10)) {
                    textView.setLayoutParams(itemParams);
                    itemLayout.addView(textView);
                } else {
                    itemLayout = null;
                    i--;
                    continue;
                }
            }
        }
        isUpdate = false;
    }

    public static abstract class CustomAdapter {

        private CustomLinearLayout linearLayout;

        public CustomAdapter(CustomLinearLayout linearLayout){
            this.linearLayout = linearLayout;
        }

        public abstract int getCount();

        public abstract Object getItem(int position);

        public abstract TextView getTextView(int position);

        public void notifyByDataChanged(){
            if(linearLayout != null){
                linearLayout.updateView();
            }
        }
    }

    //工具方法
    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     */
    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     */
    private int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    private int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    private int getScreenWidth(Activity activity) {
        if (activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.widthPixels;
        }
        return 0;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    private int getScreenHeight(Activity activity) {
        if (activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.heightPixels;
        }
        return 0;
    }
}
