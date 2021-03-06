package love.wintrue.com.lovestaff.widget.wheel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.utils.DensityUtil;
import love.wintrue.com.lovestaff.widget.wheel.adapter.NumericLableWheelAdapter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CommonDatePickerWheelPanel {

    private Context mContext;
    
	private View mRootView;
	
	private WheelVerticalView mWheelViewYear;
	private WheelVerticalView mWheelViewMonth;
	private WheelVerticalView mWheelViewDay;
	private WheelVerticalView mWheelViewHours;
	private WheelVerticalView mWheelViewMins;

	private boolean mHasSelectTime;
	private boolean mHasSelectmin;
    private boolean mHasSelectDay = true;
	private int mStartYear = 1900;
	private int mEndYear = 2100;

    private TextView mTvTtile;
    private Button mBtnCancel;
    private Button mBtnEnsure;

    private Dialog mSelectDialog;
    
    public CommonDatePickerWheelPanel(Context context) {
        mHasSelectTime = false;
        mHasSelectmin = false;
        init(context);
    }
    public CommonDatePickerWheelPanel(Context context, boolean hasSelectTime,boolean mHasSelectDay) {
        mHasSelectTime = hasSelectTime;
        this.mHasSelectDay = mHasSelectDay;
        init(context);
    }

    public void setEndYear(int endYear) {
        this.mEndYear = endYear;
    }

    public void setStartYear(int startYear) {
        this.mStartYear = startYear;
    }

    private void init(Context context) {
        mContext = context;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.widget_wheel_datatime_picker, null);
        mTvTtile = (TextView) mRootView.findViewById(R.id.tv_title);
        mWheelViewYear = (WheelVerticalView) mRootView.findViewById(R.id.year);
        mWheelViewMonth = (WheelVerticalView) mRootView.findViewById(R.id.month);
        mWheelViewDay = (WheelVerticalView) mRootView.findViewById(R.id.day);
        mWheelViewHours = (WheelVerticalView)mRootView.findViewById(R.id.hour);
        mWheelViewMins = (WheelVerticalView)mRootView.findViewById(R.id.min);
        
        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
//        int textSize = DensityUtil.sp2px(mContext, 16);
//        mWheelViewDay.TEXT_SIZE = textSize;
//        mWheelViewMonth.TEXT_SIZE = textSize;
//        mWheelViewYear.TEXT_SIZE = textSize;
//        mWheelViewHours.TEXT_SIZE = textSize;
//        mWheelViewMins.TEXT_SIZE = textSize;
    }
	
	/**
	 * 弹出日期时间选择器
	 * @param year
	 * @param month
	 * @param day
	 */
	public void initDateTimePicker(int year ,int month,int day){
		this.initDateTimePicker(year, month, day, 0, 0);
	}

    public void initDateTimePicker(int year ,int month){
        this.initDateTimePicker(year, month, 0, 0, 0);
    }
	
	/**
	 * 弹出日期时间选择器
	 * @param year
	 * @param month
	 * @param day
	 * @param h
	 * @param m
	 */
	public void initDateTimePicker(int year ,int month ,int day,int h,int m) {
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] monthsBig = { "1", "3", "5", "7", "8", "10", "12" };
		String[] monthsLittle = { "4", "6", "9", "11" };
		final List<String> listMonthBig = Arrays.asList(monthsBig);
		final List<String> listMonthLittle = Arrays.asList(monthsLittle);

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                wheel.setSelectedIndex(newValue);
                mWheelViewYear.setViewAdapter(mWheelViewYear.getViewAdapter());
                if(mHasSelectDay){
                    int year_num = newValue + mStartYear;
                    // 判断大小月及是否闰年,用来确定"日"的数据
                    if (listMonthBig.contains(String.valueOf(mWheelViewMonth.getCurrentItem() + 1))) {
                        mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 31, "日"));
                    } else if (listMonthLittle.contains(String.valueOf(mWheelViewMonth.getCurrentItem() + 1))) {
                        mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 30, "日"));
                    } else {
                        if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                            mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 29, "日"));
                        else
                            mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 28, "日"));
                    }
                    mWheelViewDay.setCurrentItem(0);
                }

            }

        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                wheel.setSelectedIndex(newValue);
                mWheelViewMonth.setViewAdapter(mWheelViewMonth.getViewAdapter());
                if(mHasSelectDay){
                    int month_num = newValue + 1;
                    // 判断大小月及是否闰年,用来确定"日"的数据
                    if (listMonthBig.contains(String.valueOf(month_num))) {
                        mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 31, "日"));
                    } else if (listMonthLittle.contains(String.valueOf(month_num))) {
                        mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 30, "日"));
                    } else {
                        int currentItem = mWheelViewYear.getCurrentItem();
                        if (((currentItem + mStartYear) % 4 == 0 && (currentItem + mStartYear) % 100 != 0) || (currentItem + mStartYear) % 400 == 0)
                            mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 29, "日"));
                        else
                            mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 28, "日"));
                    }
                    mWheelViewDay.setCurrentItem(0);
                }
            }
        };
        mWheelViewYear.addChangingListener(wheelListener_year);
        mWheelViewMonth.addChangingListener(wheelListener_month);
        mWheelViewDay.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                wheel.setSelectedIndex(newValue);
                mWheelViewDay.setViewAdapter(mWheelViewDay.getViewAdapter());}
        });

		// 年
		mWheelViewYear.setViewAdapter(getAdater(mContext, mStartYear, mEndYear,"年"));// 设置"年"的显示数据
		mWheelViewYear.setCyclic(false);// 可循环滚动
