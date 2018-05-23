package love.wintrue.com.lovestaff.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import love.wintrue.com.lovestaff.bean.GesturePasswordBean;
import love.wintrue.com.lovestaff.bean.User;
import love.wintrue.com.lovestaff.config.AppConfig;
import love.wintrue.com.lovestaff.config.PreferenceConfig;
import love.wintrue.com.lovestaff.exception.CrashException;
import love.wintrue.com.lovestaff.http.ApiService;
import love.wintrue.com.lovestaff.receiver.NetChangeObserver;
import love.wintrue.com.lovestaff.receiver.NetworkStateReceiver;
import love.wintrue.com.lovestaff.ui.activity.MainActivity;
import love.wintrue.com.lovestaff.utils.FrescoImageLoader;
import love.wintrue.com.lovestaff.utils.ImageDownLoader;
import love.wintrue.com.lovestaff.utils.ImageUtil;
import love.wintrue.com.lovestaff.utils.LogUtil;
import love.wintrue.com.lovestaff.utils.StringEncodeUtil;
import love.wintrue.com.lovestaff.utils.Util;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @des:
 * @author:th
 * @email:342592622@qq.com
 * @date: 2017/3/22 14:22
 */

public class MApplication extends Application {
    private static final int DEFAULT_TIMEOUT = 30;//网络请求超时时间 秒

    private static MApplication mInstance;
    private String mDisplay;// 屏幕分辨率
    private boolean mIsWifiConnectNetWork;
    private ActivityManager mStackManager;//应用activity管理器
    private boolean mNetworkAvailable = true;
    private ApiService mApiService;
    private User mUser;
    private boolean isActivityResult;//标识是否从选择图片过来，用于手势是否显示
    private boolean isActive = true;//标识是否是启动的APP唤醒的程序
    private boolean isFirstMain = true;

    private GesturePasswordBean gesturePasswordBean;//验证手势密码

    public GesturePasswordBean getGesturePasswordBean() {
        return gesturePasswordBean;
    }

    public void setGesturePasswordBean(GesturePasswordBean gesturePasswordBean) {
        this.gesturePasswordBean = gesturePasswordBean;
    }

    public boolean isActivityResult() {
        return isActivityResult;
    }

