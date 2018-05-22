package love.wintrue.com.lovestaff.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import java.io.File;
import java.util.UUID;

/**
 * Created by dell on 2017/5/8.
 */

public class AndroidUtil {

    private final static String DEFAULT_IMEI = "000000000000000";

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeightByContext(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (getAndroidSDKCode() >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point outSize = new Point();
            wm.getDefaultDisplay().getSize(outSize);
            return outSize.y;
        } else {
            return wm.getDefaultDisplay().getHeight();
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidthByContext(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (getAndroidSDKCode() >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point outSize = new Point();
            wm.getDefaultDisplay().getSize(outSize);
            return outSize.x;
        } else {
            return wm.getDefaultDisplay().getWidth();
        }
    }

    /**
     * 获取当前SDK版本号
     *
     * @return
     */
    public static int getAndroidSDKCode() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取当前SDK名称
     *
     * @return
     */
    public static String getAndroidSDKName() {
        return android.os.Build.VERSION.SDK;
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = tm.getDeviceId();
        if (StringUtils.isNotBlank(imei) && !imei.equals(DEFAULT_IMEI)) {
            return imei;
        }
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (StringUtils.isNotBlank(androidId) && !"9774d56d682e549c".equals(androidId)) {
            return androidId;
        }
        return UUID.randomUUID().toString();
    }

    // 计算出该TextView中文字的长度(像素)
    public static float getTextViewLength(Paint textPaint, String text){
        float textLength = textPaint.measureText(text);
        return textLength;
    }

    /**
     * 判断手机是否ROOT
     */
    public static boolean isRoot() {
        boolean root = false;

        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }

        } catch (Exception e) {
        }

        return root;
    }

    public static int dp2px(Context context, float dp) {
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * dp);
    }
}
