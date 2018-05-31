package love.wintrue.com.lovestaff.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.ui.setting.MyCollectListActivity;
import love.wintrue.com.lovestaff.utils.ActivityUtil;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;

/**
 * Created by a1314 on 2018/5/23.
 */

public class MineActivity extends BaseActivity {
    @Bind(R.id.common_action_bar)
    CommonActionBar commonActionBar;
    @Bind(R.id.mine_user_img)
    ImageView mineUserImg;
    @Bind(R.id.mine_user_name)
    TextView mineUserName;
    @Bind(R.id.mine_user_discr)
    TextView mineUserDiscr;
    @Bind(R.id.mine_user_info)
    View mineUserInfo;
    @Bind(R.id.mine_header_fl)
    FrameLayout mineHeaderFl;
    @Bind(R.id.mine_wdfl)
    LinearLayout mineWdfl;
    @Bind(R.id.mine_wdgz)
    LinearLayout mineWdgz;
    @Bind(R.id.mine_wdfb)
    LinearLayout mineWdfb;
    @Bind(R.id.mine_wdhf)
    LinearLayout mineWdhf;
    @Bind(R.id.mine_wdsc)
    LinearLayout mineWdsc;
    @Bind(R.id.mine_wdqz)
    LinearLayout mineWdqz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine;
    }

    @Override
    public void initView() {
        commonActionBar.setActionBarTitle("我的");
        commonActionBar.setActionBarSeparationLineVisiable(View.GONE);
        commonActionBar.setLeftImgBtn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commonActionBar.setBackground(ContextCompat.getDrawable(THIS, R.drawable.title_bg));
        commonActionBar.setRightImgBtn(R.drawable.icon_sz,new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.next(MineActivity.this,SettingActivity.class);
            }
        });
    }


    @OnClick({R.id.mine_user_info, R.id.mine_wdfl, R.id.mine_wdgz, R.id.mine_wdfb, R.id.mine_wdhf, R.id.mine_wdsc, R.id.mine_wdqz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_user_info:
                ActivityUtil.next(MineActivity.this,UserInfoActivity.class);
                break;
            case R.id.mine_wdfl:
                break;
            case R.id.mine_wdgz:
                break;
            case R.id.mine_wdfb:
                break;
            case R.id.mine_wdhf:
                break;
            case R.id.mine_wdsc:
                ActivityUtil.next(MineActivity.this, MyCollectListActivity.class);
                break;
            case R.id.mine_wdqz:
                break;
        }
    }
}
