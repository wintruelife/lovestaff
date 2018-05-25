package love.wintrue.com.lovestaff.ui.activity;

import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.widget.ClearEditText;
import love.wintrue.com.lovestaff.widget.StateButton;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;

/**
 * Created by XDHG on 2018/5/25.
 */

public class ChangePasswordActivity extends BaseActivity {
    @Bind(R.id.title_actionbar_login)
    CommonActionBar cab;
    @Bind(R.id.iv_pwd_visible)
    ImageView ivPwdVisible;
    @Bind(R.id.iv_setpwd_visible)
    ImageView ivSetPwdVisible;
    @Bind(R.id.et_pwd)
    ClearEditText etPwd;
    @Bind(R.id.et_setpwd)
    ClearEditText etSetPwd;
    @Bind(R.id.et_confirmpwd)
    ClearEditText etConfirmPwd;
    @Bind(R.id.ll_changePwd_one)
    LinearLayout llChangePwdOne;
    @Bind(R.id.ll_changePwd_two)
    LinearLayout llChangePwdTwo;
    @Bind(R.id.btn_change_pwd_next)
    StateButton btnChangePwdNext;
    @Bind(R.id.btn_changePwd_confirm)
    StateButton btnChangePwdConfirm;

    private boolean password_visual;
    private boolean changePassword_visual;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initView() {
        String intentFrom = getIntent().getStringExtra("intentFrom");
        if (TextUtils.equals(intentFrom, "ForgetPasswordActivity")) {
            llChangePwdOne.setVisibility(View.GONE);
            llChangePwdTwo.setVisibility(View.VISIBLE);
        }
        cab.setActionBarTitle("修改密码");
        cab.setLeftImgBtn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnChangePwdConfirm.setNormalBackgroundColor(colors);
        btnChangePwdNext.setNormalBackgroundColor(colors);
        btnChangePwdNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llChangePwdOne.setVisibility(View.GONE);
                llChangePwdTwo.setVisibility(View.VISIBLE);
            }
        });

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

        ivSetPwdVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = 0;
                if (etSetPwd.hasFocus()) {
                    index = etSetPwd.getSelectionStart();
                } else if (etConfirmPwd.hasFocus()) {
                    index = etConfirmPwd.getSelectionStart();
                }
                if (changePassword_visual) {
                    changePassword_visual = false;
                    etSetPwd.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etConfirmPwd.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivSetPwdVisible.setImageDrawable(getResources().getDrawable(R.mipmap.icon_yj_gb));
                } else {
                    changePassword_visual = true;
                    etSetPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etConfirmPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivSetPwdVisible.setImageDrawable(getResources().getDrawable(R.mipmap.icon_yj_dk));
                }

                if (etSetPwd.hasFocus()) {
                    etSetPwd.setSelection(index);
                } else if (etConfirmPwd.hasFocus()) {
                    etConfirmPwd.setSelection(index);
                }
            }
        });
    }
}
