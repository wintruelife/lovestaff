package love.wintrue.com.lovestaff.observer;

import java.io.Serializable;

/**
 * Created by Administrator on 13-8-30.
 */
public interface Observer extends Serializable {
    public void notifyChanged(String action, Object object);
}
