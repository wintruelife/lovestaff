/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package love.wintrue.com.lovestaff.widget.xlistview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import love.wintrue.com.lovestaff.R;

public class XListViewFooter extends LinearLayout {

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;

	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;
	private TextView mTvNone;

	private String ReadyText;
	private String LoadingText;
	private String NolmalText;
	private boolean showNoData = false;

	public void setNolmalText(String nolmalText) {
		NolmalText = nolmalText;
	}

	public void setLoadingText(String loadingText) {
		LoadingText = loadingText;
	}

	public void setReadyText(String readyText) {
		ReadyText = readyText;
	}

	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}


	public void setState(int state) {
		if (state == STATE_READY) {
			mProgressBar.setVisibility(View.INVISIBLE);
			if(!TextUtils.isEmpty(ReadyText)){
				mHintView.setText(ReadyText);
			}else {
				mHintView.setText(R.string.xlistview_footer_hint_ready);
			}

		} else if (state == STATE_LOADING) {

			if(!TextUtils.isEmpty(LoadingText)){
				mHintView.setText(LoadingText);
			}else {
				mHintView.setText(R.string.xlistview_footer_hint_loading);
			}
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mProgressBar.setVisibility(View.INVISIBLE);

			if(!TextUtils.isEmpty(NolmalText)){
				mHintView.setText(NolmalText);
			}else {
				mHintView.setText(R.string.xlistview_footer_hint_normal);
			}
		}
	}

	public void setHintText(int strId){
		mHintView.setText(strId);
		showNoData = true;
	}

	public void setText(){
		showNoData = false;
	}

	public boolean getShowNoData(){
		return showNoData;
	}

	public void setBottomMargin(int height) {
		if (height < 0) return ;
		LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setText(R.string.xlistview_footer_hint_normal);
		mProgressBar.setVisibility(View.GONE);
		mTvNone.setVisibility(View.GONE);
	}

	public void none() {
		mTvNone.setText("抱歉，没有更多啦");
		mTvNone.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setText(R.string.xlistview_footer_hint_loading);
		mProgressBar.setVisibility(View.VISIBLE);
		mTvNone.setVisibility(View.GONE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
		lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
		mTvNone.setVisibility(View.GONE);
	}

	/**
	 * show footer
	 */
	public void show(int resid) {
		LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
		lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
		mTvNone.setVisibility(View.GONE);
		mHintView.setText(resid);
	}
	/**
	 * show footer
	 */
	public void showNoData() {
		LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
		lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
		mTvNone.setVisibility(View.GONE);
		mHintView.setText(R.string.xlistview_footer_hint_nodata);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.widget_xlistview_footer, null);
		if(moreView != null){
			addView(moreView);
			moreView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			mContentView = moreView.findViewById(R.id.xlistview_footer_content);
			mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
			mHintView = (TextView)moreView.findViewById(R.id.xlistview_footer_hint_textview);
			mTvNone = (TextView)moreView.findViewById(R.id.m_tv_none);
		}
	}

}
