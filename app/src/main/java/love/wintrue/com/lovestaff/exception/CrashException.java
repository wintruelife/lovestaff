package love.wintrue.com.lovestaff.exception;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.widget.Toast;


import java.lang.Thread.UncaughtExceptionHandler;

import love.wintrue.com.lovestaff.base.MApplication;
import love.wintrue.com.lovestaff.config.AppConfig;
import love.wintrue.com.lovestaff.utils.LogUtil;

/**
 * @ClassName: CrashException
 * @Description: 应用崩溃异常
 */
public class CrashException extends BaseException implements
		UncaughtExceptionHandler {

	private static final long serialVersionUID = 171420830016802708L;
	/** 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler mDefaultHandler;
	private Context mContext;

	public CrashException(Context context) {
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		this.mContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if(!handleException(ex) && mDefaultHandler != null){
            //如果用户没有处理则让系统默认的异常处理器来处理    
			 mDefaultHandler.uncaughtException(thread, ex);
        }else{
			Toast.makeText(mContext,"应用出现异常，程序即将重启...",Toast.LENGTH_LONG).show();
//			try{
//				Thread.sleep(2000);
//			}catch (InterruptedException e){
////				Log.e(TAG, "error : ", e);
//			}
//			Intent intent = new Intent(mContext, SplashActivity.class);
//			PendingIntent restartIntent = PendingIntent.getActivity(
//					mContext, 0, intent,
//					Intent.FLAG_ACTIVITY_NEW_TASK);
//			//退出程序
//			AlarmManager mgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
//			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
//					restartIntent); // 1秒钟后重启应用
//			MApplication.getInstance().exit();
//			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	/**
	 * 自定义异常处理:收集错误信息&发送错误报告
	 * 
	 * @param ex
	 * @return true:处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}

		final Context context = MApplication.getInstance().getStackManager().getCurrentActivity();

		if (context == null) {
			return false;
		}

		final String crashReport = getCrashReport(context, ex);
		LogUtil.e(crashReport);
		if (!AppConfig.SEND_CARSH) {
			return false;
		}
//		DhApplication.getInstance().exit();
		// 显示异常信息&发送报告
//		new Thread() {
//			@Override
//			public void run() {
//				Looper.prepare();
//				// UIHelper.sendAppCrashReport(context, crashReport);
//				// 写在Looper.loop()之后的代码不会被执行，这个函数内部应该是一个循环，当调用mHandler.getLooper().quit()后，loop才会中止，其后的代码才能得以运行。
//				DhApplication.getInstance().exit();
//				Looper.loop();
//			}
//		}.start();
		return true;
	}

	/**
	 * 获取APP崩溃异常报告
	 * 
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context context, Throwable ex) {
		PackageInfo pinfo = ((MApplication) context.getApplicationContext()).getPackageInfo();
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("Version: " + pinfo.versionName + "(" + pinfo.versionCode + ")\n");
		exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.MODEL + ")\n");
		exceptionStr.append("Exception: " + ex.getMessage() + "\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString() + "\n");
		}
		return exceptionStr.toString();
	}
}
