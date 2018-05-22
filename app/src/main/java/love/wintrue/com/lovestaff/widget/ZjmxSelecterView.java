package love.wintrue.com.lovestaff.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.adapter.CommonBaseAdapter;

import java.util.List;

public class ZjmxSelecterView extends FrameLayout implements AdapterView.OnItemClickListener{

	private Context context;
	private View mView;
	private MyGridView gridView;
	private int curPosition = 0;
	private ZjmxSelecterAdapter adapter;
	private OnDismissListener dismissListener;

	public ZjmxSelecterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
		this.context = context;
	}

	public ZjmxSelecterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
		this.context = context;
	}

	public ZjmxSelecterView(Context context) {
		super(context);
		initViews(context);
		this.context = context;
	}

	private void initViews(Context context) {
		mView = LayoutInflater.from(context).inflate(R.layout.widget_zjmx_selector_list, this);
		if(mView != null){
			mView.setVisibility(INVISIBLE);
			gridView = (MyGridView) mView.findViewById(R.id.zjmx_selecter_gridview);
			gridView.setOnItemClickListener(this);
			adapter = new ZjmxSelecterAdapter(context);
			gridView.setAdapter(adapter);
			mView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(curPosition != position && adapter != null){
			curPosition = position;
			adapter.notifyDataSetChanged();
		}
		dismiss();
	}

	public void setCurPosition(int position){
		curPosition = position;
	}

	/**
	 * 设置选择项数据
	 * @param list
	 */
	public void setData(List<String> list){
		adapter.setList(list);
	}

	/**
	 * 设置监听
	 * @param dismissListener
	 */
	public void setOnDismissListener(OnDismissListener dismissListener){
		this.dismissListener = dismissListener;
	}

	/**
	 * 显示
	 */
	public void show(){
		mView.setVisibility(View.VISIBLE);
		mView.bringToFront();
		TranslateAnimation animation = new TranslateAnimation(0,0,-gridView.getHeight(),0);
		animation.setFillAfter(true);
		animation.setDuration(300);
		gridView.startAnimation(animation);
	}

	/**
	 * 消失
	 */
	public void dismiss(){
		TranslateAnimation animation = new TranslateAnimation(0,0,0,-gridView.getHeight());
		animation.setFillAfter(true);
		animation.setDuration(300);
		gridView.startAnimation(animation);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mView.setVisibility(INVISIBLE);
				if(dismissListener != null){
					dismissListener.onDismiss(curPosition,adapter.getItem(curPosition));
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}

	class ZjmxSelecterAdapter extends CommonBaseAdapter<String> {

		public ZjmxSelecterAdapter(Context context){
			super(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mLayoutInflater.inflate(R.layout.widget_zjmx_selector_list_item,null);
			TextView textView = (TextView) convertView;
			if(curPosition == position){
				textView.setBackgroundResource(R.drawable.red_round_bg_3);
				textView.setTextColor(mContext.getResources().getColor(R.color.white));
			}else{
				textView.setBackgroundResource(R.drawable.gray_round_bg_3);
				textView.setTextColor(mContext.getResources().getColor(R.color.color_545454));
			}
			textView.setText(getItem(position));
			return convertView;
		}
	}

	public interface OnDismissListener{
		void onDismiss(int position, String item);
	}

}
