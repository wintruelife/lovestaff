package love.wintrue.com.lovestaff.image;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.utils.ImageUtil;
import love.wintrue.com.lovestaff.utils.LogUtil;

/**
 * @author th
 * @desc 裁减图片框
 * @time 2017/3/13 13:46
 */
public class ImageCropActivity extends MonitoredActivity {
    private static final String TAG = "CropImage";
    private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;
    // Static final constants
    private static final int ROTATE_NINETY_DEGREES = 90;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private final Handler mHandler = new Handler();

    private Uri mSaveUri = null;
    private ContentResolver mContentResolver;
    private String mImagePath;
    private Uri mImagePathUri;

    private CropImageView cropImageView;
    private Bitmap mBitmap;
    private boolean mSaving;

    private int mAspectRatioX = 1;
    private int mAspectRatioY = 1;
    private int mGuidelines = 0;
    private boolean mScale = true;
    private boolean isSuppotRotate = true;
    private boolean mFixedAspectRatio;

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_crop);
        mContentResolver = getContentResolver();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            mImagePath = extras.getString("image-path");
            mImagePathUri = extras.getParcelable("image-path-uri");
            mAspectRatioX = extras.getInt("aspectRatioX", mAspectRatioX);
            mAspectRatioY = extras.getInt("aspectRatioY", mAspectRatioY);
            if (mImagePathUri != null) {
                mSaveUri = getImageUri(mImagePath);
                InputStream in = null;
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    LogUtil.e("inSampleSize");
                    bitmapOptions.inSampleSize = 2;// getPowerOfTwoForSampleRatio(ratio);
                    bitmapOptions.inDither = true;// optional
                    bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    in = mContentResolver.openInputStream(mImagePathUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, bitmapOptions);
//                    LogUtil.e(bitmap.getWidth()+"/"+bitmap.getHeight());
                    if (bitmap != null && bitmap.getWidth() < 700) {
                        Matrix matrix = new Matrix();
                        matrix.postScale(2, 2); //长和宽放大缩小的比例
                        mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        bitmap.recycle();
                    } else {
                        mBitmap = bitmap;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                mSaveUri = getImageUri(mImagePath);
                mBitmap = getBitmap(mImagePath);
            }
            mGuidelines = extras.getInt("guidelines");
            mScale = extras.getBoolean("scale", mScale);
            isSuppotRotate = extras.getBoolean("rotate", true);
            mFixedAspectRatio = extras.getBoolean("fixedAspectRatio", true);
        }

        if (mBitmap == null) {
            finish();
            return;
        }

        // Make UI fullscreen.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        cropImageView.setAspectRatio(mAspectRatioX, mAspectRatioY);
        cropImageView.setFixedAspectRatio(mFixedAspectRatio);
        cropImageView.setGuidelines(mGuidelines);
        cropImageView.setImageBitmap(mBitmap);
        findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveClicked();
            }
        });

        if (isSuppotRotate) {
            findViewById(R.id.rotate).setVisibility(View.VISIBLE);
            findViewById(R.id.rotate).setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if (isFinishing()) {
                        return;
                    }
                    cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
                }
            });
        } else {
            findViewById(R.id.rotate).setVisibility(View.GONE);
        }

    }

    private void onSaveClicked() {
        // TODO this code needs to change to use the decode/crop/encode single
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mSaving)
            return;
        mSaving = true;
        Bitmap croppedImage = cropImageView.getCroppedImage();
        Bundle myExtras = getIntent().getExtras();
        if (croppedImage != null) {
            if (myExtras != null && (myExtras.getParcelable("data") != null || myExtras.getBoolean("return-data"))) {
                Bundle extras = new Bundle();
                croppedImage = ImageUtil.compressForScale(croppedImage);
                extras.putParcelable("data", croppedImage);
                setResult(RESULT_OK, (new Intent()).setAction("inline-data").putExtras(extras));
                finish();
            } else {
                final Bitmap b = croppedImage;
                Util.startBackgroundJob(this, null, (String) getText(R.string.image_select_crop_img), new Runnable() {
                    public void run() {
                        saveOutput(b);
                    }
                }, mHandler);
            }
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void saveOutput(Bitmap croppedImage) {
        try {
            ImageUtil.saveBitmap(mImagePath, croppedImage);
        } catch (IOException e) {
            LogUtil.e(TAG, "Cannot open file: " + mImagePath, e);
        } finally {
            croppedImage.recycle();
        }
        setResult(RESULT_OK);
        croppedImage.recycle();
        finish();
    }

    private Bitmap getBitmap(String path) {
//        Uri uri = getImageUri(path);
//        InputStream in = null;
//        LogUtil.e(uri.getPath());
        try {
//            in = mContentResolver.openInputStream(uri);
//            LogUtil.e(in.read()+"");
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(path, bitmapOptions);// 此时返回bm为空

            bitmapOptions.inSampleSize = 2;// getPowerOfTwoForSampleRatio(ratio);
            bitmapOptions.inDither = true;// optional
            bitmapOptions.inJustDecodeBounds = false;
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
            bitmap = BitmapFactory.decodeFile(path, bitmapOptions);
            return bitmap;
        } catch (Exception e) {
            LogUtil.e(TAG, "file " + path + " not found");
        }
        return null;
    }

    /*
     * 得到图片字节流 数组大小
     * */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    private Uri getImageUri(String path) {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion < 24) {
            return Uri.fromFile(new File(path));
        } else {
            return FileProvider.getUriForFile(this, "com.ytsh.finance.fileprovider", new File(path));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBitmap != null) {
            mBitmap.recycle();
        }
    }
}
