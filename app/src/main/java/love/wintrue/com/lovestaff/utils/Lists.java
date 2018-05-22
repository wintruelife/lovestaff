package love.wintrue.com.lovestaff.utils;

/**
 * Created by a1314 on 17/6/13.
 */
import java.util.List;

public class Lists {
    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean notEmpty(List list) {
        return list != null && list.size() > 0;
    }

    public static boolean hasPosition(List list,int position){
        if(isEmpty(list) || position<0 || position>=list.size() || list.get(position)==null){
            return false;
        }
        return true;
    }
}

