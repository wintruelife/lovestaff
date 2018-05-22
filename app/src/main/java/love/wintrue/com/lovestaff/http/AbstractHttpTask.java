package love.wintrue.com.lovestaff.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.ActivityManager;
import love.wintrue.com.lovestaff.base.MApplication;
import love.wintrue.com.lovestaff.bean.BaseOtherBean;
import love.wintrue.com.lovestaff.utils.ActivityUtil;
import love.wintrue.com.lovestaff.utils.EmojiUtil;
import love.wintrue.com.lovestaff.utils.LogUtil;
import love.wintrue.com.lovestaff.utils.StringUtils;
import love.wintrue.com.lovestaff.widget.DataLoadingLayout;
import love.wintrue.com.lovestaff.widget.dialog.CommonAlertDialog;
import love.wintrue.com.lovestaff.widget.dialog.LoadingDialog;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author th
 * @desc 网络请求task基类
 * @time 2017/3/17 15:25
 */
public abstract class AbstractHttpTask<T> implements ResponseParser<T> {
    private final String TAG = AbstractHttpTask.class.getSimpleName();

    private Context mContext;
    protected Map<String, Object> mDatas;
    protected Map<String, Object> mDataTemps;
    private Map<String, Object> mRequestHeader;
    private AbstractHttpResponseHandler<T> mHandler;
    /**
     * 加载等待框
     */
    private LoadingDialog mLoadingDialog;
    private boolean mShowDialog = true;
    private DataLoadingLayout mDataLoadingLayout;
    private boolean mShowDataLoadingLayout = false;
    Observable<ResponseBody> call;

    public AbstractHttpTask(Context context) {
        this.mContext = context;
        this.mDatas = new HashMap<>();
        this.mRequestHeader = new HashMap<>();
        mDataTemps = new HashMap<>();
    }

    public void send() {
        for (String key : mDatas.keySet()) {
            if (mDatas.get(key) != null) {
                String value = mDatas.get(key).toString();
                if (EmojiUtil.hasEmoji(value)) {
                    Toast.makeText(mContext, "请勿输入表情!", Toast.LENGTH_LONG).show();
                    mHandler.onFinish();
                    return;
                }
                mDataTemps.put(key, mDatas.get(key));
            }
        }
        if (mShowDialog && !mShowDataLoadingLayout) {
            showWaitDialog(mContext.getResources().getString(R.string.txt_on_wait));
        }
        if (mDataLoadingLayout != null && mShowDataLoadingLayout) {
            mDataLoadingLayout.showDataLoading();
        }
        HttpTaskUtil.addGlobParams(mRequestHeader,mDatas, mContext);

        mHandler.onStart();
        if (getMethod() == Method.GET) {
            call = MApplication.getInstance().getApiService().getResponseBody(mRequestHeader, getUrl(), mDataTemps);
        } else {
            call = MApplication.getInstance().getApiService().postResponseBody(mRequestHeader, getUrl(), mDataTemps);
        }


//        BaseInterface3.getQueryMap(call.get);

        call.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        if (null != mContext) {
                            mHandler.onFinish();
                            hideWaitDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != mContext) {
                            try {
                                hideWaitDialog();
                                String errorMsg;
                                if (e.getMessage().startsWith("Failed to connect to")
                                        || e.getMessage().startsWith("failed to connect to")
                                        || e.getMessage().startsWith("HTTP")
                                        || e.getMessage().endsWith("No address associated with hostname")) {
                                    errorMsg = "网络状况不佳";
                                } else if (e.getMessage().startsWith("java.security.cert")) {
                                    errorMsg = "当前网络不安全,请切换网络";
                                } else {
                                    errorMsg = e.getMessage();
                                }

                                if (mDataLoadingLayout != null && mShowDataLoadingLayout) {
                                    mDataLoadingLayout.showDataLoadFailed(errorMsg);
                                }
                                mHandler.onFailure("405", errorMsg);
                            } catch (Exception e1) {
                                mHandler.onFailure("405", "网络请求超时");
                            }
                            mHandler.onFinish();
                        }
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (null != mContext) {
                            hideWaitDialog();
                            try {
                                String data = new String(responseBody.bytes());
                                LogUtil.e(TAG, data);
                                if (StringUtils.isNotEmpty(data)) {
                                    BaseOtherBean baseOtherBean = com.alibaba.fastjson.JSON.parseObject(data,BaseOtherBean.class);

                                    if (baseOtherBean != null && "0".equals(baseOtherBean.getStatus()) && ("无效的token".equals(baseOtherBean.getMsg()) || "token为空".equals(baseOtherBean.getMsg()))) {//token无效单点登录
                                        MApplication application = MApplication.getInstance();
//                                        User user = application.getUser();

                                        ActivityManager activityManager = ActivityManager.getInstance();
//                                        if (StringUtils.isNotEmpty(user.getUserId())) {
//                                        if (!(activityManager.getCurrentActivity() instanceof LoginActivity)) {
//                                            application.setUser(null);
//                                            ActivityUtil.next(mContext, LoginActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            activityManager.finishAllActivityExcept();
//
//                                        }
                                        mHandler.onFailure("505", "登录失效");
                                    } else {//返回正确
                                        mHandler.onSuccess(parse(data));
                                    }
                                } else {
                                    mHandler.onFailure("505", "数据格式化错误");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            catch (JSONException e) {
//                                e.printStackTrace();
//                                if (mDataLoadingLayout != null && mShowDataLoadingLayout) {
//                                    mDataLoadingLayout.showDataLoadFailed("数据格式化错误");
//                                }
//                                mHandler.onFailure("505", "数据格式化错误");
//                            }
                        }
                    }
                });
    }

