package love.wintrue.com.lovestaff.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import love.wintrue.com.lovestaff.base.MApplication;
import love.wintrue.com.lovestaff.config.AppConfig;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUtil {

    public static final String ENCODING_TYPE = "UTF-8";

    public static String getSDPath() {
        // 得到当前外部存储设备的目录( /SDCARD )
        return Environment.getExternalStorageDirectory() + "/";
    }

    /**
     * 检查文件是否存在
     *
     * @param fileName
     * @param isCreate
     * @return
     * @throws IOException
     */
    public static boolean isExist(String fileName, boolean isCreate)
            throws IOException {
        File file = new File(fileName);
        boolean ret = file.exists();
        if (!ret && isCreate) {
            file.createNewFile();
        }
        return ret;
    }

    /**
     * 删除文件
     *
     * @param fileName
     */
    public static void deteleFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 创建文件
     *
     * @param fileName
     * @throws IOException
     */
    public static void createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    /**
     * 读数据
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static byte[] read(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        FileInputStream is = null;
        try {
            is = new FileInputStream(fileName);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                is.close();
            }
        }
        return null;
    }

    /**
     * 写入数据
     *
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static void write(String fileName, byte[] data) throws IOException {
        FileOutputStream os = null;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            os = new FileOutputStream(fileName);
            os.write(data);
            os.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                os.close();
            }
        }
    }

    /**
     * 在SD卡上创建文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File createSDFile(String fileName) throws IOException {
        String path = getSDPath() + fileName;
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile;
    }


    /**
     * 在SD卡上创建目录和文件
     *
     * @param dir
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File createSDFile(String dir, String fileName)
            throws IOException {
        String path = getSDPath() + dir;
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(path, fileName);
        file.createNewFile();
        return file;
    }

    public static String getSDFilePath(String dir, String fileName){
        return getSDPath() + dir + fileName;
    }
    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     * @return
     */
    public static File createSDDir(String dirName) {
        File dir = new File(getSDPath() + dirName);
        dir.mkdir();
        return dir;
    }

    public static void saveBitmap(String dirName, String fileName, Bitmap mBitmap) {
        if (null == mBitmap) {
            return;
        }
        File tempFile;
        try {
            tempFile = FileUtil.createSDFile(dirName, fileName);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            ImageUtil.saveBitmap(tempFile.getPath(), mBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断SD卡上的文件夹是否存在
     *
     * @param fileName
     * @return
     */
    public boolean isFileExist(String fileName) {
        File file = new File(getSDPath() + fileName);
        return file.exists();
    }

    /**
     * 判断SD卡上的文件是否存在
     *
     * @param (url + fileName)
     * @return
     */
    public static boolean isFileExists(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        boolean isexit = true;
        try {
            File f = new File(url);
            if (!f.exists()) {
                isexit = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            isexit = false;
        }
        return isexit;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     *
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public static File write2SDFromInput(String path, String fileName,
                                         InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = createSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * 将一个字符串写入到SD卡中
     *
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public static File write2SDFromInput(String path, String fileName,
                                         String input) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = createSDFile(path + fileName);
            output = new FileOutputStream(file);
            output.write(input.getBytes());
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 从raw读取文本内容
     *
     * @param context
     * @param id
     * @return
     */
    public static String getFromRaw(Context context, int id) {
        StringBuffer result = new StringBuffer();
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getResources()
                    .openRawResource(id), "UTF-8");
            bufReader = new BufferedReader(inputReader);
            String lineTxt = null;
            while ((lineTxt = bufReader.readLine()) != null)
                result.append(lineTxt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputReader != null) {
                    inputReader.close();
                }
                if (bufReader != null) {
                    bufReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString().trim();
    }

    /**
     * 从assets读取文本内容
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName) {
        StringBuffer result = new StringBuffer();
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getResources()
                    .getAssets().open(fileName), "UTF-8");
            bufReader = new BufferedReader(inputReader);
            String lineTxt = null;
            while ((lineTxt = bufReader.readLine()) != null)
                result.append(lineTxt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputReader != null) {
                    inputReader.close();
                }
                if (bufReader != null) {
                    bufReader.close();
                }
            } catch (IOException e) {
//				e.printStackTrace();
            }
        }
        return result.toString().trim();
    }

    /**
     * 序列化文件到硬盘
     *
     * @param context
     * @param name
     * @param obj
     */
    public static void saveFile(Context context, String name, Object obj) {
        if (context == null)
            return;
        File root = context.getFilesDir();
        File file = new File(root, name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            fos.close();
            oos.close();
        } catch (Exception e) {
//			e.printStackTrace();
        }
    }

    /**
     * 读取本地序列化文件
     *
     * @param context
     * @param name
     * @return
     */
    public static Object readFile(Context context, String name) {
        File root = context.getFilesDir();
        File file = new File(root, name);
        FileInputStream fis = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
                return null;
            }
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            fis.close();
            ois.close();
            return obj;
        } catch (Exception e) {
//			e.printStackTrace();
            return null;
        }finally {
            try {
                if(null!=fis){
                    fis.close();
                    fis = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object readFileFromAssets(Context context, String name) {
        try {

            InputStream fis = context.getAssets().open(name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            fis.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double getDirSize(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {// 如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
//            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    /**
     * Uri变成File对象
     *
     * @param originalUri
     * @return
     */
    @SuppressWarnings("deprecation")
    public static File changeUriToFile(Activity activity, Uri originalUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = activity.managedQuery(originalUri, proj,
                null, null, null);
        int index = actualimagecursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String imgPath = actualimagecursor.getString(index);
        File originalFile = new File(imgPath);
        return originalFile;
    }

    /**
     * file转byte
     *
     * @param filePath
     * @return
     */
    public static byte[] file2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * byte转file
     *
     * @param buf
     * @param file
     */
    public static void byte2File(byte[] buf, File file) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载网络文件到本地指定位置
     *
     * @param FileUrl  网络文件地址
     * @param filePath 下载到的本地指定地址
     * @return string 下载到的本地指定地址
     */
    public static String getDownloadFile(String FileUrl, String filePath) {
        File file = new File(filePath);
        //如果目标文件已经存在，则删除。产生覆盖旧文件的效果
        if (file.exists()) {
            file.delete();
        }
        try {
            // 构造URL
            URL url = new URL(FileUrl);
            // 打开连接
            URLConnection con = url.openConnection();
            //获得文件的长度
            int contentLength = con.getContentLength();
            LogUtil.e("长度 :" + contentLength);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(filePath);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            return null;
        }
        return filePath;
    }

    /**
     * 用第三方应用打开本地文件
     *
     * @param filePath 文件本地地址
     * @param type     文件类型  application/pdf(pdf文件) text/html(html文件) image/*(图片文件) text/plain(文本文件) application/x-chm(chm文件)
     * @return intent
     */
    public static Intent openFile(String filePath, String type) {
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return null;
        } else {
            Intent intent = new Intent();
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(file), type);
            return intent;
        }
    }

    /**
     * 上传文件
     * @param fileName 文件名称
     * @param file 文件
     * @param handler 上传文件监听handler 1上传成功,并且返回url在obj里面 0上传失败
     * @param tag 上传文件tag标记 在handdler的arg1里面
     */
    public static void uploadFile(String fileName, File file, final Handler handler,final int tag) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pic", file.getName(), requestFile);
        Call<ResponseBody> call = MApplication.getInstance().iniApiService(180).uploadFile(getHeaders(),fileName, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Message msg = handler.obtainMessage();
                if(response.code() == 200) {
                    try {
                        String data = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(data);
                        String code = jsonObject.getString("code");
                        if (code.endsWith("000000")) {
                            JSONObject resultMap = jsonObject.getJSONObject("resultMap");
                            msg.what = 1;
                            msg.obj = resultMap.optString("url");
                            msg.arg1 = tag;
                        } else {
                            msg.what = 0;
                            msg.obj = jsonObject.optString("description");
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        msg.obj=e.getMessage();
                    }
                }else{
                    try {
                        msg.obj = response.errorBody().string();
                        msg.what = 0;
                    }catch (Exception e){
                        e.printStackTrace();
                        msg.obj=e.getMessage();
                    }
                }
                msg.sendToTarget();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Message msg = handler.obtainMessage();
                try{
                    msg.obj = t.getMessage();
                }catch (Exception e){
                    msg.obj = "网络请求超时";
                }
                msg.what = 0;
                msg.sendToTarget();
            }
        });

    }

    private static Map<String,Object> getHeaders(){
        String date = DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> mDataTemps = new HashMap<>();
        String requestId = UUID.randomUUID().toString();
        params.put("token", TextUtils.isEmpty(MApplication.getInstance().getUser().getToken()) ? "" : MApplication.getInstance().getUser().getToken());
        params.put("passed", "1");
        params.put("passed_type", "3DES");
        params.put("dev", MApplication.getInstance().getDeviceInfo());
        params.put("sign_type", "MD5");
        params.put("did", MApplication.getInstance().getAppUniqueID());
        params.put("versionCode", MApplication.getInstance().getVersionCode());
        params.put("version", MApplication.getInstance().getVersion());
//        params.put("net", NetworkStateReceiver.getNetworkType(mContext));
        params.put("timestamp", date);
        params.put("sign", MD5.MD5Encode(Util.getSign(mDataTemps)));
        params.put("Accept", "application/json");
        params.put("apptype", "1");
        params.put("ostype", "1");
        return params;
    }

    /**
     * 下载文件
     *
     * @param fileUrl
     * @param path
     * @param fileName
     */
    public static void downLoadFile(String fileUrl, final String path, final String fileName) {
        Call<ResponseBody> call = MApplication.getInstance().getApiService().downloadFileWithDynamicUrlSync(fileUrl);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    InputStream inputStream = response.body().byteStream();
                    write2SDFromInput(path, fileName, inputStream);
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public static File createFile(Context context, String fileName) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file;
            try {
                file = FileUtil.createSDFile(AppConfig.FILE_ROOT_URL, fileName);
                LogUtil.e("path:", file.getPath() + "");
                return file;
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
            return file;
        }
        return null;
    }

    /**
     * 打开APK
     *
     * @param context
     * @param filePath
     */
    public static void openApk(Context context, String filePath) {
        if (filePath != null) {
            if (filePath.endsWith(".apk")) {
                if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
                    File file = new File(filePath);
                    Uri apkUri = FileProvider.getUriForFile(context, "com.ytsh.finance.fileprovider", file);//在AndroidManifest中的android:authorities值
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                    install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    context.startActivity(install);
                } else {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(install);
                }
                MApplication.getInstance().exit();
            }
        }
    }

    public static Uri getImageUri(Context context,File file) {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if(currentapiVersion < 24) {
            return Uri.fromFile(file);
        }else{
            return FileProvider.getUriForFile(context, "com.ytsh.finance.fileprovider", file);
        }
    }
}
