package love.wintrue.com.lovestaff.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import love.wintrue.com.lovestaff.base.MApplication;
import love.wintrue.com.lovestaff.receiver.NetworkStateReceiver;
import love.wintrue.com.lovestaff.utils.DateUtil;

/**
 * Created by lhe on 2017/8/14.
 */

public class HttpTaskUtil {

    public static void addGlobParams(Map<String, Object> params,Map<String, Object> headerData, Context mContext) {
        String date = DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        String requestId = UUID.randomUUID().toString();
        params.put("token", TextUtils.isEmpty(MApplication.getInstance().getUser().getToken()) ? "" : MApplication.getInstance().getUser().getToken());
        params.put("passed", "1");
        params.put("passed_type", "3DES");
        params.put("dev", MApplication.getInstance().getDeviceInfo());
        params.put("sign_type", "MD5");
        params.put("did", MApplication.getInstance().getAppUniqueID());
        params.put("versionCode", MApplication.getInstance().getVersionCode());
        params.put("version", MApplication.getInstance().getVersion());
        params.put("net", NetworkStateReceiver.getNetworkType(mContext));
        params.put("timestamp", date);
        params.put("Accept", "application/json");
        params.put("apptype", "1");
        params.put("ostype", "1");
        params.put("appid","10001");
        params.put("requestid",requestId);

        String sig =   BaseInterface3.groupParams(headerData, BaseInterface3.Come_App_Secret);
        Log.d("hzm","sig = "+sig);

        params.put("sig",sig);
    }
}
