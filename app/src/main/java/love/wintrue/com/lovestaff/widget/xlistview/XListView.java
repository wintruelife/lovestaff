/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package love.wintrue.com.lovestaff.widget.xlistview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.adapter.FFBaseAdapter;
import love.wintrue.com.lovestaff.bean.BaseBean;
import love.wintrue.com.lovestaff.config.AppConstants;
import love.wintrue.com.lovestaff.utils.DateUtil;
import love.wintrue.com.lovestaff.widget.MyRadioButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @ClassName: XListView
 * @Description: 上拉下拉listview控件
 * @author th
 */
public class XListView extends ListView implements OnScrollListener {

    private static final String TAG = "XListView";

    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400; // scroll back duration
    private int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
    // at bottom, trigger
    // load more.
    /** 实际的padding的距离与界面上偏移距离的比例 */
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    // feature.
    private Context mContext;

    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;

    // -- header view
    private XListViewHeader mHeaderView;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;
    private int mHeaderViewHeight; // header view's height
    // -- footer view
    public XListViewFooter mFooterView;
    // -- xlist fun
    private boolean mEnablePullRefresh = true;
    /** 当该ListView所在的控件显示到屏幕上时，是否直接显示正在刷新...   */
    private boolean mEnableAutoRefresh = false;
    private boolean mPullRefreshing = false; // is refreashing.
    /** 是否已经执行刷新*/
    private boolean mAutoRefreshed = false;

    private boolean mEnablePullLoad;
    private boolean mEnableAutoLoadMore = false;
    private boolean mPullLoading;

    private boolean mIsFooterReady = false;
    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;
    private boolean canHeaderZero = false;
    private boolean isFirstPullLoad = false;
    private int page = 0;
    private int pageSize = AppConstants.PAGE_NUM;
    private LinearLayout menuView;
    MyRadioButton mBtnScan;
    MyRadioButton mBtnPayment;
    MyRadioButton mBtnIncome;
    MyRadioButton mBtnConsum;
    LinearLayout mLlMarquee;
    ImageView closeMarquee;

    public void setCanHeaderZero(boolean canHeaderZero) {
        this.canHeaderZero = canHeaderZero;
    }

    /**
     * @param context
     */
    public XListView(Context context) {
        super(context);
        initWithContext(context,null);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context,attrs);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context,attrs);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initWithContext(Context context,AttributeSet attrs) {
        mContext = context;
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HomeMenu);
        boolean hasHome = a.getBoolean(R.styleable.HomeMenu_hasHomeMenu,false);
        a.recycle();

        // init header view
        mHeaderView = new XListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
        mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.xlistview_header_time);
        addHeaderView(mHeaderView, null, true);
        // init footer view
        mFooterView = new XListViewFooter(context);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderViewContent.getHeight();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }else{
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        if (mEnableAutoRefresh && !mAutoRefreshed) {
                            mAutoRefreshed = true;
                            onRefresh();
                        }
                    }
                });
        this.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
    }

//
//    public void setNodataString(int strId){
//        mFooterView.setHintText(strId);
//    }
//
//    public void setdataString(){
//        mFooterView.setText();
//    }


