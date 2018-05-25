package love.wintrue.com.lovestaff.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import love.wintrue.com.lovestaff.receiver.NetChangeObserver;
import love.wintrue.com.lovestaff.utils.LogUtil;
import love.wintrue.com.lovestaff.widget.dialog.LoadingDialog;
import love.wintrue.com.lovestaff.widget.statusbarUtil.StatusBarUtil;

/**
 * @ClassName: BaseBankActivity
 * @Description: 基础activity
 */
public abstract class BaseActivity extends Activity {

    public Activity THIS;
    /**
     * 默认提示框
     */
    private Toast mToast;
    /**
     * 加载等待框
     */
    private LoadingDialog mLoadingDialog;
    protected InputMethodManager inputMethodManager;
    public static int colors[] = {0xffFF9B4C, 0xffFF2F50, 0xffFF2F50};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.d("onCreate");
        super.onCreate(savedInstanceState);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        THIS = this;
        doBeforeSetcontentView();
        setContentView(getLayoutId());
        ButterKnife.bind(THIS);
        setStatusBar();
        initView();
    }

    //获取布局文件
    public abstract int getLayoutId();

    //初始化view
    public abstract void initView();

    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    private void doBeforeSetcontentView() {
        MApplication.getInstance().getStackManager().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onStart() {
        LogUtil.d("onStart", "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        LogUtil.d("onResume", "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtil.d("onPause", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtil.d("onStop", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.d("onDestroy", "onDestroy");
        MApplication.getInstance().getStackManager().finishActivity(this);
        super.onDestroy();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }

    /**
     * 系统toast提示
     *
     * @param msg
     */
    public void showToastMsg(String msg) {
        if (mToast != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mToast.cancel();
            }
        } else {
            mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        mToast.setText(msg);
        mToast.show();
    }

    /**
     * 显示加载框
     *
     * @param msg
     */
    public void showWaitDialog(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(THIS);
        }
        mLoadingDialog.setText(msg);
        if (!mLoadingDialog.isShowing() && !isFinishing()) {
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
        try {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络连接连接时调用
     */
    public void onConnect(NetChangeObserver.NetType type) {
    }

    ;

    /**
     * 当前没有网络连接
     */
    public void onDisConnect() {
    }

    ;

    /**
     * 隐藏系统软键盘
     */
    public void hideSoftInput() {
        if (getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 隐藏系统软键盘
     *
     * @param view
     */
    public void hideSoftInput(View view) {
        if (view == null) {
            hideSoftInput();
            return;
        }
        if (getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(view.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void showSoftInput(View view) {
        if (view == null) {
            hideSoftInput();
            return;
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<>();
    private Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<>();

    /**
     * 请求权限
     *
     * @param id                   请求授权的id 唯一标识即可
     * @param permission           请求的权限
     * @param allowableRunnable    同意授权后的操作
     * @param disallowableRunnable 禁止权限后的操作
     */
    public void requestPermission(int id, String[] permission, String opPermission, Runnable allowableRunnable, Runnable disallowableRunnable) {
        if (allowableRunnable == null) {
            throw new IllegalArgumentException("allowableRunnable == null");
        }
        allowablePermissionRunnables.put(id, allowableRunnable);
        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(id, disallowableRunnable);
        }
        //版本判断
        if (Build.VERSION.SDK_INT >= 19) {
            //是否拥有权限
            for (int i = 0; i < permission.length; i++) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, permission[i]);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        //弹出对话框接收权限
                        LogUtil.e("弹出对话框接收权限");
                        ActivityCompat.requestPermissions(BaseActivity.this, permission, id);
                        return;
                    }
                }
                try {
                    AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                    int checkOp = appOpsManager.checkOp(opPermission, MApplication.getInstance().getPackageInfo().applicationInfo.uid, getPackageName());
                    if (checkOp == AppOpsManager.MODE_IGNORED) {
                        // 权限被拒绝了 .
                        disallowableRunnable.run();
                        return;
                    }
                } catch (Exception e) {
                    LogUtil.e("allowableRunnable Exception");
                    if (i == permission.length - 1) {
                        allowableRunnable.run();
                        return;
                    }
                }
                if (i == permission.length - 1) {
                    LogUtil.e("allowableRunnable end");
                    allowableRunnable.run();
                }
            }
        } else {
            allowableRunnable.run();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isAllow = true;
        LogUtil.e("onRequestPermissionsResult");
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isAllow = false;
                break;
            }
        }
        if (isAllow) {
            Runnable allowRun = allowablePermissionRunnables.get(requestCode);
            if (allowRun != null) {
                allowRun.run();
            }
        } else {
            Runnable disallowRun = disallowablePermissionRunnables.get(requestCode);
            if (disallowRun != null) {
                disallowRun.run();
            }
        }
    }


    /**
     * APP是否处于前台唤醒状态
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

}
