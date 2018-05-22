package love.wintrue.com.lovestaff.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.config.AppConfig;


/**
 * @author th
 * @desc
 * @time 2017/3/20 13:51
 */
public class Util {
    /**
     * 是否连接网络
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        Pattern emailer = Pattern
                .compile("[_a-z\\d\\-\\./]+@[_a-z\\d\\-]+(\\.[_a-z\\d\\-]+)*"
                        + "(\\.(info|biz|com|edu|gov|net|am|bz|cn|cx|hk|jp|tw|vc|vn))$");
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email.toLowerCase()).matches();
    }

    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return
     */
    public static boolean isSpecialSymbol(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        for (char c : str.toCharArray()) {
            if (p.matcher(String.valueOf(c)).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判别手机是否为正确手机号码
     */
    public static boolean isValidMobileNumber(String mobileNumber) {
        Pattern pattern = Pattern.compile("^1\\d{10}$");
        Matcher matcher = pattern.matcher(mobileNumber);
        return matcher.matches();
    }

    public static boolean isValidveriNumber(String mobileNumber) {
        Pattern pattern = Pattern.compile("^1\\d{6}$");
        Matcher matcher = pattern.matcher(mobileNumber);
        return matcher.matches();
    }

    /**
     * 根据身份证号码判断是否为正确的身份证号码并返回身份证号码的生日
     *
     * @param idNumber
     * @return
     */
    public static String getBirthDayByIdNumber(String idNumber) {
        //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        //通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(idNumber);
        //判断用户输入是否为身份证号
        if (idNumMatcher.matches()) {
            //如果是，定义正则表达式提取出身份证中的出生日期
            Pattern birthDatePattern = Pattern.compile("\\d{6}(\\d{4})(\\d{2})(\\d{2}).*");//身份证上的前6位以及出生年月日
            //通过Pattern获得Matcher
            Matcher birthDateMather = birthDatePattern.matcher(idNumber);
            //通过Matcher获得用户的出生年月日
            if (birthDateMather.find()) {
                String year = birthDateMather.group(1);
                String month = birthDateMather.group(2);
                String date = birthDateMather.group(3);
                //输出用户的出生年月日
                String birthDay = year + "-" + month + "-" + date;
                if (DateUtil.strToDateShort(birthDay).after(new Date())) {
                    return null;
                }
                return birthDay;
            }
        }
        return null;
    }

    /**
     * 判断是否为正确的身份证号码
     *
     * @param idNumber
     * @return
     */
    public static boolean isValidIdNumber(String idNumber) {
        //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        //通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(idNumber);
        //判断用户输入是否为身份证号
        if (idNumMatcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 拨打电话
     *
     * @param activity
     * @param phone
     */
    public static void callPhone(Activity activity, String phone) {
        try {
            Uri uri = Uri.parse("tel:" + phone);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Uri uri = Uri.parse("tel:" + phone);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    activity.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 验证是否是有效的验证码
     */
    public static boolean IsValidVerify(String verify) {
        // Pattern pattern = Pattern.compile("\\d{4}");
        // Matcher matcher = pattern.matcher(verify);
        if (verify.length() < 1) {
            return false;
        }
        return true;
    }

    /**
     * 发送短信
     *
     * @param activity
     * @param phone
     * @param msg
     */
    public static void sendMessage(Activity activity, String phone, String msg) {
        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", msg);
        activity.startActivity(intent);
    }

    public static String formatMoney(double amount,boolean showY) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("##,##0.00");
        return showY?"¥"+df.format(amount):df.format(amount);
    }

    /**
     * 将传入的金额（单位分）格式化为元，保留两位小数 格式如：5.00
     *
     * @param s
     * @return
     */
    public static String formatMoneyNoUnit(String s,boolean showY) {
        if (TextUtils.isEmpty(s)) {
            return showY?"¥0.00":"0.00";
        }
        try {
            double num = Double.parseDouble(s);
            DecimalFormat formater = new DecimalFormat("###,##0.00");
            String result = formater.format(num / 100.0d);
            return showY?"¥"+result:result;
        } catch (Exception e) {
            return showY?"¥0.00":"0.00";
        }
    }

    /**
     * 将传入的金额（单位分）格式化为元，保留两位小数 格式如：5.00
     *
     * @param
     * @return
     */
    public static String formatMoneyNoUnitTwo(String s,boolean showY) {
        if (TextUtils.isEmpty(s)) {
            return showY?"¥0.00":"0.00";
        }
        try {
            double num = Double.parseDouble(s);
            DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
            df.applyPattern("0.00");
            String result = df.format(num);
            return showY?"¥"+result:result;
        } catch (Exception e) {
            return showY?"¥0.00":"0.00";
        }

    }

    public static String formatMoneyNoUnitTwo(double num,boolean showY) {
        if (num == 0d) {
            return showY?"¥0.00":"0.00";
        }
        try {
            DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
            df.applyPattern("0.00");
            String result = df.format(num);
            return showY?"¥"+result:result;
        } catch (Exception e) {
            return showY?"¥0.00":"0.00";
        }

    }

    public static String formatMoneyNoUnitOne(String s,boolean showY) {
        if (TextUtils.isEmpty(s)) {
            return showY?"¥0.00":"0.00";
        }
        try {
            double num = Double.parseDouble(s);
            DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
            df.applyPattern("0.0");
            String result = df.format(num);
            return showY?"¥"+result:result;
        } catch (Exception e) {
            return showY?"¥0.00":"0.00";
        }

    }

    /**
     * 格式化金额
     *
     * @param s
     * @param len
     * @return
     */
    public static String formatMoney(String s, int len) {
        if (s == null || s.length() < 1) {
            return "";
        }
        NumberFormat formater = null;
        double num = 0;
        try {
            num = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            num = 0;
        }

        if (len == 0) {
            formater = new DecimalFormat("###,###");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,##0.");
            for (int i = 0; i < len; i++) {
                buff.append("0");
            }
            formater = new DecimalFormat(buff.toString());
        }
        String result = formater.format(num);
        if (result.indexOf(".") == -1) {
            result = result + ".00";
        }
        return result;
    }

    public static String getPngName(String prefix) {
        return prefix + ".png";
    }

    public static String createImagePath(Context context) {
        String fileName = Util.getPngName(DateUtil
                .getStringDate("yyyyMMddHHmmssSSS"));
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file;
            try {
                file = FileUtil.createSDFile(AppConfig.PICTURE_PATH, fileName);
                return file.getPath();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "您已拒绝读写手机存储功能权限，为了您更好的体验，请到应用管理权限中心设置相关权限", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            File file = new File(context.getCacheDir().getPath(), fileName);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            LogUtil.e("path:", file.getPath() + "");
            return file.getPath();
        }
        return null;
    }

    /**
     * 手机号中间4位替换为*号
     *
     * @param phone
     * @return
     */
    public static String phoneEncryption(String phone) {
        String str = "";
        if (TextUtils.isEmpty(phone)) {
            return str;
        }
        if (phone.length() != 11) {
            return phone;
        }
        for (int i = 0; i < phone.length(); i++) {
            if (i > 2 && i < 7) {
                str += "*";
            } else {
                str += phone.charAt(i);
            }
        }
        return str;
    }

    public static String getValueEncoded(String value) {
        if (value == null) return "null";
        String newValue = value.replace("\n", "");
        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                try {
                    return URLEncoder.encode(newValue, "UTF-8");
                } catch (Exception e) {

                }
            }
        }
        return newValue;
    }

    /**
     * 判断字符是否为数字和字母的组合
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
                continue;
            }
            if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
                continue;
            }
            return false;
        }
        if (isDigit && isLetter) {
            return true;
        }
        return false;
    }

    /**
     * 变量class的变量名和值
     *
     * @param object
     */
    public static Map<String, Object> getFiledAndValue(Object object) {
        Map<String, Object> map = new HashMap<>();
        for (Field filed : object.getClass().getDeclaredFields()) {
            String fieldName = filed.getName();
            LogUtil.e("name:" + fieldName);
            String methodGetter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                Method m = object.getClass().getMethod(methodGetter);
                // 调用getter方法获取属性值
                LogUtil.e("value:" + m.invoke(object));
                if (m.invoke(object) != null && !TextUtils.isEmpty(m.invoke(object).toString())) {
                    map.put(fieldName, m.invoke(object));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static float getTextWidth(String text, int textSize) {
        TextPaint paint = new TextPaint();
        paint.setTextSize(textSize);
        return paint.measureText(text);
    }

    public static String getSign(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                list.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                sb.append(arrayToSort[i]);
            } else {
                sb.append("&" + arrayToSort[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 根据name获取AndroidManifest文件里meta-data的value值
     *
     * @param context
     * @param name
     * @return
     */
    public static String getMetaValueByName(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString(name);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm.isScreenOn()) {
            return true;
        }
        return false;
    }

    // 隐藏系统键盘
    public static void hideSoftInputMethod(Activity activity, EditText ed) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            ed.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            } catch (NoSuchMethodException e) {
                ed.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //此方法只是关闭系统软键盘
    public static void hideSoftInput(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && context.getCurrentFocus() != null) {
            if (context.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void showSoftInput(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.SHOW_FORCED);
    }

    public static void setPlaceHolder(Context context,int resId,SimpleDraweeView draweeView){
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        int mResId = R.drawable.bg_jz;
        if(resId!=0){
            mResId = resId;
        }
        GenericDraweeHierarchy hierarchy = builder
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                .setPlaceholderImage(mResId).build();
        draweeView.setHierarchy(hierarchy);
    }
}
