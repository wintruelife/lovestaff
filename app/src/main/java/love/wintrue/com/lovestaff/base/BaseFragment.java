package love.wintrue.com.lovestaff.base;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import love.wintrue.com.lovestaff.widget.dialog.LoadingDialog;

/**
 * @ClassName: BaseFragment
 * @Description:
 */
public abstract class BaseFragment extends Fragment {

	/** Toast */
	private Toast mToast;
	/** ProgressDialog */
	private LoadingDialog mLoadingDialog;
	private boolean needNotifyOnAttach = false;
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected View rootView;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if(needNotifyOnAttach){
			onNotifyData();
			needNotifyOnAttach = false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null)
			rootView = inflater.inflate(getLayoutResource(), container, false);
		ButterKnife.bind(this, rootView);
		mContext = getActivity();
		mInflater = inflater;
		initView();
		return rootView;
	}

	//获取布局文件
	protected abstract int getLayoutResource();

	//初始化view
	protected abstract void initView();

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null) {
			this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
		}
	}

	protected View findViewById(int res) {
		return getView().findViewById(res);
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
			mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);
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
		if(getActivity() == null){
			return;
		}
		if (mLoadingDialog == null) {
			mLoadingDialog = new LoadingDialog(getActivity(), msg);
		}
			mLoadingDialog.setText(msg);
		if (!mLoadingDialog.isShowing() && !getActivity().isFinishing()) {
			try {
				mLoadingDialog.show();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 隐藏加载等待框 */
	public void hideWaitDialog() {
		try {
			if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
				mLoadingDialog.dismiss();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<>();
	private Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<>();
	protected void requestPermission(int id, String[] permission,String opPermission, Runnable allowableRunnable, Runnable disallowableRunnable) {
		if (allowableRunnable == null) {
			throw new IllegalArgumentException("allowableRunnable == null");
		}
		allowablePermissionRunnables.put(id, allowableRunnable);
		if (disallowableRunnable != null) {
			disallowablePermissionRunnables.put(id, disallowableRunnable);
		}
		//版本判断
		if (Build.VERSION.SDK_INT >= 23) {
			//减少是否拥有权限
			for (int i = 0; i < permission.length; i++) {
				int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), permission[i]);
				if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
					//弹出对话框接收权限
					ActivityCompat.requestPermissions(getActivity(), permission, id);
					return;
				}
				AppOpsManager appOpsManager = (AppOpsManager) getActivity().getSystemService(Context.APP_OPS_SERVICE);
				int checkOp = appOpsManager.checkOp(opPermission, MApplication.getInstance().getPackageInfo().applicationInfo.uid, getActivity().getPackageName());
				if (checkOp == AppOpsManager.MODE_IGNORED) {
					// 权限被拒绝了 .
					disallowableRunnable.run();
					return;
				}
				if(i == permission.length - 1) {
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
	 * 更新数据
	 */
	public final void notifyData(){
		needNotifyOnAttach = false;
		if(getActivity() == null){
			needNotifyOnAttach = true;
		}else {
			onNotifyData();
		}
	}

	/**
	 * 子类具体更新数据更新数据操作
	 */
	protected void onNotifyData(){

	}

}
