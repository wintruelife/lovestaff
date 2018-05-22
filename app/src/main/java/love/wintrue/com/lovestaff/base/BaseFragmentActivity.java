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
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import love.wintrue.com.lovestaff.receiver.NetChangeObserver;
import love.wintrue.com.lovestaff.utils.LogUtil;
import love.wintrue.com.lovestaff.widget.dialog.LoadingDialog;

/**
 * FragmentActivity基类
 *
 * @ClassName: BaseFragmentActivity.java
 */
public class BaseFragmentActivity extends FragmentActivity {

    public Activity THIS;
    /** 默认提示框 */
    private Toast mToast;
    /** 加载等待框 */
    private LoadingDialog mLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MApplication.getInstance().getStackManager().addActivity(this);
        LogUtil.d("onCreate");
        super.onCreate(savedInstanceState);
        THIS = this;
        // 取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onResume() {
//        if (!Application.getInstance().getIsActive()) {
//            //app 从后台唤醒，进入前台
//            Application.getInstance().setIsActive(true);
//            LogUtil.d("ACTIVITY", "程序从后台唤醒");
//            if(!TextUtils.isEmpty(Application.getInstance().getUser().getUid())){
//                String pwdKey = Application.getInstance().getUser().getPhone()+ AppConstants.PARAM_GESTURE_PWD;
//                String switchKey = Application.getInstance().getUser().getPhone() + AppConstants.PARAM_GESTURE_SWITCH;
//                if(!TextUtils.isEmpty(PrefersUtil.getInstance().getStrValue(pwdKey))
//                        && PrefersUtil.getInstance().getBooleanValue(switchKey,false)
//                        && !com.ytsh.finance.ActivityManager.getInstance().isExistenceActivity(GestureVerifyActivity.class)
//                        && com.ytsh.finance.ActivityManager.getInstance().isExistenceActivity(MainActivity.class)
//                        && !Application.getInstance().isActivityResult()){
//                    ActivityUtil.next(THIS, GestureVerifyActivity.class);
//                }
//            }
//            Application.getInstance().setActivityResult(false);
//        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
//        if (!isAppOnForeground()) {
//            //app 进入后台
//            Application.getInstance().setIsActive(false);//记录当前已经进入后台
//            LogUtil.d("ACTIVITY", "程序进入后台");
//        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        MApplication.getInstance().getStackManager().finishActivity(this);
        super.onDestroy();
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
            mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
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
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.setText(msg);
        if (!mLoadingDialog.isShowing() && !isFinishing()) {
            try {
                mLoadingDialog.show();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 网络连接连接时调用
     */
    public void onConnect(NetChangeObserver.NetType type) {};

    /**
     * 当前没有网络连接
     */
    public void onDisConnect() {};

    /** 隐藏加载等待框 */
    public void hideWaitDialog() {
        try{
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /** 隐藏系统软键盘 */
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

    public void showSoftInput(View view){
        if (view == null) {
            hideSoftInput();
            return;
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }


    private Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<>();
    private Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<>();
    /**
     * 请求权限
     * @param id 请求授权的id 唯一标识即可
     * @param permission 请求的权限
     * @param allowableRunnable 同意授权后的操作
     * @param disallowableRunnable 禁止权限后的操作
     */
    protected void requestPermission(int id, String[] permission,String opPermission, Runnable allowableRunnable, Runnable disallowableRunnable) {
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
                        ActivityCompat.requestPermissions(BaseFragmentActivity.this, permission, id);
                        return;
                    }
                }
                try{
                    AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                    int checkOp = appOpsManager.checkOp(opPermission, MApplication.getInstance().getPackageInfo().applicationInfo.uid, getPackageName());
                    if (checkOp == AppOpsManager.MODE_IGNORED) {
                        // 权限被拒绝了 .
                        disallowableRunnable.run();
                        return;
                    }
                }catch (Exception e){
                    if (i == permission.length - 1) {
                        allowableRunnable.run();
                        return;
                    }
                }
                if (i == permission.length - 1) {
                    allowableRunnable.run();
                }
            }
        }else {
            allowableRunnable.run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Runnable allowRun = allowablePermissionRunnables.get(requestCode);
            if(allowRun != null) {
                allowRun.run();
            }
        } else {
            Runnable disallowRun = disallowablePermissionRunnables.get(requestCode);
            if(disallowRun != null) {
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
