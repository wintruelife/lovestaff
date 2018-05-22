package love.wintrue.com.lovestaff.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Set;

import love.wintrue.com.lovestaff.base.MApplication;
import love.wintrue.com.lovestaff.config.AppConfig;

public class PrefersUtil {

    public static final String PREFERENCES_FILE = AppConfig.APP_NAME;

    private static PrefersUtil PREFERSUTIL;
    private static SharedPreferences cache;
    private Editor editor;

    private PrefersUtil(Context fra_act) {
        cache = fra_act.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        editor = cache.edit();
    }

    public static PrefersUtil getInstance() {
        if (PREFERSUTIL == null) {
            PREFERSUTIL = new PrefersUtil(MApplication.getInstance());
        }
        return PREFERSUTIL;
    }

    public String getStrValue(String key) {
        if(!TextUtils.isEmpty(cache.getString(MD5.md5(key), ""))) {
            String desValue;
            try {
                desValue = Des3.des3DecodeCBC(MD5.md5(key).substring(4, 32), "19283127", cache.getString(MD5.md5(key), ""));
                return desValue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setValue(String key, String value) {
        try {
            String desValue = Des3.des3EncodeCBC(MD5.md5(key).substring(4,32),"19283127",value);
            editor.putString(MD5.md5(key), desValue);
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setFolatValue(String key, float value) {
        editor.putFloat(MD5.md5(key), value);
        editor.commit();
    }

    public float getFloatValue(String key) {
        return cache.getFloat(MD5.md5(key), 0);
    }

    public void setLongValue(String key, long value) {
        editor.putLong(MD5.md5(key), value);
        editor.commit();
    }

    public long getLongValue(String key) {
        return cache.getLong(MD5.md5(key), 0);
    }

    public void removeSpf(String key) {
        cache = MApplication.getInstance().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        cache.edit().remove(key).commit();
    }

    public int getIntValue(String key, int defaultInt) {
        return cache.getInt(MD5.md5(key), defaultInt);
    }

    public void setValue(String key, int value) {
        editor.putInt(MD5.md5(key), value);
        editor.commit();
    }

    public Boolean getBooleanValue(String key, Boolean defaultBoolean) {
        return cache.getBoolean(MD5.md5(key), defaultBoolean);
    }

    public void setValue(String key, Boolean value) {
        editor.putBoolean(MD5.md5(key), value);
        editor.commit();
    }


    public void clearData() {
        editor.clear();
        editor.commit();
    }

    @SuppressLint("NewApi")
    public static void saveStringList(Context context, Set<String> value, String key) {
        cache = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        Editor editor = cache.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    @SuppressLint("NewApi")
    public static Set<String> getSavedStringList(Context context, String key) {
        cache = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return cache.getStringSet(key, null);
    }

    public void saveObject(String key, Object object) {
        // 创建字节输出流  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流  
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流  
            oos.writeObject(object);
            // 将字节流编码成base64的字符窜  
            String userBase64 = new String(Base64.encode(baos.toByteArray()));
            editor.putString(MD5.md5(key), userBase64);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public Object readObject(String key) {
        Object object = null;
        try {
            String userBase64 = cache.getString(MD5.md5(key), "");
            //读取字节
            byte[] base64 = Base64.decode(userBase64);

            //封装到字节流
            ByteArrayInputStream bais = new ByteArrayInputStream(base64);

            //再次封装  
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象  
                object = bis.readObject();
            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return object;
    }
}
