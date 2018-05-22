package love.wintrue.com.lovestaff.base.adapter;

import java.util.List;

/**
 * 泛型接口 - 通用Adapter操作方法
 * 
 * @ClassName: CommonAdapterOptible.java
 */
public interface CommonAdapterOptible<T> {

	/**
	 * 设置列表数据
	 * 
	 * @param list
	 */
	void setList(List<T> list);

	/**
	 * 添加列表数据
	 * 
	 * @param list
	 */
	void addList(List<T> list);

	/**
	 * 移除一个列表数据
	 * 
	 * @param t
	 */
	void removeItem(int position);
}