    public void setActivityResult(boolean activityResult) {
        isActivityResult = activityResult;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mStackManager = ActivityManager.getInstance();
        initExceptionHandler();
        initNetworkObserver();
        iniApiService(DEFAULT_TIMEOUT);
        initFresco();
        initActivityLife();
        initPicasso();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initFresco() {
        Fresco.initialize(this, FrescoImageLoader.getImagePipelineConfig(this));
    }

    public static MApplication getInstance() {
        return mInstance;
    }


    /**
     * APP是否处于前台唤醒状态
     *
     * @return
     */
    public boolean isAppOnForeground() {
        android.app.ActivityManager activityManager = (android.app.ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<android.app.ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    public String getVersion() {
        return getPackageInfo().versionName;
    }

    public int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public String getDeviceInfo() {
        String deviceInfo = Build.MANUFACTURER + "#" + Build.VERSION.RELEASE
                + "#android#" + Build.BRAND + "#" + Build.MODEL + "#"
                + getDisplay();
        return deviceInfo;
    }

    public String getDisplay() {
        return mDisplay;
    }

    /**
     * 屏幕分辨率
     *
     * @param mDisplay
     */
    public void setDisplay(String mDisplay) {
        this.mDisplay = mDisplay;
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppUniqueID() {
        String uniqueID = PreferenceConfig.getAppUniqueID(this);
        if (StringEncodeUtil.isEmpty(uniqueID)) {
            // 获取设备ID
            try {
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                uniqueID = tm.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 获取不到，则随机生成一个标识
            if (StringEncodeUtil.isEmpty(uniqueID)) {
                uniqueID = UUID.randomUUID().toString();
            }
            PreferenceConfig.saveAppUniqueId(this, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 覆盖系统异常处理
     */
    private void initExceptionHandler() {
        // 注册App异常崩溃处理器
        if (AppConfig.SEND_CARSH) {
            Thread.setDefaultUncaughtExceptionHandler(new CrashException(this));
        }
    }

    public boolean isWifiConnectNetWork() {
        return mIsWifiConnectNetWork;
    }

    public void setWifiConnectNetWork(boolean isWifiConnectNetWork) {
        this.mIsWifiConnectNetWork = isWifiConnectNetWork;
    }

    /**
     * activity管理堆栈
     */
    public ActivityManager getStackManager() {
        if (mStackManager == null) {
            mStackManager = ActivityManager.getInstance();
        }
        return mStackManager;
    }

    /**
     * 注册网路连接状态监听
     */
    private void initNetworkObserver() {
        if (NetworkStateReceiver.getNetworkType(mInstance) == NetChangeObserver.NetType.wifi || NetworkStateReceiver.getNetworkType(mInstance) == NetChangeObserver.NetType.G4) {
            mIsWifiConnectNetWork = true;
        }
        NetworkStateReceiver.registerObserver(new NetChangeObserver() {

            @Override
            public void onConnect(NetType type) {
                super.onConnect(type);
                mNetworkAvailable = true;
                if (mStackManager != null) {
                    Activity activity = mStackManager.getCurrentActivity();
                    if (activity == null) {
                        return;
                    }
                    if (activity instanceof BaseActivity) {
                        ((BaseActivity) activity).onConnect(type);
                    } else if (activity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) activity).onConnect(type);
                    }
                }
            }

            @Override
            public void onDisConnect() {
                super.onDisConnect();
                mNetworkAvailable = false;
                if (mStackManager != null) {
                    Activity activity = mStackManager.getCurrentActivity();
                    if (activity == null) {
                        return;
                    }
                    if (activity instanceof BaseActivity) {
                        ((BaseActivity) activity).onDisConnect();
                    } else if (activity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) activity).onDisConnect();
                    }
                }
            }
        });
    }

    /**
     * 网络连接状态
     */
    public boolean isNetworkAvailable() {
        return mNetworkAvailable;
    }

    public ApiService iniApiService(int timeOut) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
//                .addInterceptor(cacheInterceptor)
//                .addNetworkInterceptor(cacheInterceptor)
//                .cache(cache)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
//                .hostnameVerifier(mHostnameVerifier)
//                .sslSocketFactory(HttpsFactroy.getSSLSocketFactory(this))//加密文件
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConfig.HTTP_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiService = retrofit.create(ApiService.class);
        return mApiService;
    }

    private void initPicasso() {
        Picasso.setSingletonInstance(new Picasso.Builder(this).
                downloader(new ImageDownLoader(ImageUtil.getUnsafeOkHttpClient(this)))
                .build());
    }

    public ApiService getApiService() {
        return mApiService;
    }

    /**
     * 退出系统
     */
    public void exit() {
        mStackManager.appExit();
    }

    public User getUser() {
        mUser = PreferenceConfig.readUser(this);
        if (mUser == null) {
            mUser = new User();
        }
        return this.mUser;
    }

    public void setUser(User user) {
        PreferenceConfig.saveUser(this, user);
        this.mUser = user;
    }

    private void initActivityLife() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (!getIsActive()) {
                    LogUtil.e("ACTIVITY", "程序从后台唤醒");
                    //app 从后台唤醒，进入前台
                    setIsActive(true);
                    toLock(activity);
                    setActivityResult(false);
                } else {
                    LogUtil.e("isFirstMain:" + isFirstMain);
                    if (isFirstMain && activity instanceof MainActivity) {
                        isFirstMain = false;
                        toLock(activity);
                    }
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (!isAppOnForeground() || !Util.isScreenOn(getApplicationContext())) {
                    //app 进入后台
                    setIsActive(false);//记录当前已经进入后台
                    LogUtil.e("ACTIVITY", "程序进入后台");
//                    EventBus.getDefault().post(new MessageEvent(MessageEvent.MESSAGE_HIDE_FC));
                }
                LogUtil.e("onActivityStopped", "onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void toLock(Activity activity) {
//        if (!TextUtils.isEmpty(getUser().getUid())) {
//            String pwdKey = getUser().getMobile() + AppConstants.PARAM_GESTURE_PWD;
//            String switchKey = getUser().getPhone() + AppConstants.PARAM_GESTURE_SWITCH;

//            if (gesturePasswordBean != null&&"open".equals(gesturePasswordBean.getState())
//                    && !ActivityManager.getInstance().isExistenceActivity(GestureVerifyActivity.class)
//                    && ActivityManager.getInstance().isExistenceActivity(MainActivity.class)
//                    && !isActivityResult()) {
//                ActivityUtil.next(activity, GestureVerifyActivity.class);
//            }
//        }
    }
}
