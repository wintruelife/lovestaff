package love.wintrue.com.lovestaff.observer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 带行为的观察者模式
 */
public class SubObserver implements IObserver {
	public static final int MSG_WHAT = 0;
	private static SubObserver ourInstance = new SubObserver();
	private HashMap<String, ArrayList<Observer>> observers = new HashMap<String, ArrayList<Observer>>();

	public static SubObserver getInstance() {
		return ourInstance;
	}

	private SubObserver() {
		
	}

	// 注册
	@Override
	public void registerObserver(Observer observer, String filter) {
		if (observer == null) {
			return;
		}
		synchronized (observers) {
			if (!contains(observer, filter)) {
				ArrayList<Observer> list;
				if (observers.containsKey(filter)) {
					list = observers.get(filter);
					list.add(observer);
				} else {
					list = new ArrayList<Observer>();
					list.add(observer);
					observers.put(filter, list);
				}
			} else {
			}
		}
	}

	@Override
	public void unregisterObserver(Observer observer) {
		synchronized (observers) {
			ArrayList<ArrayList<Observer>> temp = new ArrayList<ArrayList<Observer>>();
			for (String key : observers.keySet()) {
				ArrayList<Observer> list = observers.get(key);
				for (Observer observer2 : list) {
					if (observer2.equals(observer)) {
						temp.add(list);
						break;
					}
				}
			}
			for (ArrayList<Observer> arrayList : temp) {
				arrayList.remove(observer);
			}
		}
	}



	@Override
	public void notifyDataChanged(Object object, String filter) {
		synchronized (observers) {
			if (!observers.containsKey(filter))
				return;
			ArrayList<Observer> list = observers.get(filter);
			for (Observer observer : list) {
				Message msg = new Message();
				msg.what = MSG_WHAT;
				Bundle b = new Bundle();
				b.putSerializable("observer", observer);
				msg.obj = new ObsNotifyObj(filter, object);
				msg.setData(b);
				handler.sendMessage(msg);
			}
		}
	}

	@Override
	public void unregisterAllObserver() {
		synchronized (observers) {
			observers.clear();
		}
	}

	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_WHAT:
				Bundle b = msg.getData();
				Observer observer = (Observer) b.getSerializable("observer");
				ObsNotifyObj obj = (ObsNotifyObj) msg.obj;
				observer.notifyChanged(obj.filter, obj.obj);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * @param observer
	 * @return
	 */
	private boolean contains(Observer observer, String filter) {
		if (observers.containsKey(filter)) {
			ArrayList<Observer> observerList = observers.get(filter);
			if (observerList != null && observerList.size() > 0)
				for (Observer observer2 : observerList) {
					if (observer2.equals(observer)) {
						return true;
					}
				}
		}
		return false;
	}
}