//		mWheelViewYear.setLabel("年");// 添加文字
		mWheelViewYear.setVisibleItems(5);
		mWheelViewYear.setCurrentItem(year - mStartYear);// 初始化时显示的数据
		// 月
		mWheelViewMonth.setViewAdapter(getAdater(mContext, 1, 12,"月"));
		mWheelViewMonth.setCyclic(true);
//		mWheelViewMonth.setLabel("月");
		mWheelViewMonth.setVisibleItems(5);
		mWheelViewMonth.setCurrentItem(month);

        if(mHasSelectDay){
            // 日
            mWheelViewDay.setCyclic(true);
            mWheelViewDay.setVisibleItems(5);
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (listMonthBig.contains(String.valueOf(month + 1))) {
                mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 31,"日"));
            } else if (listMonthLittle.contains(String.valueOf(month + 1))) {
                mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 30,"日"));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                    mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 29,"日"));
                else
                    mWheelViewDay.setViewAdapter(getAdater(mContext, 1, 28,"日"));
            }
    //		mWheelViewDay.setLabel("日");
            mWheelViewDay.setCurrentItem(day - 1);
        }else {
            mWheelViewDay.setVisibility(View.GONE);
        }
		
		if(mHasSelectTime){
			mWheelViewHours.setVisibility(View.VISIBLE);
			
			mWheelViewHours.setViewAdapter(getAdater(mContext, 0, 23,"时"));
			mWheelViewHours.setCyclic(true);// 可循环滚动
			mWheelViewHours.setVisibleItems(5);
//			mWheelViewHours.setLabel("时");// 添加文字
			mWheelViewHours.setCurrentItem(h);
			if(mHasSelectmin){
				mWheelViewMins.setVisibility(View.VISIBLE);
				mWheelViewMins.setViewAdapter(getAdater(mContext, 0, 59,"分"));
				mWheelViewMins.setCyclic(true);// 可循环滚动
				mWheelViewMins.setVisibleItems(5);
//				mWheelViewMins.setLabel("分");// 添加文字
				mWheelViewMins.setCurrentItem(m);
			}
			
		}else{
			mWheelViewHours.setVisibility(View.GONE);
			mWheelViewMins.setVisibility(View.GONE);
		}
	}
	
    public void setTitle(String title) {
        mTvTtile.setVisibility(View.VISIBLE);
        mTvTtile.setText(title);
    }

    public void setTitle(String title, OnClickListener listener) {
        setTitle(title);
        setEnsureClickListener(listener);
    }

    public void setCancelClickListener(OnClickListener listener) {
        mBtnCancel = (Button) mRootView.findViewById(R.id.btn_cancel);
        mBtnCancel.setVisibility(View.VISIBLE);
        mBtnCancel.setOnClickListener(listener);
    }

    public void setEnsureClickListener(OnClickListener listener) {
        mBtnEnsure = (Button) mRootView.findViewById(R.id.btn_ensure);
        mBtnEnsure.setVisibility(View.VISIBLE);
        mBtnEnsure.setOnClickListener(listener);
    }
    
    public void showDialog() {
        if (null == mSelectDialog) {
            mSelectDialog = new Dialog(mContext, R.style.DatetimePickerDialog);
            Window window = mSelectDialog.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = Float.valueOf(DensityUtil.getScreenWidth((Activity) mContext) * 0.9+"").intValue();
            layoutParams.height = LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
            window.setContentView(mRootView);
        }
        if (mBtnCancel == null) {
            setCancelClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    mSelectDialog.dismiss();
                }
            });
        }
        if (mBtnEnsure == null) {
            setEnsureClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    mSelectDialog.dismiss();
                }
            });
        }
        mSelectDialog.show();
    }
    
    public void dismissDialog() {
        if (null != mSelectDialog) {
            mSelectDialog.dismiss();
        }
    }
    
    public NumericLableWheelAdapter getAdater(Context context, int start, int end, String lable){
    	NumericLableWheelAdapter adapter = new NumericLableWheelAdapter(context, start, end);
    	adapter.setItemResource(R.layout.widget_wheel_item);
		adapter.setItemTextResource(R.id.name);
		adapter.setLable(lable);
		return adapter;
    }

    public String getDatetime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mWheelViewYear.getCurrentItem() + mStartYear);
        calendar.set(Calendar.MONTH, mWheelViewMonth.getCurrentItem());
        calendar.set(Calendar.DAY_OF_MONTH, mWheelViewDay.getCurrentItem() + 1);
        calendar.set(Calendar.HOUR_OF_DAY, mWheelViewHours.getCurrentItem());
        calendar.set(Calendar.MINUTE, mWheelViewMins.getCurrentItem());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return format.format(calendar.getTime());
    }
    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mWheelViewYear.getCurrentItem() + mStartYear);
        calendar.set(Calendar.MONTH, mWheelViewMonth.getCurrentItem());
        calendar.set(Calendar.DAY_OF_MONTH, mWheelViewDay.getCurrentItem() + 1);
        calendar.set(Calendar.HOUR_OF_DAY, mWheelViewHours.getCurrentItem());
        calendar.set(Calendar.MINUTE, mWheelViewMins.getCurrentItem());
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        return format.format(calendar.getTime());
    }
    
    public String getDateHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mWheelViewYear.getCurrentItem() + mStartYear);
        calendar.set(Calendar.MONTH, mWheelViewMonth.getCurrentItem());
        calendar.set(Calendar.DAY_OF_MONTH, mWheelViewDay.getCurrentItem() + 1);
        calendar.set(Calendar.HOUR_OF_DAY, mWheelViewHours.getCurrentItem());
        calendar.set(Calendar.MINUTE, mWheelViewMins.getCurrentItem());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH", Locale.getDefault());
        return format.format(calendar.getTime());
    }
}
