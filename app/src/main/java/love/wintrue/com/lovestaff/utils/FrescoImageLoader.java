package love.wintrue.com.lovestaff.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by lhe on 2017/8/15.
 */

public class FrescoImageLoader {

    private static final String IMAGE_PIPELINE_CACHE_DIR = "image_cache";

    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "image_small_cache";

    private static final int MAX_DISK_SMALL_CACHE_SIZE = 10 * ByteConstants.MB;

    private static final int MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE = 5 * ByteConstants.MB;

    private static ImagePipelineConfig sImagePipelineConfig;

    /**
     * Creates config using android http stack as network backend.
     */
    public static ImagePipelineConfig getImagePipelineConfig(final Context context) {
        if (sImagePipelineConfig == null) {
            /**
             * 推荐缓存到应用本身的缓存文件夹，这么做的好处是:
             * 1、当应用被用户卸载后能自动清除缓存，增加用户好感（可能以后用得着时，还会想起我）
             * 2、一些内存清理软件可以扫描出来，进行内存的清理
             */
            File fileCacheDir = context.getApplicationContext().getCacheDir();
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                fileCacheDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Fresco");
//            }
            DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                    .setBaseDirectoryPath(fileCacheDir)
                    .build();
            DiskCacheConfig smallDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(fileCacheDir)
                    .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
                    .setMaxCacheSize(MAX_DISK_SMALL_CACHE_SIZE)
                    .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE)
                    .build();
            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
            Set<RequestListener> requestListeners = new HashSet<>();
            requestListeners.add(new RequestLoggingListener());
            // 当内存紧张时采取的措施
            MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
            memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
                @Override
                public void trim(MemoryTrimType trimType) {
                    final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                    LogUtil.i(String.format("Fresco onCreate suggestedTrimRatio : %d", suggestedTrimRatio));
                    if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                            ) {
                        // 清除内存缓存
                        Fresco.getImagePipeline().clearMemoryCaches();
                    }
                }
            });

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            X509TrustManager mX509TrustManager = new X509TrustManager() {
//                @Override
//                public void checkClientTrusted(
//                        java.security.cert.X509Certificate[] x509Certificates,
//                        String s) throws java.security.cert.CertificateException {
//                }
//
//                @Override
//                public void checkServerTrusted(
//                        java.security.cert.X509Certificate[] x509Certificates,
//                        String s) throws java.security.cert.CertificateException {
//                }
//
//                @Override
//                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                    return new java.security.cert.X509Certificate[]{};
//                }
//            };
//            TrustManager[] mTrustAllCerts = new TrustManager[]{mX509TrustManager};
//            HostnameVerifier mHostnameVerifier = new HostnameVerifier() {
//                @Override
//                public boolean verify(String s, SSLSession sslSession) {
//                    return true;
//                }
//            };
//            SSLContext sSLContext = null;
//            try {
//                sSLContext = SSLContext.getInstance("TLS");
//                sSLContext.init(null, mTrustAllCerts, new java.security.SecureRandom());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(httpLoggingInterceptor)
//                .hostnameVerifier(mHostnameVerifier)
//                .sslSocketFactory(sSLContext.getSocketFactory(),mX509TrustManager)
//                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
//                .build();

            OkHttpClient okHttpClient = ImageUtil.getUnsafeOkHttpClient(context);
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            sImagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(context, okHttpClient)
//            sImagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                    .setBitmapsConfig(Bitmap.Config.ARGB_4444) // 若不是要求忒高清显示应用，就用使用RGB_565吧（默认是ARGB_8888)
                    .setDownsampleEnabled(true) // 在解码时改变图片的大小，支持PNG、JPG以及WEBP格式的图片，与ResizeOptions配合使用
                    // 设置Jpeg格式的图片支持渐进式显示
                    .setProgressiveJpegConfig(new ProgressiveJpegConfig() {
                        @Override
                        public int getNextScanNumberToDecode(int scanNumber) {
                            return scanNumber + 2;
                        }

                        public QualityInfo getQualityInfo(int scanNumber) {
                            boolean isGoodEnough = (scanNumber >= 5);
                            return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
                        }
                    })
                    .setRequestListeners(requestListeners)
                    .setMemoryTrimmableRegistry(memoryTrimmableRegistry) // 报内存警告时的监听
                    // 设置内存配置
                    .setBitmapMemoryCacheParamsSupplier(new FrescoImageMemoryCacheSupplier(activityManager))
                    .setMainDiskCacheConfig(mainDiskCacheConfig) // 设置主磁盘配置
                    .setSmallImageDiskCacheConfig(smallDiskCacheConfig) // 设置小图的磁盘配置
                    .build();
        }
        return sImagePipelineConfig;
    }

    /**
     * 加载本地图片
     * @param draweeView
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     */
    public static void loadSdCardImg(final SimpleDraweeView draweeView, String filePath, final int reqWidth, final int reqHeight) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.autoRotate())
                .setLocalThumbnailPreviewsEnabled(true)
                .setResizeOptions(new ResizeOptions(reqWidth, reqHeight))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                        if (imageInfo == null) {
                            return;
                        }
                        ViewGroup.LayoutParams vp = draweeView.getLayoutParams();
                        vp.width = reqWidth;
                        vp.height = reqHeight;
                        draweeView.requestLayout();
                    }
                })
                .build();
        draweeView.setController(controller);
    }

    /**
     * 从本地资源id加载图片
     * @param draweeView
     * @param resId
     */
    public static void loadDrawableById(SimpleDraweeView draweeView, int resId) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(draweeView.getController())
                .build();
        draweeView.setController(controller);
    }

    /**
     * 加载图片并高斯模糊处理
     * @param draweeView
     * @param url
     */
    public static void loadImageBlur(final SimpleDraweeView draweeView, String url) {
        loadImage(draweeView, url, new BasePostprocessor() {
            @Override
            public String getName() {
                return "blurPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                ImageBlurUtil.blur(bitmap, 35);
            }
        });
    }

    /**
     * 加载图片
     * @param simpleDraweeView
     * @param url
     * @param processor
     */
    public static void loadImage(SimpleDraweeView simpleDraweeView, String url, BasePostprocessor processor) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        simpleDraweeView.setImageURI(url);
        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.autoRotate())
                .setPostprocessor(processor)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(simpleDraweeView.getController())
                .build();
        simpleDraweeView.setController(controller);
    }
}
