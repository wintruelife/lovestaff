package love.wintrue.com.lovestaff.image;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.base.MApplication;
import love.wintrue.com.lovestaff.config.AppConfig;
import love.wintrue.com.lovestaff.utils.ActivityUtil;
import love.wintrue.com.lovestaff.utils.FileUtil;
import love.wintrue.com.lovestaff.utils.ImageUtil;
import love.wintrue.com.lovestaff.utils.LogUtil;

/**
 * @author th
 * @desc 获取图片
 * @time 2017/3/13 13:46
 */
public class ImageSelectActivity extends BaseActivity implements OnClickListener {

    private static final String IMAGE_DEFAULT_NAME = "default.png";

    private String IMAGE_DEFAULE_PATH = AppConfig.PICTURE_PATH;

    private static final int REQ_RESULT_PHOTO_CODE = 500;
    private static final int REQ_RESULT_CAPTURE_CODE = 600;
    private static final int REQ_RESULT_CORP_CODE = 666;
    // 图片缓存路径
    private Uri mCameraImageUri;
    // 图片缓存路径
    private String mCameraImagePath;
    private boolean mIsCrop = false;
    private boolean mFixedAspectRatio;
    private int mAspectRatioX = 1;
    private int mAspectRatioY = 1;
    /**
     * 将取消改为查看功能
     */
    private String mViewImageUrl;
    private Button mBtnCancel;

    protected int activityCloseEnterAnimation;

