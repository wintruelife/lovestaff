/**
 *
 */
package love.wintrue.com.lovestaff.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import love.wintrue.com.lovestaff.R;

/**
 * Universal-Image-loader 封装，单例
 *
 * @author dushumeng
 */
public class ImageLoaderUtils {
    private static final int BUFFER_SIZE = 4096;
    private static final Object temoObject = new Object();

    public static final DisplayImageOptions OPTIONS = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.bg_jz)
            .showImageForEmptyUri(R.drawable.bg_jz).showImageOnFail(R.drawable.bg_jz).bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).resetViewBeforeLoading(true).cacheOnDisc(true).cacheInMemory(true).considerExifParams(true)
            .build();

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .discCacheFileNameGenerator(new ImageLoaderUtils.Md5FileNameGeneratorEx()).tasksProcessingOrder(QueueProcessingType.LIFO);

        ImageLoaderConfiguration config = builder.build();
        ImageLoader.getInstance().init(config);
    }

    public static ImageLoader createImageLoader(Context context) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (imageLoader.isInited()) {
            return imageLoader;
        }
        // 避免多线程对其二次初始化
        synchronized (temoObject) {
            if (imageLoader.isInited()) {
                return imageLoader;
            }
            initImageLoader(context);
            return imageLoader;
        }
    }

    private static String getImageSuffix(String url) {
        int index = url.lastIndexOf('.');
        if (index < 0) {
            return null;
        }
        String lowfilefix = url.substring(index).toLowerCase();
        if (lowfilefix.equals(".jpeg") || lowfilefix.equals(".bmp")) {
            return ".jpg";
        } else {
            return lowfilefix;
        }
    }

    private static class Md5FileNameGeneratorEx extends Md5FileNameGenerator {
        @Override
        public String generate(String imageUri) {
            String imageSuffix = getImageSuffix(imageUri);
            String fileName = super.generate(imageUri);
            if (imageSuffix != null) {
                return fileName + imageSuffix;
            }
            return fileName;
        }
    }

    public static void removeCache(String imageUri, ImageLoader loader) {
        DiskCacheUtils.removeFromCache(imageUri, loader.getDiskCache());
        MemoryCacheUtils.removeFromCache(imageUri, loader.getMemoryCache());
    }

    /**
     * 将图片置灰
     */
    public static void toGrayImage(ImageView imageview) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageview.setColorFilter(filter);

    }

    /**
     * 压缩图片
     *
     * @return
     */
    public static InputStream compressImage(Context context, String filePath, int imgSize) {
        Log.d("Lird", "filePath:   " + filePath);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap image = BitmapFactory.decodeFile(filePath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = AndroidUtil.getScreenHeightByContext(context);
        float ww = AndroidUtil.getScreenWidthByContext(context);

        Log.d("Lird", "newOpts.outWidth:   " + newOpts.outWidth + "newOpts.outHeight:  "
                + newOpts.outHeight + "screen height:    " + hh + "screen width:    " + ww);
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        image = BitmapFactory.decodeFile(filePath, newOpts);
        Log.d("Lird", "image:   " + image);
        if (null == image) {
            return null;
        }
        //此时返回bm为空
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > imgSize) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩
            Log.d("Lird", "baos.toByteArray().length:" + baos.toByteArray().length);
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return isBm;
    }

    /**
     * 将文件生成位图
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static BitmapDrawable getImageDrawable(String path)
            throws IOException {
        //打开文件
        if (StringUtils.isBlank(path)) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] bt = new byte[BUFFER_SIZE];

        //得到文件的输入流
        InputStream in = new FileInputStream(file);

        //将文件读出到输出流中
        int readLength = in.read(bt);
        while (readLength != -1) {
            outStream.write(bt, 0, readLength);
            readLength = in.read(bt);
        }

        //转换成byte 后 再格式化成位图
        byte[] data = outStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        return bd;
    }

    /**
     * 将文件生成位图
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static BitmapDrawable getImageDrawable(String path, int w, int h)
            throws IOException {
        //打开文件
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] bt = new byte[BUFFER_SIZE];

        //得到文件的输入流
        InputStream in = new FileInputStream(file);

        //将文件读出到输出流中
        int readLength = in.read(bt);
        while (readLength != -1) {
            outStream.write(bt, 0, readLength);
            readLength = in.read(bt);
        }

        //转换成byte 后 再格式化成位图
        byte[] data = outStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        int width = bd.getIntrinsicWidth();
        int height = bd.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(bd);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(newbmp);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
