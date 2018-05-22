package love.wintrue.com.lovestaff.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;


/**
 * 
 * @ClassName: MyBaseAdapter
 * @Description: 基础适配类
 * @param <T>
 */
public abstract class MyBaseAdapter<T> extends CommonBaseAdapter<T> {

	public MyBaseAdapter(Context context) {
		super(context);
	}
	
	public MyBaseAdapter(Context context, List<T> list) {
		super(context);
		setList(list);
	}
	
	public LayoutInflater getLayoutInflater() {
		return mLayoutInflater;
	}

}
