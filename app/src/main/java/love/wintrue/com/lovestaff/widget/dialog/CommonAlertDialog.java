package love.wintrue.com.lovestaff.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import love.wintrue.com.lovestaff.R;

/**
 * 通用自定义样式AlertDialog
 *
 * @ClassName: CommonAlertDialog.java
 */
public class CommonAlertDialog {

	private AlertDialog mAlertDialog;
	private TextView mTxtViewTitle;
	private TextView mTxtViewMessage;
	private TextView mTxtViewMessageSub;
	private Button mBtnPositive;
	private Button mBtnNegative;
	private Button mBtnLong;
	private View mVline;
	private ImageView mImageView;
	private Context mContext;
	public CommonAlertDialog(Context context) {
		this.mContext = context;
		mAlertDialog = new AlertDialog.Builder(context).create();
		mAlertDialog.show();
		Window window = mAlertDialog.getWindow();
		window.setContentView(R.layout.widget_dialog_custom);
		mAlertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		mTxtViewTitle = (TextView) window.findViewById(R.id.txt_dialog_title);
		mTxtViewMessage = (TextView) window.findViewById(R.id.txt_dialog_message);
		mTxtViewMessageSub = (TextView) window.findViewById(R.id.txt_dialog_message_sub);
		mBtnPositive = (Button) window.findViewById(R.id.btn_dialog_positive);
		mBtnNegative = (Button) window.findViewById(R.id.btn_dialog_negative);
		mBtnLong = (Button) window.findViewById(R.id.btn_dialog_long);
		mVline = window.findViewById(R.id.v_line);
		mImageView = (ImageView)window.findViewById(R.id.iv_img);
		mAlertDialog.setCancelable(false);
		mBtnNegative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public void setIsCancelable(boolean isCancelable){
		mAlertDialog.setCancelable(isCancelable);
	}

	public void setIsCanceledOnTouchOutside(boolean isCancelable){
		mAlertDialog.setCanceledOnTouchOutside(isCancelable);
	}

	public AlertDialog getAlertDialog(){
		return mAlertDialog;
	}
	public void setButtonTextColor(int color){
		mBtnPositive.setTextColor(color);
		mBtnNegative.setTextColor(color);
	}
	public void setButtonNegativeTextColor(int color){
		mBtnNegative.setTextColor(color);
	}
	public void setButtonPositiveTextColor(int color){
		mBtnPositive.setTextColor(color);
	}
	public void setTitle(int resId) {
		mTxtViewTitle.setText(resId);
		mTxtViewTitle.setVisibility(View.VISIBLE);
	}

	public void setTitle(String txt) {
		mTxtViewTitle.setText(txt);
		mTxtViewTitle.setVisibility(View.VISIBLE);
	}

	public TextView getTxtViewMessage(){
		return mTxtViewMessage;
	}


	public void setMessage(int resId) {
		mTxtViewMessage.setVisibility(View.VISIBLE);
		mTxtViewMessage.setText(resId);
	}

	public void setMessage(String txt){
		mTxtViewMessage.setVisibility(View.VISIBLE);
		mTxtViewMessage.setText(txt);
	}

	public void setMessageSub(int resId) {
		mTxtViewMessageSub.setVisibility(View.VISIBLE);
		mTxtViewMessageSub.setText(resId);
	}

	public void setMessageSub(String txt){
		mTxtViewMessageSub.setVisibility(View.VISIBLE);
		mTxtViewMessageSub.setText(txt);
	}

	public void setMessageSubColor(int color) {
		mTxtViewMessageSub.setTextColor(color);
	}

	public void setMessageSubSize(int sise) {
		mTxtViewMessageSub.setTextSize(sise);
	}

	public void setMessageGravity(int gravity){
		mTxtViewMessage.setGravity(gravity);
		mTxtViewMessage.setPadding(50,50,50,50);
		mTxtViewMessage.setLineSpacing(15,1);
	}
	public void setMsgGravity(int gravity){
		mTxtViewMessage.setGravity(gravity);
	}
	/**
	 * 设置按钮（确定）
	 *
	 * @param resId
	 * @param onClickListener
	 */
	public void setPositiveButton(int resId, final View.OnClickListener onClickListener) {
		mBtnPositive.setText(resId);
		mBtnPositive.setOnClickListener(onClickListener);
		mVline.setVisibility(View.VISIBLE);
		mBtnPositive.setVisibility(View.VISIBLE);
		mBtnNegative.setBackgroundResource(R.drawable.bg_btn_negative_bottom_left);
	}

	/**
	 * 设置按钮（确定）
	 *
	 * @param resId
	 * @param onClickListener
	 */
	public void setPositiveButton(String resId, final View.OnClickListener onClickListener) {
		mBtnPositive.setText(resId);
		mBtnPositive.setOnClickListener(onClickListener);
		mVline.setVisibility(View.VISIBLE);
		mBtnPositive.setVisibility(View.VISIBLE);
		mBtnNegative.setBackgroundResource(R.drawable.bg_btn_negative_bottom_left);
	}

	/**
	 * 设置按钮（确定）
	 *
	 * @param resId
	 * @param onClickListener
	 */
	public void setPositiveButton(int resId,int textColor, int drawable, final View.OnClickListener onClickListener) {
		mBtnPositive.setText(resId);
		mBtnPositive.setTextColor(textColor);
		mBtnPositive.setOnClickListener(onClickListener);
		mVline.setVisibility(View.VISIBLE);
		mBtnPositive.setVisibility(View.VISIBLE);
		if(drawable != -1) {
			mBtnNegative.setBackgroundResource(drawable);
		}
	}

	/**
	 * 设置按钮（确定）
	 *
	 * @param resId
	 * @param onClickListener
	 */
	public void setPositiveButton(int resId,int textColor,final View.OnClickListener onClickListener) {
		mBtnPositive.setText(resId);
		mBtnPositive.setTextColor(textColor);
		mBtnPositive.setOnClickListener(onClickListener);
		mVline.setVisibility(View.VISIBLE);
		mBtnPositive.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置按钮（否定）
	 *
	 * @param resId
	 * @param onClickListener
	 */
	public void setNegativeButton(int resId, final View.OnClickListener onClickListener) {
		mBtnNegative.setText(resId);
		mBtnNegative.setOnClickListener(onClickListener);
		mBtnNegative.setVisibility(View.VISIBLE);
		mBtnLong.setVisibility(View.GONE);
	}

	/**
	 * 设置按钮（否定）
	 *
	 * @param resId
	 * @param onClickListener
	 */
	public void setNegativeButton(int resId, int textColor,final View.OnClickListener onClickListener) {
		mBtnNegative.setText(resId);
		mBtnNegative.setTextColor(textColor);
		mBtnNegative.setOnClickListener(onClickListener);
		mBtnNegative.setVisibility(View.VISIBLE);
		mBtnLong.setVisibility(View.GONE);
	}

	/**
	 * 设置按钮（否定）
	 *
	 * @param resId
	 * @param onClickListener
	 */
	public void setNegativeButton(String resId, final View.OnClickListener onClickListener) {
		mBtnNegative.setText(resId);
		mBtnNegative.setOnClickListener(onClickListener);
		mBtnNegative.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置按钮（一个长按钮，不分左右）
	 *
	 * @param resId
	 * @param onClickListener
	 */
	public void setLongButton(int resId, final View.OnClickListener onClickListener) {
		mBtnLong.setText(resId);
		mBtnLong.setOnClickListener(onClickListener);
		mBtnLong.setVisibility(View.VISIBLE);
		mBtnNegative.setVisibility(View.GONE);
		mVline.setVisibility(View.GONE);
		mBtnPositive.setVisibility(View.GONE);
	}

	public void setImg(int resId){
		mImageView.setImageResource(resId);
		mImageView.setVisibility(View.VISIBLE);
	}
	public void setLongButton(String str, final View.OnClickListener onClickListener) {
		mBtnLong.setText(str);
		mBtnLong.setOnClickListener(onClickListener);
		mBtnLong.setVisibility(View.VISIBLE);
		mBtnNegative.setVisibility(View.GONE);
		mVline.setVisibility(View.GONE);
		mBtnPositive.setVisibility(View.GONE);
	}
	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		if (null != mAlertDialog && !((Activity)mContext).isFinishing()) {
			mAlertDialog.dismiss();
		}
	}

	/**
	 * 显示对话框
	 */
	public void show() {
		if (null != mAlertDialog && !mAlertDialog.isShowing()) {
			mAlertDialog.show();
		}
	}

}
