/**
 * Copyright 2006～2014. Chengdu Sicent Technology Co.,Ltd all rights reserved 
 */
package love.wintrue.com.lovestaff.base.adapter;

import android.content.Context;
import android.widget.BaseAdapter;


import java.util.ArrayList;
import java.util.List;

import love.wintrue.com.lovestaff.bean.BaseBean;

/**
 * BaseAdapter模版类
 * 
 * @author hanzhiming
 * @data 2014-3-27
 */
public abstract class FFBaseAdapter<T extends BaseBean> extends BaseAdapter {

	protected List<T> list;
	protected Context context;

	public FFBaseAdapter(Context context, List<T> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		int size = getCount();
		if (size == 0 || position >= size) {
			return null;
		}
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	public List<? extends BaseBean> getList() {
		return this.list;
	}

	@SuppressWarnings("unchecked")
	public void setList(List<? extends BaseBean> mlist) {
		list = (List<T>) mlist;
	}


	public void addList(List<? extends BaseBean> mlist) {
		if(mlist != null) {
            if(list == null){
                list = new ArrayList<T>();
            }

			this.list.addAll((List<T>) mlist);
			int n = list.size();
		}
	}
}