    protected int activityCloseExitAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("onCreate Bundle = " + savedInstanceState);

//        setContentView(R.layout.activity_image_select);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_select;
    }

    @Override
    public void initView() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;

        getWindow().setAttributes(lp);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mIsCrop = extras.getBoolean("crop");
            mCameraImagePath = extras.getString("image-path");
            mCameraImageUri = extras.getParcelable("image-path-uri");
            mViewImageUrl = extras.getString("view-image-uri");
            mAspectRatioX = extras.getInt("aspectRatioX", mAspectRatioX);
            mAspectRatioY = extras.getInt("aspectRatioY", mAspectRatioY);
            if (mCameraImageUri == null) {
                if (extras.containsKey("image-path")) {
                    mCameraImageUri = getImageUri(mCameraImagePath);
                }
            }
            mFixedAspectRatio = extras.getBoolean("fixedAspectRatio", true);
        }

        findViewById(R.id.btn_take_photo).setOnClickListener(this);
        findViewById(R.id.btn_pick_photo).setOnClickListener(this);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);
        if (null != mViewImageUrl) {
            mBtnCancel.setText("查看");
        }
        initAnim();
    }

    void initAnim() {
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});

        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);

        activityStyle.recycle();

        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});

        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);

        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);

        activityStyle.recycle();
    }

    /**
     * 打开手机相册
     */
    private void openMediaStore() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            try {
                if (mCameraImageUri == null) {
                    File tempFile = FileUtil.createSDFile(IMAGE_DEFAULE_PATH, IMAGE_DEFAULT_NAME);
                    mCameraImageUri = getImageUri(tempFile.getPath());
                    mCameraImagePath = tempFile.getPath();
                }
            } catch (IOException e) {
                e.printStackTrace();
                showToastMsg("您已拒绝读写手机存储功能权限，为了您更好的体验，请到应用管理权限中心设置相关权限");
            }
        } else {
            showToastMsg("存储卡已取出，相册功能暂不可用");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("noFaceDetection", false);
        startActivityForResult(intent, REQ_RESULT_PHOTO_CODE);
    }

    /**
     * 打开照相机
     */
    private void openCamera() {
        if (Util.isFastDoubleClick()) {
            return;
        }
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            try {
                if (mCameraImageUri == null) {
                    File tempFile = FileUtil.createSDFile(IMAGE_DEFAULE_PATH, IMAGE_DEFAULT_NAME);
                    mCameraImageUri = getImageUri(tempFile.getPath());
                    mCameraImagePath = tempFile.getPath();
                }
            } catch (IOException e) {
                e.printStackTrace();
                showToastMsg("您已拒绝相机相关权限，为了您更好的体验，请到应用管理权限中心设置相关权限");
            }
        } else {
            showToastMsg("存储卡已取出，拍照功能暂不可用");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQ_RESULT_CAPTURE_CODE);
    }

    private Uri getImageUri(String path) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < 24) {
            return Uri.fromFile(new File(path));
        } else {
            return FileProvider.getUriForFile(this, getPackageName() + ".android7.fileprovider", new File(path));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (null != mViewImageUrl) {
                    ArrayList<String> imageUrlList = new ArrayList<String>();
                    imageUrlList.add(mViewImageUrl);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS, imageUrlList);
                    ActivityUtil.next(ImageSelectActivity.this, ImageSimpleBrowseActivity.class, bundle, -1);
                } else {
                    if (!TextUtils.isEmpty(mCameraImagePath)) {
                        FileUtil.deteleFile(mCameraImagePath);
                    }
                }
                finish();
                break;
            case R.id.btn_pick_photo:
                openMediaStore();
                break;
            case R.id.btn_take_photo:
                checkPermissionCamera();
                break;
            default:
                break;
        }
    }

    private void checkPermissionCamera() {
        requestPermission(1, new String[]{Manifest.permission.CAMERA}, AppOpsManager.OPSTR_CAMERA, new Runnable() {
            @Override
            public void run() {
                try {
                    openCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                    showToastMsg("您已拒绝拍照权限，为了您更好的体验，请到应用权限管理中心设置权限!");
                }

            }
        }, new Runnable() {
            @Override
            public void run() {
                showToastMsg("您已拒绝拍照权限，请到应用权限管理中心设置权限!");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_RESULT_PHOTO_CODE) {
            MApplication.getInstance().setActivityResult(true);
            LogUtil.e("REQ_RESULT_PHOTO_CODE");
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            if (resultCode == Activity.RESULT_OK) {
                crop(data);
            }
        } else if (requestCode == REQ_RESULT_CAPTURE_CODE) {
            MApplication.getInstance().setActivityResult(true);
            LogUtil.e("REQ_RESULT_CAPTURE_CODE");
            if (resultCode == Activity.RESULT_OK) {
                crop(null);
            }
        } else if (requestCode == REQ_RESULT_CORP_CODE) {
            LogUtil.e("REQ_RESULT_CORP_CODE");
            if (resultCode == Activity.RESULT_OK) {
                backResult();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void crop(Intent data) {
        if (mIsCrop) {
            Intent intent = new Intent(ImageSelectActivity.this, ImageCropActivity.class);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("image-path", mCameraImagePath);
            if (data != null) {
                Uri imageUri = data.getData();
                intent.putExtra("image-path-uri", imageUri);
            }
            intent.putExtra("rotate", true);
            intent.putExtra("scale", true);
            intent.putExtra("fixedAspectRatio", mFixedAspectRatio);
            intent.putExtra("aspectRatioX", mAspectRatioX);
            intent.putExtra("aspectRatioY", mAspectRatioY);
            startActivityForResult(intent, REQ_RESULT_CORP_CODE);
        } else {
            Uri imageUri;
            if (data != null) {
                imageUri = data.getData();
            } else {
                imageUri = getImageUri(mCameraImagePath);
            }
            Bitmap selectedImage = ImageUtil.getBitmap(this, imageUri);
            try {
                ImageUtil.saveBitmap(mCameraImagePath, selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            backResult();
        }
    }

    private void backResult() {
        Bundle bd = new Bundle();
        bd.putString("image-path", mCameraImagePath);
        bd.putParcelable("image-path-uri", mCameraImageUri);
        setResult(Activity.RESULT_OK, getIntent().putExtras(bd));
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }
}