//    public void setHeaderBg(int colorResId){
//        mHeaderView.setHeaderBg(colorResId);
//    }

    public void setOnScanClickListener(OnClickListener listener){
        if(null!=mBtnScan){
            mBtnScan.setOnClickListener(listener);
        }
    }
    public void setOnPaymentClickListener(OnClickListener listener){
        if(null!=mBtnPayment){
            mBtnPayment.setOnClickListener(listener);
        }
    }
    public void setOnIncomeClickListener(OnClickListener listener){
        if(null!=mBtnIncome){
            mBtnIncome.setOnClickListener(listener);
        }
    }
    public void setOnConsumClickListener(OnClickListener listener){
        if(null!=mBtnConsum){
            mBtnConsum.setOnClickListener(listener);
        }
    }



    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure XListViewFooter is the last footer view, and only add once.
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView,null,true);
        }
        setHeaderDividersEnabled(false);
        setFooterDividersEnabled(false);
        super.setAdapter(adapter);
    }


    public void showNoData(FFBaseAdapter<?> adapter) {
        adapter.setList(null);
        adapter.notifyDataSetChanged();
    }

    public void modifyData(FFBaseAdapter<?> adapter, List<? extends BaseBean> list, int pageIndex) {
        if (pageIndex == AppConstants.PARAM_START_NUM) {
            onRefreshComplete();
            // 第一页
            if (!isBlank(list)) {
                // 修改listview
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                if (list.size() < pageSize) {
                    setPullLoadEnable(false);
                    mFooterView.none();
                } else {
                    setPullLoadEnable(true);
                }
            } else {
                // 显示无数据emptyview
                showNoData(adapter);
                setPullLoadEnable(false);
                mFooterView.none();
            }
        } else {
            // 分页
            onLoadMoreComplete();
            if (isBlank(list)) {
                setPullLoadEnable(false);
                return;
            }

            // 修改listview
            adapter.addList(list);
            adapter.notifyDataSetChanged();

            if (list.size() < pageSize) {
                setPullLoadEnable(false);
                mFooterView.none();
            } else {
                setPullLoadEnable(true);
            }
        }
    }

    protected static boolean isBlank(List<? extends BaseBean> items) {
        return items == null || items.size() == 0;
    }

    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            if(!mFooterView.getShowNoData()){
                mFooterView.hide();
            }else {
                mFooterView.show(R.string.xlistview_footer_hint_normal);
            }
            mFooterView.setOnClickListener(null);
            if(isFirstPullLoad && page > 1){
                mFooterView.none();
            }
        } else {
            isFirstPullLoad = true;
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mPullRefreshing && !mPullLoading) {
                        startLoadMore();
                    }
                }
            });
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnableNoMachant(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            if(!mFooterView.getShowNoData()){
                mFooterView.hide();
            }else {
//                mFooterView.show(R.string.txt_no_machant);
            }
            mFooterView.setOnClickListener(null);
            if(isFirstPullLoad && page > 1){
                mFooterView.none();
            }
        } else {
            isFirstPullLoad = true;
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mPullRefreshing && !mPullLoading) {
                        startLoadMore();
                    }
                }
            });
        }
    }

    /** 布局完成后直接显示刷新*/
    public void setAutoRefreshEnable(boolean enable) {
        mEnableAutoRefresh = enable;
    }

    /** 滑动到底部自动加载更多*/
    public void setAutoLoadMoreEnable(boolean enable){
        mEnableAutoLoadMore = enable;
    }

    /**
     * 显示刷新效果
     */
    public void onRefresh(){
        updateHeaderHeight(mHeaderViewHeight);
        startRefresh();
    }

    /**
     * 显示加载更多
     */
    public void onLoad(){
        startLoadMore();
    }

    /**
     * stop refresh, reset header view.
     */
    public void onRefreshComplete() {
        if (mPullRefreshing == true) {
            SharedPreferences preferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE );
            Editor editor=preferences.edit();
            editor.putString(mContext.getClass().getName()+".lasttime", getStringDateLong());
            editor.commit();
            setRefreshTime("刚刚刷新");
            mPullRefreshing = false;

            mHeaderView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetHeaderHeight();
                    if(mScroller.isFinished()){
                        mHeaderView.setVisiableHeight(0);
                    }
                }
            },1000);
        }else {
            mHeaderView.setVisiableHeight(0);
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void onLoadMoreComplete() {
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
        }
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        int visiableHeight = (int) delta + mHeaderView.getVisiableHeight();
//        if(visiableHeight<0){
//            mHeaderView.setVisiableHeight(0);
//        }
        mHeaderView.setVisiableHeight((visiableHeight));
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(XListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            }
        }
        SharedPreferences preferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE );
        if (!TextUtils.isEmpty(preferences.getString(mContext.getClass().getName()+".lasttime", ""))){
            setRefreshTime(DateUtil.friendlyTimeYMDHS(preferences.getString(mContext.getClass().getName()+".lasttime", "")));
        }
        setSelection(0); // scroll to top each time
    }

    public int getViewVisiableHeight(){
        return mHeaderView.getVisiableHeight();
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.

            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);

        // trigger computeScroll
        invalidate();

    }

    private void updateFooterHeight(float delta) {
        if (mEnableAutoLoadMore || !mEnablePullLoad) {
            return;
        }
        int height = mFooterView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load more.
                mFooterView.setState(XListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
            }
        }

        mFooterView.setBottomMargin(height);

        setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    /** 开始刷新数据*/
    private void startRefresh(){
        if (mPullRefreshing) {
            return;
        }
        mPullRefreshing = true;
        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
        if (mListViewListener != null) {
            mListViewListener.onRefresh();
            page = 0;

        }
    }

    private void startLoadMore() {
        if (mPullLoading) {
            return;
        }
        mPullLoading = true;
        mFooterView.setState(XListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onLoadMore();
            page += 1;
        }
    }

//    public void setHeadStates(){
//    	mHeaderViewContent.setVisibility(View.GONE);
//    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        SharedPreferences preferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE );
        String str = preferences.getString(mContext.getClass().getName()+".lasttime", "");
        if(preferences != null){
            setRefreshTime(DateUtil.friendlyTimeYMDHS(str));
        }
    }

    public String getStringDateLong() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }else if(canHeaderZero&&getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() <= 0)){
                    mHeaderView.setVisiableHeight(0);
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight && !mPullRefreshing && !mPullLoading) {
                        startRefresh();
                    }
                    resetHeaderHeight();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
                    if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA && !mPullRefreshing && !mPullLoading) {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());

            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }

        switch (scrollState) {
            // 当不滚动时
            case OnScrollListener.SCROLL_STATE_IDLE:
                // 判断滚动到底部
                if (getLastVisiblePosition() == (getCount() - 1)) {
                    if (mEnableAutoLoadMore && mEnablePullLoad) {
                        startLoadMore();
                    }
                } else if (getFirstVisiblePosition() == 0) {
                    // 判断滚动到顶部
                }
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    public void setXListViewListener(IXListViewListener l) {
        mListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IXListViewListener {
        public void onRefresh();

        public void onLoadMore();
    }

    public void setFooterNolmalText(String NolmalText){
        mFooterView.setNolmalText(NolmalText);
    }

    public void setFooterLoadingText(String LoadingText){
        mFooterView.setLoadingText(LoadingText);
    }

    public void setFooterReadyText(String ReadyText){
        mFooterView.setReadyText(ReadyText);
    }
}
