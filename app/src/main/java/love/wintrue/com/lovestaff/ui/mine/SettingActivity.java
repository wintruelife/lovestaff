package love.wintrue.com.lovestaff.ui.mine;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.ActivityManager;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.base.MApplication;
import love.wintrue.com.lovestaff.ui.activity.ChangePasswordActivity;
import love.wintrue.com.lovestaff.ui.activity.RegisterAndLoginActivity;
import love.wintrue.com.lovestaff.ui.setting.BlackListActivity;
import love.wintrue.com.lovestaff.utils.ActivityUtil;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;
import love.wintrue.com.lovestaff.widget.dialog.CommonAlertDialog;

/**
 * Created by a1314 on 2018/5/24.
 */

public class SettingActivity extends BaseActivity {


    @Bind(R.id.common_action_bar)
    CommonActionBar commonActionBar;
    @Bind(R.id.userinfo_name)
    TextView userinfoName;
    @Bind(R.id.mine_setting_msg)
    LinearLayout mineSettingMsg;
    @Bind(R.id.textView12)
    TextView textView12;
    @Bind(R.id.activity_setting_blacnum)
    LinearLayout activitySettingBlacnum;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.activity_setting_changepassword)
    LinearLayout activitySettingChangepassword;
    @Bind(R.id.activity_setting_clear)
    LinearLayout activitySettingClear;
    @Bind(R.id.activity_setting_callback)
    LinearLayout activitySettingCallback;
    @Bind(R.id.userinfo_setting_versioncode)
    TextView userinfoSettingVersioncode;
    @Bind(R.id.activity_setting_version)
    LinearLayout activitySettingVersion;
    @Bind(R.id.setting_loginout)
    TextView settingLoginout;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    private CommonAlertDialog mDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        commonActionBar.setActionBarTitle("设置");
        commonActionBar.setActionBarSeparationLineVisiable(View.GONE);
        commonActionBar.setLeftImgBtn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commonActionBar.setBackground(ContextCompat.getDrawable(THIS, R.drawable.title_bg));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.mine_setting_msg, R.id.activity_setting_blacnum, R.id.activity_setting_changepassword, R.id.activity_setting_clear, R.id.activity_setting_callback, R.id.activity_setting_version, R.id.setting_loginout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_setting_msg:
                break;
            case R.id.activity_setting_blacnum:
                ActivityUtil.next(SettingActivity.this, BlackListActivity.class);
                break;
            case R.id.activity_setting_changepassword:
                ActivityUtil.next(SettingActivity.this, ChangePasswordActivity.class);
                break;
            case R.id.activity_setting_clear:
                break;
            case R.id.activity_setting_callback:
                break;
            case R.id.activity_setting_version:
                break;
            case R.id.setting_loginout:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        if (null == mDialog) {
            mDialog = new CommonAlertDialog(this);
        }
        mDialog.setTitle("退出登录");
//        mDialog.setMessage(msg);
        mDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setPositiveButton(R.string.ensure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                MApplication.getInstance().setUser(null);
                ActivityUtil.next(SettingActivity.this, RegisterAndLoginActivity.class, true);

                ActivityManager activityManager = ActivityManager.getInstance();
                activityManager.finishAllActivityExcept();
            }
        });
        mDialog.show();
    }
}
