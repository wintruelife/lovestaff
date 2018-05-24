package love.wintrue.com.lovestaff.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import love.wintrue.com.lovestaff.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ImageUtil {

    private static float[] carray = new float[20];

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return compressForQuality(BitmapFactory.decodeStream(is, null, opt));
    }

    /**
     * 以最省内存的方式读取本地资源的图片 或者SDCard中的图片
     *
     * @param imagePath  图片在SDCard中的路径
     * @return
     */
    public static Bitmap getSDCardImg(String imagePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        return BitmapFactory.decodeFile(imagePath, opt);
    }

    /**
     * 编辑图片大小，保持图片不变形。
     *
     * @param sourceBitmap
     * @param resetWidth
     * @param resetHeight
     * @return
     */
    public static Bitmap resetImage(Bitmap sourceBitmap, int resetWidth,
                                    int resetHeight) {
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        int tmpWidth;
        int tmpHeight;
        float scaleWidth = (float) resetWidth / (float) width;
        float scaleHeight = (float) resetHeight / (float) height;
        float maxTmpScale = scaleWidth >= scaleHeight ? scaleWidth
                : scaleHeight;
        // 保持不变形
        tmpWidth = (int) (maxTmpScale * width);
        tmpHeight = (int) (maxTmpScale * height);
        Matrix m = new Matrix();
        m.setScale(maxTmpScale, maxTmpScale, tmpWidth, tmpHeight);
        sourceBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, width, height,
                m, false);
        // 切图
        int x = (tmpWidth - resetWidth) / 2;
        int y = (tmpHeight - resetHeight) / 2;
        return Bitmap.createBitmap(sourceBitmap, x, y, resetWidth, resetHeight);
    }

    /**
     * 获取本地图片并指定高度和宽度
     *
     * @param imagePath
     * @return
     */
    public static Bitmap getNativeImage(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap myBitmap = BitmapFactory.decodeFile(imagePath, options); // 此时返回myBitmap为空
        // 计算缩放比
        int be = (int) (options.outHeight / (float) 1000);
        int ys = options.outHeight % 1000;// 求余数
        float fe = ys / (float) 1000;
        if (fe >= 0.5)
            be = be + 1;
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false;
        myBitmap = BitmapFactory.decodeFile(imagePath, options);
        if (myBitmap != null) {
            return compressForQuality(myBitmap);
        }
        return null;
    }

    /**
     * 代码创建一个selector 代码生成会清除padding
     */
    public static Drawable CreateStateDrawable(Context context, int bulr,
                                               int focus) {
        Drawable bulrDrawable = context.getResources().getDrawable(bulr);
        Drawable focusDrawable = context.getResources().getDrawable(focus);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed},
                focusDrawable);
        drawable.addState(new int[]{}, bulrDrawable);
        return drawable;
    }

    public static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        if (bitmap == null) {
            return null;
        }
        return compressForQuality(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩方法：
     *
     * @param image
     * @return
     */
    public static Bitmap compressForQuality(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        if (baos.toByteArray().length / 1024 > 300) {// 判断如果图片大于300k,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩50%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        LogUtil.e("质量压缩："+options);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = 1;// 设置缩放比例
        newOpts.inPreferredConfig = Config.RGB_565;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressForQuality(Bitmap image,int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        if (baos.toByteArray().length / 1024 > quality) {// 判断如果图片大于300k,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩50%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        LogUtil.e("质量压缩："+options);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = 1;// 设置缩放比例
        newOpts.inPreferredConfig = Config.RGB_565;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressForScale(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 2048) {// 判断如果图片大于2M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1280;// 这里设置高度为800f
        float ww = 720;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w >= h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPreferredConfig = Config.RGB_565;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressForScale(Bitmap image,float width,float height) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        if (baos.toByteArray().length / 1024 > 300) {// 判断如果图片大于300k,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩50%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        LogUtil.e("质量压缩："+options);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = width;// 这里设置高度为800f
        float ww = height;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPreferredConfig = Config.RGB_565;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {

        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        paint.setColorFilter(null);
        c.drawBitmap(bmpGrayscale, 0, 0, paint);
        ColorMatrix cm = new ColorMatrix();
        getValueBlackAndWhite();
        cm.set(carray);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public static void getValueSaturation() {
        // 高饱和度
        carray[0] = 5;
        carray[1] = 0;
        carray[2] = 0;
        carray[3] = 0;
        carray[4] = -254;
        carray[5] = 0;
        carray[6] = 5;
        carray[7] = 0;
        carray[8] = 0;
        carray[9] = -254;
        carray[10] = 0;
        carray[11] = 0;
        carray[12] = 5;
        carray[13] = 0;
        carray[14] = -254;
        carray[15] = 0;
        carray[16] = 0;
        carray[17] = 0;
        carray[18] = 5;
        carray[19] = -254;

    }

    private static void getValueBlackAndWhite() {
        // 黑白
        carray[0] = (float) 0.308;
        carray[1] = (float) 0.609;
        carray[2] = (float) 0.082;
        carray[3] = 0;
        carray[4] = 0;
        carray[5] = (float) 0.308;
        carray[6] = (float) 0.609;
        carray[7] = (float) 0.082;
        carray[8] = 0;
        carray[9] = 0;
        carray[10] = (float) 0.308;
        carray[11] = (float) 0.609;
        carray[12] = (float) 0.082;
        carray[13] = 0;
        carray[14] = 0;
        carray[15] = 0;
        carray[16] = 0;
        carray[17] = 0;
        carray[18] = 1;
        carray[19] = 0;
    }

    /***/
    /**
     * 去色同时加圆角
     *
     * @param bmpOriginal 原图
     * @param pixels      圆角弧度
     * @return 修改后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    /**
     * 把图片变成圆角
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 使圆角功能支持BitampDrawable
     *
     * @param bitmapDrawable
     * @param pixels
     * @return
     */
    @SuppressWarnings("deprecation")
    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
                                               int pixels) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
        return bitmapDrawable;
    }

    /**
     * 生成图片倒影
     *
     * @param originalImage
     * @return
     */
    public static Bitmap createReflectedImage(Bitmap originalImage) {
        final int reflectionGap = 4;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
                height / 2, width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(originalImage, 0, 0, null);
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
                + reflectionGap, 0x70ffffff, 0x00ffffff,
                TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);
        return bitmapWithReflection;
    }

    /**
     * bitmap保存为本地图片
     */
    public static void saveBitmap(String url, Bitmap mBitmap)
            throws IOException {
        File f = new File(url);
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmap(String filename, Bitmap mBitmap,Context context)
            throws IOException {

        FileOutputStream fOut = null;
        try {
            fOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmap(Context context, Uri uri) {
        InputStream in = null;
        try {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 2;// getPowerOfTwoForSampleRatio(ratio);
            bitmapOptions.inDither = true;// optional
            bitmapOptions.inPreferredConfig = Config.ARGB_8888;// optional
            in = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(in, null, bitmapOptions);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public static void deleteImage(String path) {
        File file = new File(path);
        file.deleteOnExit();
    }

    /**
     * 图片资源回收
     *
     * @param bitmap
     */
    public static void distoryBitmap(Bitmap bitmap) {
        if (null != bitmap && bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * Check the SD card
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static File savePhotoToSDCard(Bitmap photoBitmap, String path,
                                         String photoName) {
        File photoFile = null;

        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            photoFile = new File(path, photoName + ".png");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                            fileOutputStream)) {
                        fileOutputStream.flush();
                        // fileOutputStream.close();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (photoFile.isFile()) {
//            System.out.println("aaaaa");
        }
        return photoFile;
    }

    /**
     * view转bitmap
     *
     * @param v
     * @return
     */
    public static Bitmap createViewBitmap(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Config.RGB_565);
        Canvas c = new Canvas(screenshot);
        c.translate(-v.getScrollX(), -v.getScrollY());
        v.draw(c);
        return screenshot;
    }

    public static Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * @param bitmap
     * @return
     * @Description: Bitmap转Drawable
     */
    public static Drawable BitmapToDrawable(Bitmap bitmap) {
        if (bitmap != null) {
            return new BitmapDrawable(bitmap);
        }
        return null;
    }

    /**
     * @param drawable
     * @return
     * @Description: Drawable转Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                                : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * @param bitmap
     * @param pixels
     * @return
     * @Description: 圆角图片
     */
    public static Bitmap getfilletBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.BLACK);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        return output;
    }

    // 生成圆角图片
    public static Bitmap getfilletBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = 15;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     * @throws MalformedURLException
     * @Description: 从文件获取图片缩略图
     */
    public static Bitmap getThumbnailFromFile(String filePath, int reqWidth,
                                              int reqHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        opts.inSampleSize = computeSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, opts);
    }

    /**
     * @param source
     * @param targetWidth
     * @param targetHeight
     * @return
     * @Description: 指定宽高缩放图像(推荐方法)
     */
    public static Bitmap extractBitmap(Bitmap source, int targetWidth,
                                       int targetHeight) {
        return extractThumbnail(source, targetWidth, targetHeight, Option.NONE);
    }

    /**
     * @param source 图像
     * @param width  宽
     * @param height 高
     * @return
     * @Description: 指定宽高缩放图像
     */
    public static Bitmap zoomBitmap(Bitmap source, int width, int height) {
        return Bitmap.createScaledBitmap(source, width, height, true);
    }

    /**
     * @param source
     * @param targetWidth
     * @param targetHeight
     * @return
     * @Description: 按比例缩放图像(推荐方法)
     */
    public static Bitmap prorateBitmap(Bitmap source, int targetWidth,
                                       int targetHeight) {
        if (source == null) {
            return null;
        }
        int srcWidth = source.getWidth();
        int srcHeight = source.getHeight();
        if (srcWidth < srcHeight) {
            targetHeight = srcHeight * targetWidth / srcWidth;
        } else {
            targetWidth = srcWidth * targetHeight / srcHeight;
        }
        return extractThumbnail(source, targetWidth, targetHeight, Option.NONE);
    }

    /**
     * @param bitmap    图像
     * @param maxWidth  最大宽
     * @param maxHeight 最大高
     * @return
     * @Description: 按比例缩放图像
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        if (originWidth < maxWidth && originHeight < maxHeight) {
            return bitmap;
        }
        int newWidth = originWidth;
        int newHeight = originHeight;
        // 若图片过宽, 则保持长宽比缩放图片
        if (originWidth > maxWidth) {
            newWidth = maxWidth;
            double i = originWidth * 1.0 / maxWidth;
            newHeight = (int) Math.floor(originHeight / i);
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight,
                    true);
        }
        // 若图片过高, 则从中部截取
        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            int half_diff = (int) ((originHeight - maxHeight) / 2.0);
            bitmap = Bitmap.createBitmap(bitmap, 0, half_diff, newWidth,
                    newHeight);
        }
        return bitmap;
    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     * @Description: 计算缩放比例
     */
    private static int computeSampleSize(BitmapFactory.Options options,
                                         int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * scale source Bitmap to targeted width and height
     */
    private static Bitmap extractThumbnail(Bitmap source, int targetWidth,
                                           int targetHeight, int options) {
        if (source == null) {
            return null;
        }
        float scale;
        if (source.getWidth() < source.getHeight()) {
            scale = targetWidth / (float) source.getWidth();
        } else {
            scale = targetHeight / (float) source.getHeight();
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        return transform(matrix, source, targetWidth, targetHeight,
                Option.SCALE_UP | options);
    }

    /**
     * Transform source Bitmap to targeted width and height
     */
    private static Bitmap transform(Matrix scaler, Bitmap source,
                                    int targetWidth, int targetHeight, int options) {
        boolean scaleUp = (options & Option.SCALE_UP) != 0;
        boolean recycle = (options & Option.RECYCLE_INPUT) != 0;
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
            Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
                    Config.ARGB_8888);
            Canvas c = new Canvas(b2);
            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
                    + Math.min(targetWidth, source.getWidth()), deltaYHalf
                    + Math.min(targetHeight, source.getHeight()));
            int dstX = (targetWidth - src.width()) / 2;
            int dstY = (targetHeight - src.height()) / 2;
            Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
                    - dstY);
            c.drawBitmap(source, src, dst, null);
            if (recycle) {
                source.recycle();
            }
            return b2;
        }
        float bitmapWidthF = source.getWidth();
        float bitmapHeightF = source.getHeight();
        float bitmapAspect = bitmapWidthF / bitmapHeightF;
        float viewAspect = (float) targetWidth / targetHeight;
        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeightF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        } else {
            float scale = targetWidth / bitmapWidthF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        }
        Bitmap b1;
        if (scaler != null) {
            b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), scaler, true);
        } else {
            b1 = source;
        }
        if (recycle && b1 != source) {
            source.recycle();
        }
        int dx1 = Math.max(0, b1.getWidth() - targetWidth);
        int dy1 = Math.max(0, b1.getHeight() - targetHeight);
        Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
                targetHeight);
        if (b2 != b1) {
            if (recycle || b1 != source) {
                b1.recycle();
            }
        }
        return b2;
    }

    private interface Option {
        int NONE = 0x0;
        int SCALE_UP = 0x1;
        int RECYCLE_INPUT = 0x2;
    }

    /**
     * @param bitmap
     * @param _file
     * @throws IOException
     * @author miaoxin.ye
     * @createdate 2014-1-20 下午3:26:14
     * @Description: 保存图片到文件
     */
    public static void saveBitmapToFile(Bitmap bitmap, String _file) {
        BufferedOutputStream os = null;
        try {
            File file = new File(_file);
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 图片转字节
     *
     * @param bm
     * @return byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 30, baos);
        return baos.toByteArray();
    }

    /**
     * 字节转图片
     *
     * @param b
     * @return bitmap
     */
    public static Bitmap bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 下载并保存图片到本地
     *
     * @param url      图片地址
     */
    public static void downLoadAndSaveBitmap(String url,final  Context context,Target target) {
        if(StringUtils.isBlank(url)){
            return;
        }
        LogUtil.e("url:"+url);
        Picasso.with(context).load(url).into(target);
    }

    /**
     * 加载常规图片
     * @param url
     * @param imageView
     */
    public static void displayImage(String url, ImageView imageView){
        if(StringUtils.isBlank(url)){
            imageView.setImageResource(R.drawable.bg_jz);
            return;
        }

        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.bg_jz)//默认图片
                .error(R.drawable.bg_jz)//加载错误默认图片
