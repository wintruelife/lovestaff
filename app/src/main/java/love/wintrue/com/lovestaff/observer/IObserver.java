package love.wintrue.com.lovestaff.observer;

/**
 * Created by Administrator on 13-8-30.
 */
public interface IObserver {
    public void registerObserver(Observer observer, String filter);

    public void unregisterObserver(Observer observer);

    public void notifyDataChanged(Object object, String filter);

    public void unregisterAllObserver();
}
