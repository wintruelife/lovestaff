package love.wintrue.com.lovestaff.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.image.ImageSelectActivity;
import love.wintrue.com.lovestaff.receiver.NetworkStateReceiver;
import love.wintrue.com.lovestaff.utils.ActivityUtil;
import love.wintrue.com.lovestaff.utils.FileUtil;
import love.wintrue.com.lovestaff.utils.LogUtil;
import love.wintrue.com.lovestaff.utils.Util;
import love.wintrue.com.lovestaff.widget.CircleImageView;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;

/**
 * Created by a1314 on 2018/5/24.
 */

public class UserInfoActivity extends BaseActivity {
    @Bind(R.id.common_action_bar)
    CommonActionBar commonActionBar;
    @Bind(R.id.userinfo_phone)
    CircleImageView userinfoPhone;
    @Bind(R.id.userinfo_tx)
    LinearLayout userinfoTx;
    @Bind(R.id.userinfo_name)
    TextView userinfoName;
    @Bind(R.id.mine_sm)
    LinearLayout mineSm;
    @Bind(R.id.userinfo_nick)
    TextView userinfoNick;
    @Bind(R.id.mine_nc)
    LinearLayout mineNc;
    @Bind(R.id.userinfo_discr)
    TextView userinfoDiscr;
    @Bind(R.id.mine_gxqm)
    LinearLayout mineGxqm;
    @Bind(R.id.userinfo_sex)
    TextView userinfoSex;
    @Bind(R.id.mine_wdsc)
    LinearLayout mineWdsc;
    @Bind(R.id.userinfo_age)
    TextView userinfoAge;
    @Bind(R.id.mine_nl)
    LinearLayout mineNl;
    @Bind(R.id.userinfo_born_time)
    TextView userinfoBornTime;
    @Bind(R.id.mine_csrq)
    LinearLayout mineCsrq;
    @Bind(R.id.userinfo_company)
    TextView userinfoCompany;
    @Bind(R.id.mine_gs)
    LinearLayout mineGs;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;

    public static final int CHOICE_AVATAR_REQUEST = 1;//选择用户头像
    private String mHeadPath;

    @Override
    public int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initView() {
        commonActionBar.setActionBarTitle("个人资料");
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

    @OnClick({R.id.userinfo_tx, R.id.mine_nc, R.id.mine_gxqm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.userinfo_tx:
                choiceAvatarImg();
                break;
            case R.id.mine_nc:
                ActivityUtil.next(UserInfoActivity.this,NickSettingActivity.class);
                break;
            case R.id.mine_gxqm:
                break;
        }
    }

    int mErrorCount = 3;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            String obj = msg.obj.toString();
            switch (what) {
                case 0://上传失败,重新上传
                    if (mErrorCount > 0) {
                        FileUtil.uploadFile("head.jpg", new File(mHeadPath), mHandler, CHOICE_AVATAR_REQUEST);
                        mErrorCount--;
                    } else {
                        hideWaitDialog();
                        showToastMsg(obj);
                    }
                    break;
                case 1://上传成功
                    hideWaitDialog();
//                    mIvHead.setTag(obj);
//                    httpRequestUpdateHead();
                    break;
            }
        }
    };

    /**
     * 选择头像
     */
    private void choiceAvatarImg() {
        String mImagePath = Util.createImagePath(this);
        if (mImagePath == null) {
            return;
        }
        Intent intent = new Intent(this, ImageSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("crop", true);
        bundle.putString("image-path", mImagePath);
        intent.putExtras(bundle);
        this.startActivityForResult(intent, CHOICE_AVATAR_REQUEST);
        this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
//        ActivityUtil.nextBottom(getActivity(), ImageSelectActivity.class,bundle,CHOICE_AVATAR_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) {
            if (requestCode == CHOICE_AVATAR_REQUEST) {//头像
                mHeadPath = data.getStringExtra("image-path");
                LogUtil.e(mHeadPath);
                if (!NetworkStateReceiver.isNetworkAvailable(this)) {
                    showToastMsg("网络状况不佳,请检查网络");
                } else {
                    showWaitDialog(getResources().getString(R.string.txt_on_wait));
                    FileUtil.uploadFile("head.jpg", new File(mHeadPath), mHandler, CHOICE_AVATAR_REQUEST);
                }
            }
        }
    }
}
