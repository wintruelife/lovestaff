package love.wintrue.com.lovestaff.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import love.wintrue.com.lovestaff.R;


/**
 * 数据加载（HTTP请求或读取数据库）过程中视图
 * 
 * @ClassName: DataLoadingLayout.java
 * 
 * @author th
 */
public class DataLoadingLayout extends RelativeLayout {

    /**
     * 数据加载中的显示布局
     */
    private ViewGroup mLayoutDataLoading;

    /**
     * 数据加载失败的显示布局
     */
    private ViewGroup mLayoutDataLoadFailed;

    /**
     * 数据加载失败的错误信息
     */
    private TextView mTxtViewErrorInfo;

    /**
     * 重新加载按钮
     */
    private TextView mTxtViewReload;
    /**
     * 图片显示
     */
    private ImageView mImageView;
    private Context mContext;

    public DataLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public DataLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public DataLoadingLayout(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context) {
        mContext = context;
        View rootLayout = LayoutInflater.from(context).inflate(R.layout.widget_loading, this);
        if (rootLayout != null) {
            mLayoutDataLoading = (ViewGroup) rootLayout.findViewById(R.id.data_loading_layout);
            mLayoutDataLoadFailed =
                    (ViewGroup) rootLayout.findViewById(R.id.data_load_failed_layout);
        }
        if (mLayoutDataLoadFailed != null) {
            mTxtViewErrorInfo = (TextView) mLayoutDataLoadFailed.findViewById(R.id.txt_view_error);
            mTxtViewReload = (TextView) mLayoutDataLoadFailed.findViewById(R.id.txt_view_reload);
            mTxtViewReload.setVisibility(View.GONE);
            mImageView = (ImageView) mLayoutDataLoadFailed.findViewById(R.id.iv_img);
        }
    }

    /**
     * 显示数据加载中的布局
     */
    public void showDataLoading() {
        if(this != null) {
            this.setVisibility(View.VISIBLE);
            mLayoutDataLoading.setVisibility(View.VISIBLE);
            mLayoutDataLoadFailed.setVisibility(View.GONE);
        }
    }

    /**
     * 数据加载成功 - 隐藏布局
     */
    public void showDataLoadSuccess() {
        if(this != null) {
            this.setVisibility(View.GONE);
        }
    }
    
    /**
     * 显示请求成功但请求返回数据为空的情况
     * 提示：暂时还没有相关内容，看看别的？
     */
    public void showDataEmptyView() {
        if(this != null) {
            this.setVisibility(View.VISIBLE);
            mLayoutDataLoadFailed.setVisibility(View.VISIBLE);
            mTxtViewErrorInfo.setText(R.string.txt_layout_empty);
            mTxtViewErrorInfo.setVisibility(View.VISIBLE);
            mTxtViewReload.setVisibility(View.GONE);
        }
    }
    
    /**
     * 显示请求成功但请求返回数据为空的情况
     * 提示：暂时还没有相关内容，看看别的？
     */
    public void showDataEmptyView(int Stringid) {
        if(this != null) {
            this.setVisibility(View.VISIBLE);
            mLayoutDataLoadFailed.setVisibility(View.VISIBLE);
            mTxtViewErrorInfo.setText(Stringid);
            mTxtViewErrorInfo.setVisibility(View.VISIBLE);
            mTxtViewReload.setVisibility(View.GONE);
        }
    }
    
    /**
     * 显示请求成功但请求返回数据为空的情况
     * @param strReason 提示：strReason
     */
	public void showDataLoadEmpty(String strReason) {
        if(this != null) {
            this.setVisibility(View.VISIBLE);
            mLayoutDataLoadFailed.setVisibility(View.VISIBLE);
            mTxtViewErrorInfo.setText(strReason);
            mTxtViewErrorInfo.setVisibility(View.VISIBLE);
            mTxtViewReload.setVisibility(View.GONE);
            mImageView.setImageResource(R.drawable.spenddetail_nodata_icon);
        }
	}
	
	/**
	 * 显示数据加载失败原因
	 */
	public void showDataLoadFailed(String strReason) {
        if(this != null) {
            this.setVisibility(View.VISIBLE);
            mLayoutDataLoadFailed.setVisibility(View.VISIBLE);
            mTxtViewErrorInfo.setText(strReason);
            mTxtViewErrorInfo.setVisibility(View.VISIBLE);
            mImageView.setImageResource(R.drawable.img_jzsb);
            //mTxtViewReload.setVisibility(View.GONE);
        }
	}

    /**
     * 设置点击重新加载事件
     * 
     * @param onClickListener
     */
    public void setOnReloadClickListener(OnClickListener onClickListener) {
        mTxtViewReload.setVisibility(View.VISIBLE);
        mTxtViewReload.setOnClickListener(onClickListener);
    }
}