    private void showLoginOutDialog() {
        final CommonAlertDialog mDialog = new CommonAlertDialog(mContext);

        mDialog.setTitle("下线通知");
        mDialog.setMessage("当前登录已失效");
        mDialog.setMessageSub("");
        mDialog.setNegativeButton(R.string.cancel, R.color.color_333333, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
//                Bundle bd = new Bundle();
//                bd.putInt(AppConstants.PARAM_FROM_TYPE, AppConstants.PARAM_FROM_TYPE_TOKEN_INVALID);
//                ActivityUtil.next(mContext, LoginTabActivity.class, bd, Intent.FLAG_ACTIVITY_NEW_TASK);
//                ActivityManager activityManager = ActivityManager.getInstance();
//                activityManager.finishAllActivityExcept();
//                ActivityUtil.next(mContext, MainActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });
        mDialog.setPositiveButton(R.string.text_login_again, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
//                ActivityUtil.next(mContext, LoginActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK);
//                ActivityManager activityManager = ActivityManager.getInstance();
//                activityManager.finishAllActivityExcept();
            }
        });
        mDialog.setButtonPositiveTextColor(mContext.getResources().getColor(R.color.color_f24030));

        mDialog.setIsCancelable(false);
        mDialog.setIsCanceledOnTouchOutside(false);
        mDialog.show();
    }

    /**
     * 显示加载框
     *
     * @param msg
     */
    public void showWaitDialog(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
        }
        mLoadingDialog.setText(msg);
        if (!mLoadingDialog.isShowing()) {
            try {
                mLoadingDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 隐藏加载等待框
     */
    public void hideWaitDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing() && !((Activity) mContext).isFinishing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void setCallBack(boolean showDialog, AbstractHttpResponseHandler<T> handler) {
        this.mShowDialog = showDialog;
        this.mHandler = handler;
    }

    public void setDataLoadingLayout(DataLoadingLayout Layout, boolean isShow) {
        this.mDataLoadingLayout = Layout;
        this.mShowDataLoadingLayout = isShow;
    }

    public void cancel() {
        if (call != null) {
            call.unsubscribeOn(Schedulers.io());
        }
    }

    public abstract String getUrl();

    public abstract Method getMethod();

    protected enum Method {
        POST, GET
    }
}
