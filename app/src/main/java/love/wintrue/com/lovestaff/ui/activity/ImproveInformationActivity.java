package love.wintrue.com.lovestaff.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;

import java.util.List;

import butterknife.Bind;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.widget.ClearEditText;
import love.wintrue.com.lovestaff.widget.StateButton;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;

/**
 * Created by XDHG on 2018/5/24.
 */

public class ImproveInformationActivity extends BaseActivity {
    @Bind(R.id.title_actionbar_login)
    CommonActionBar cab;
    @Bind(R.id.btn_join)
    StateButton btnJoin;
    @Bind(R.id.et_pwd)
    ClearEditText etPwd;
    @Bind(R.id.iv_pwd_visible)
    ImageView ivPwdVisible;
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    private boolean password_visual;
    private int REQUEST_CODE = 120;

    @Override
    public int getLayoutId() {
        return R.layout.activity_improve_information;
    }

    @Override
    public void initView() {
        cab.setActionBarTitle("完善个人信息");
        cab.setLeftImgBtnWithBg(R.mipmap.nav_bar_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cab.setTitleColor(ContextCompat.getColor(THIS, R.color.white));
        cab.setBackground(ContextCompat.getDrawable(THIS, R.drawable.title_bg));
        btnJoin.setNormalBackgroundColor(colors);

        ivPwdVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = etPwd.getSelectionStart();
                if (password_visual) {
                    password_visual = false;
                    etPwd.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivPwdVisible.setImageDrawable(getResources().getDrawable(R.mipmap.icon_yj_gb));
                } else {
                    password_visual = true;
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivPwdVisible.setImageDrawable(getResources().getDrawable(R.mipmap.icon_yj_dk));
                }
                etPwd.setSelection(index);
            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            Glide.with(THIS).load(pathList.get(0))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.mipmap.img_user_default)
                    .error(R.mipmap.img_user_default)
                    .crossFade().into(ivAvatar);
        }
    }

    /**
     * 开启图片选择器
     */
    private void choosePhoto() {
//        ImgSelConfig config = new ImgSelConfig.Builder(loader)
//                // 是否多选
//                .multiSelect(false)
//                // 确定按钮背景色
//                .btnBgColor(Color.TRANSPARENT)
//                .titleBgColor(ContextCompat.getColor(this, R.color.colorPrimary))
//                // 使用沉浸式状态栏
//                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
//                .title("图片")
//                // 第一个是否显示相机
//                .needCamera(true)
//                // 最大选择图片数量
//                .maxNum(1)
//                .needCrop(true)
//                .build();
//        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
    }

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            //目前用Glide进行测试
            Glide.with(context).load(path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bg_jz)
                    .error(R.drawable.bg_jz)
                    .crossFade().into(imageView);
        }
    };
}
