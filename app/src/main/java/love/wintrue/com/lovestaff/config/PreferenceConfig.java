package love.wintrue.com.lovestaff.config;

import android.content.Context;

import love.wintrue.com.lovestaff.bean.User;
import love.wintrue.com.lovestaff.utils.PrefersUtil;


/**
 * @des:
 * @author:th
 * @email:342592622@qq.com
 * @date: 2017/3/22 14:29
 */

public class PreferenceConfig {
    /** 应用唯一标识 */
    public static final String APP_UNIQUEID = "app_uniqueid";
    public static final String LOCATION = "location";
    public static final String CITY = "city";
    /** 用户信息 */
    public static final String USER = "user";
    /** 基础数据 */
    public static final String BASEDATA = "baseData";
    /** 付款选择 */
    public static final String POSITION = "position";
    /** 基础数据String */
    public static final String BASEDATASTRING = "position_string";
    /** 获取应用唯一标识号 */
    public static String getAppUniqueID(Context context) {
        return PrefersUtil.getInstance().getStrValue(APP_UNIQUEID);
    }

    /** 保存应用唯一标识号 */
    public static void saveAppUniqueId(Context context, String uniqueId) {
        PrefersUtil.getInstance().setValue(APP_UNIQUEID, uniqueId);
    }
    /** 获取基础数据 */
    public static String getBaseData(Context context) {
        return PrefersUtil.getInstance().getStrValue(BASEDATASTRING);
    }
    public static void setBaseData(Context context, String basedata) {
        PrefersUtil.getInstance().setValue(BASEDATASTRING, basedata);
    }

    public static void savePosition(Context context, int positon){
        PrefersUtil.getInstance().saveObject(POSITION, positon);
    }
    /**付款选择位置*/
    public static int readPosition(Context context){
        if(PrefersUtil.getInstance().readObject(POSITION) == null){
            return 0;
        }
        return (Integer) PrefersUtil.getInstance().readObject(POSITION);
    }

    public static void saveUser(Context context, User user){
        PrefersUtil.getInstance().saveObject(USER, user);
    }
    /**用户信息*/
    public static User readUser(Context context){
        if(PrefersUtil.getInstance().readObject(USER) == null){
            return null;
        }
        return (User)PrefersUtil.getInstance().readObject(USER);
    }
}
