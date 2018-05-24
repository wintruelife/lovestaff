package love.wintrue.com.lovestaff.widget.actionbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.utils.DensityUtil;
import love.wintrue.com.lovestaff.widget.DataLoadingLayout;


public class CommonActionBar extends RelativeLayout {

    /**
     * 左边图标按钮
     */
    private ImageButton mImgBtnLeft;

    /**
     * 左边文字按钮
     */
    private Button mBtnLeft;

    /**
     * 中间文字标题
     */
    private TextView mTvTitle;

    /**
     * 中间文字标题右下角
     */
    private TextView mTvTitleRight;

    /**
     * 右边图标按钮
     */
    private ImageView mImgBtnRight;

    /**
     * 右边文字按钮
     */
    private Button mTxtBtnRight;

    private View mView;
    private View actionBarSeparationLine;
    private View virtualStatusBar;

    private RelativeLayout mRlContainer;
    private DataLoadingLayout mDataLoadingLayout;
    private Context mContext;

    public CommonActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
        mContext = context;
    }

    public CommonActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
        mContext = context;
    }

    public CommonActionBar(Context context) {
        super(context);
        initViews(context);
        mContext = context;
    }

    private void initViews(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.widget_common_action_bar, this);
        if (mView != null) {
            virtualStatusBar = mView.findViewById(R.id.virtual_statusBar);
            actionBarSeparationLine=mView.findViewById(R.id.action_bar_line);
            mBtnLeft = (Button) mView.findViewById(R.id.btn_left);
            mImgBtnLeft = (ImageButton) mView.findViewById(R.id.img_btn_left);
            mTvTitle = (TextView) mView.findViewById(R.id.tv_title);
            mTvTitleRight = (TextView) mView.findViewById(R.id.tv_title_right);
            mTxtBtnRight = (Button) mView.findViewById(R.id.btn_right);
            mImgBtnRight = (ImageView) mView.findViewById(R.id.img_btn_right);
            mRlContainer = (RelativeLayout) mView.findViewById(R.id.rl_container);
            mDataLoadingLayout = (DataLoadingLayout) mView.findViewById(R.id.data_loading);
        }
    }

    public void setActionBarSeparationLineVisiable(int visibility) {
        actionBarSeparationLine.setVisibility(visibility);
    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    public void setBackground(int color) {
        mRlContainer.setBackgroundColor(color);
    }

    /**
     * 设置背景颜色
     *
     * @param colorDrawable
     */
    public void setBackground(Drawable colorDrawable) {
        ViewGroup.LayoutParams lp;
        lp = virtualStatusBar.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = DensityUtil.getStatusHeight(mContext);
        virtualStatusBar.setLayoutParams(lp);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mRlContainer.setBackgroundDrawable(colorDrawable);
        } else {
            mRlContainer.setBackground(colorDrawable);
        }
    }


    /**
     * 设置左侧颜色
     *
     * @param color
     */
    public void setmImgBtnLeft(int color) {
        mBtnLeft.setTextColor(color);
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setTitleColor(int color) {
        mTvTitle.setTextColor(color);
        mBtnLeft.setTextColor(color);
    }

    /**
     * 设置ActionBar标题
     *
     * @param txtResId
     */
    public void setActionBarTitle(int txtResId) {
        mTvTitle.setText(txtResId);
    }

    /**
     * 设置ActionBar标题右边
     *
     * @param strTitleRight
     */
    public void setActionBarTitleRight(String strTitleRight) {
        mTvTitleRight.setText(strTitleRight);
        if (strTitleRight == null) {
            mTvTitleRight.setVisibility(GONE);
        } else {
            mTvTitleRight.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置ActionBar标题
     *
     * @param strTitle
     */
    public void setActionBarTitle(String strTitle) {
        if (!TextUtils.isEmpty(strTitle) && strTitle.length() > 10) {
            strTitle = strTitle.substring(0, 10) + "...";
        }
        mTvTitle.setText(strTitle);
    }


    /**
     * 设置左边文字按钮及事件
     *
     * @param txtResId        资源ID
     * @param onClickListener 事件
     */
    public void setLeftBtn(int txtResId, OnClickListener onClickListener) {
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setText(txtResId);
        mBtnLeft.setOnClickListener(onClickListener);
        // 隐藏左边图标按钮
        mImgBtnLeft.setVisibility(View.GONE);
    }

    public void setLeftBtnPaddingLeft(int dp) {
        mBtnLeft.setPadding(DensityUtil.dip2px(mContext, dp), 0, 0, 0);
    }

    public void setLeftBtnBack(int txtResId, OnClickListener onClickListener) {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        drawable.setBounds(0, 0, DensityUtil.dip2px(mContext, 25), DensityUtil.dip2px(mContext, 25));
        mBtnLeft.setPadding(DensityUtil.dip2px(mContext, 7), 0, 0, 0);
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setText(txtResId);
        mBtnLeft.setCompoundDrawables(drawable, null, null, null);
        mBtnLeft.setOnClickListener(onClickListener);
        // 隐藏左边图标按钮
        mImgBtnLeft.setVisibility(View.GONE);
    }

    public void setLeftBtnImg(int imgResId, OnClickListener onClickListener) {
        Drawable drawable = getResources().getDrawable(imgResId);
        drawable.setBounds(0, 0, DensityUtil.dip2px(mContext, 27), DensityUtil.dip2px(mContext, 27));
        mBtnLeft.setPadding(DensityUtil.dip2px(mContext, 12), 0, DensityUtil.dip2px(mContext, 12), 0);
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setCompoundDrawables(drawable, null, null, null);
        mBtnLeft.setOnClickListener(onClickListener);
        // 隐藏左边图标按钮
        mImgBtnLeft.setVisibility(View.GONE);
    }

    public void setLeftBtnBack(int txtResId, int backResId, OnClickListener onClickListener) {
        Drawable drawable = getResources().getDrawable(backResId);
        drawable.setBounds(0, 0, DensityUtil.dip2px(mContext, 25), DensityUtil.dip2px(mContext, 25));
        mBtnLeft.setPadding(DensityUtil.dip2px(mContext, 7), 0, 0, 0);
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setText(txtResId);
        mBtnLeft.setCompoundDrawables(drawable, null, null, null);
        mBtnLeft.setOnClickListener(onClickListener);
        // 隐藏左边图标按钮
        mImgBtnLeft.setVisibility(View.GONE);
    }

    public void setLeftBtnBackBg(int color) {
        mBtnLeft.setBackgroundColor(color);
    }


    public void setLeftBtnBack(int txtResId, Drawable drawable, int color, OnClickListener onClickListener) {
        if (drawable == null) {
            drawable = getResources().getDrawable(R.drawable.ic_back);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mBtnLeft.setBackgroundColor(color);
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setText(txtResId);
        mBtnLeft.setCompoundDrawables(drawable, null, null, null);
        mBtnLeft.setOnClickListener(onClickListener);
        // 隐藏左边图标按钮
        mImgBtnLeft.setVisibility(View.GONE);
    }

    /**
     * 设置左边文字按钮及事件
     *
     * @param imgResId        资源ID
     * @param onClickListener 事件
     */
    public void setLeftBtn(int imgResId, int txtResId, OnClickListener onClickListener) {
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setCompoundDrawablesWithIntrinsicBounds(imgResId, 0, 0, 0);
        mBtnLeft.setText(txtResId);
        mBtnLeft.setOnClickListener(onClickListener);
        // 隐藏左边图标按钮
        mImgBtnLeft.setVisibility(View.GONE);
    }

    /**
     * 设置左边文字按钮及事件
     *
     * @param imgResId        资源ID
     * @param onClickListener 事件
     */
    public void setLeftBtn(int imgResId, String txt, OnClickListener onClickListener) {
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setCompoundDrawablesWithIntrinsicBounds(imgResId, 0, 0, 0);
        mBtnLeft.setCompoundDrawablePadding(8);
        mBtnLeft.setText(txt);
        mBtnLeft.setOnClickListener(onClickListener);
        // 隐藏左边图标按钮
        mImgBtnLeft.setVisibility(View.GONE);
    }

    /**
     * 设置左边图标按钮及事件
     *
     * @param imageResId      资源ID
     * @param onClickListener 事件
     */
    public void setLeftImgBtn(int imageResId, OnClickListener onClickListener) {
        mImgBtnLeft.setVisibility(View.VISIBLE);
        mImgBtnLeft.setImageResource(imageResId);
        mImgBtnLeft.setOnClickListener(onClickListener);
        mImgBtnLeft.setPadding(40, 0, 40, 0);
        // 隐藏左边文字按钮
        mBtnLeft.setVisibility(View.GONE);
    }

    public void setLeftImgBtnWithBg(int imageResId, OnClickListener onClickListener) {
        mImgBtnLeft.setVisibility(View.VISIBLE);
        mImgBtnLeft.setBackgroundResource(imageResId);
        mImgBtnLeft.setOnClickListener(onClickListener);
        mImgBtnLeft.setPadding(40, 0, 40, 0);
        // 隐藏左边文字按钮
        mBtnLeft.setVisibility(View.GONE);
    }

    /**
     * 设置左边图片按钮及事件
     *
     * @param imageResId      资源ID
     * @param selectorId      点击效果ID
     * @param onClickListener 点击事件
     */

    public void setLeftImgBtn(int imageResId, int selectorId, OnClickListener onClickListener) {
        mImgBtnLeft.setVisibility(View.VISIBLE);
        mImgBtnLeft.setBackgroundResource(selectorId);
        mImgBtnLeft.setImageResource(imageResId);
        mImgBtnLeft.setOnClickListener(onClickListener);
        // 隐藏左边文字按钮
        mBtnLeft.setVisibility(View.GONE);
    }

    /**
     * 在默认情况下设置左侧图片按钮事件
     *
     * @param onClickListener
     */
    public void setLeftImgBtn(OnClickListener onClickListener) {
        mImgBtnLeft.setVisibility(View.VISIBLE);
        mImgBtnLeft.setOnClickListener(onClickListener);
        mBtnLeft.setVisibility(View.GONE);
    }

    public void setVisibleReturn(boolean isShow) {
        mBtnLeft.setVisibility(isShow == true ? View.VISIBLE : View.GONE);

    }

    /**
     * 设置右边图标按钮及事件
     *
     * @param imageResId      资源ID
     * @param onClickListener 事件
     */
    public void setRightImgBtn(int imageResId, int color, OnClickListener onClickListener) {
        if (imageResId <= 0) {
            mImgBtnRight.setVisibility(View.GONE);
        } else {
            mImgBtnRight.setVisibility(View.VISIBLE);
        }

        mImgBtnRight.setBackgroundColor(color);

        mImgBtnRight.setImageResource(imageResId);
        mImgBtnRight.setOnClickListener(onClickListener);
        // 隐藏右边文字按钮
        mTxtBtnRight.setVisibility(View.GONE);
    }

    /**
     * 设置右边图标按钮及事件
     *
     * @param imageResId      资源ID
     * @param onClickListener 事件
     */
    public void setRightImgBtn(int imageResId,OnClickListener onClickListener) {
        if (imageResId <= 0) {
            mImgBtnRight.setVisibility(View.GONE);
        } else {
            mImgBtnRight.setVisibility(View.VISIBLE);
        }

        mImgBtnRight.setBackgroundResource(imageResId);
        mImgBtnRight.setOnClickListener(onClickListener);
        // 隐藏右边文字按钮
        mTxtBtnRight.setVisibility(View.GONE);
    }


    /**
     * 设置右边图标按钮是否显示
     *
     * @param visibility
     */
    public void setRightImgBtnVisibility(int visibility) {
        mImgBtnRight.setVisibility(visibility);
    }

    /**
     * 设置右边文字按钮及事件
     *
     * @param txtResId        资源ID
     * @param onClickListener 事件
     */
    public void setRightTxtBtn(int txtResId, OnClickListener onClickListener) {
        mTxtBtnRight.setVisibility(View.VISIBLE);
        mTxtBtnRight.setText(txtResId);
        mTxtBtnRight.setOnClickListener(onClickListener);
        // 隐藏右边图标按钮
        mImgBtnRight.setVisibility(View.GONE);
    }

    /**
     * 设置右边文字按钮及事件
     *
     * @param txtResId        资源ID
     * @param onClickListener 事件
     */
    public void setRightTxtBtn(int txtResId, int color, OnClickListener onClickListener) {
        mTxtBtnRight.setVisibility(View.VISIBLE);
        mTxtBtnRight.setText(txtResId);
        mTxtBtnRight.setTextColor(color);
        mTxtBtnRight.setOnClickListener(onClickListener);
        // 隐藏右边图标按钮
        mImgBtnRight.setVisibility(View.GONE);
    }


    public void setRightTextSize(int textSize) {
        mTxtBtnRight.setTextSize(textSize);
    }

    /**
     * 设置右边文字按钮及事件
     *
     * @param txtResId 资源ID
     */
    public void setRightTxtBtn(int txtResId) {
        mTxtBtnRight.setVisibility(View.VISIBLE);
        mTxtBtnRight.setText(txtResId);
        // 隐藏右边图标按钮
        mImgBtnRight.setVisibility(View.GONE);
    }

    public void setRightTxtBtn(String txtResId, int color) {
        mTxtBtnRight.setVisibility(View.VISIBLE);
        mTxtBtnRight.setText(txtResId);
        mTxtBtnRight.setTextColor(color);
        // 隐藏右边图标按钮
        mImgBtnRight.setVisibility(View.GONE);
    }

    /**
     * 设置右边文字按钮是否显示
     *
     * @param visibility
     */
    public void setRightTxtBtnVisibility(int visibility) {
        mTxtBtnRight.setVisibility(visibility);
    }

    /**
     * 获取页面数据加载时显示的加载控件
     *
     * @return
     */
    public DataLoadingLayout getDataLoadingLayout() {
        return mDataLoadingLayout;
    }

    public void hideLeftImg() {
        mImgBtnLeft.setVisibility(INVISIBLE);
    }
}
