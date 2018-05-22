package love.wintrue.com.lovestaff.base;


import android.app.Activity;

import java.util.Stack;

public class ActivityManager {

	private static ActivityManager instance;
	private Stack<Activity> activityStack;

	private ActivityManager() {
	}

	/**
	 * 单例
	 */
	public static ActivityManager getInstance() {
		if (instance == null) {
			instance = new ActivityManager();
		}
		return instance;
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity getCurrentActivity() {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		if (activityStack.size() > 0) {
			Activity activity = activityStack.lastElement();
			return activity;
		}
		return null;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.push(activity);
	}

	/**
	 * 移除指定的Activity
	 */
	public void removeActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
		}
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		removeActivity(activity);
	}

	/**
	 * 结束Activity
	 * 
	 * @param activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
				break;
			}
		}
	}

	/**
	 * 判断制定类名的Activity在栈中是否存在
	 * @param cls
	 * @return
	 */
	public boolean isExistenceActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		if(activityStack != null) {
			while (!activityStack.isEmpty()) {
				finishActivity(activityStack.pop());
			}
			activityStack.clear();
		}
	}

	/**
	 * 结束所有Activity除了LoginTabActivity
	 */
	public void finishAllActivityExcept() {
//		Activity activity = null;
//		for (int i = activityStack.size() - 1; i >-1;i--) {
//			activity = activityStack.get(i);
//			if (!(activity instanceof LoginActivity)) {
//				finishActivity(activity);
//			}
//		}
	}

	/**
	 * 退出应用程序
	 *
	 */
	public void appExit() {
		finishAllActivity();
//		System.exit(0);
//		android.os.Process.killProcess(android.os.Process.myPid());
	}
}