//                .noFade()//默认图片加载方式有一个淡入的效果
                .fit()
                .centerInside()
                .config(Config.RGB_565)
                .into(imageView);
    }

    /**
     * 加载常规图片并设置默认图片
     * @param url
     * @param imageView
     * @param defaultImg
     */
    public static void displayImage(String url, ImageView imageView,int defaultImg){
        if(TextUtils.isEmpty(url)){
            imageView.setImageResource(defaultImg);
            return;
        }
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(defaultImg)//默认图片
                .error(defaultImg)//加载错误默认图片
                .noFade()//默认图片加载方式有一个淡入的效果
                .fit()
                .config(Config.RGB_565)
                .into(imageView);
    }

    /**
     * 加载图片并设置tag 列表图片用到
     * @param url
     * @param imageView
     * @param tag
     */
    public static void displayImage(String url, ImageView imageView,Object tag){
        if(StringUtils.isBlank(url)){
            imageView.setImageResource(R.drawable.bg_jz);
            return;
        }

        Picasso.with(imageView.getContext())
                .load(url)
                .tag(tag)
                .fit()
                .placeholder(R.drawable.bg_jz)//默认图片
                .error(R.drawable.bg_jz)//加载错误默认图片
//                .noFade()//默认图片加载方式有一个淡入的效果
                .config(Config.RGB_565)
                .into(imageView);
    }

    /**
     * 加载图片并制定宽高居中裁剪
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public static void displayImage(String url, ImageView imageView,int width,int height){
        if(StringUtils.isBlank(url)){
            return;
        }

        Picasso.with(imageView.getContext())
                .load(url)
                .resize(width,height)
                .fit()
//                .onlyScaleDown()//如果图片规格大于指定宽高直接替换
                .centerCrop() //.centerInside()拉伸
                .config(Config.RGB_565)
                .into(imageView);
    }

    /**
     * 加载图片并监听加载过程
     * @param context
     * @param url
     * @param target
     */
    public static void displayImage(Context context,String url,Target target){
        if(StringUtils.isBlank(url)){
            return;
        }

        Picasso.with(context).load(url).into(target);
    }

    /**
     * 加载大图
     * @param url
     * @param imageView
     */
    public static void displayImageLarge(String url, ImageView imageView){
        if(StringUtils.isBlank(url)){
            return;
        }

        Picasso.with(imageView.getContext())
                .load(url)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .config(Config.RGB_565)
                .into(imageView);
    }

    /**
     * 加载本地file类型图片
     * @param file
     * @param imageView
     */
    public static void displayImage(File file, ImageView imageView){
        Picasso.with(imageView.getContext())
                .load(file)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .config(Config.RGB_565)
                .into(imageView);
    }
    /**
     * 加载本地uri类型图片
     * @param uri
     * @param imageView
     */
    public static void displayImage(Uri uri, ImageView imageView){
        if(uri == null){
            return;
        }

        Picasso.with(imageView.getContext())
                .load(uri)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .config(Config.RGB_565)
                .into(imageView);
    }

    /**
     * 加载本地资源图片
     * @param resId
     * @param imageView
     */
    public static void displayImage(int resId, ImageView imageView){
        Picasso.with(imageView.getContext())
                .load(resId)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .config(Config.RGB_565)
                .into(imageView);
    }

    public static OkHttpClient getUnsafeOkHttpClient(Context context) {
        try {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
//                    .hostnameVerifier(mHostnameVerifier)
//                    .sslSocketFactory(HttpsFactroy.getSSLSocketFactory(context))//https证书
//                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}