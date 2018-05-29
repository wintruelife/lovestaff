package love.wintrue.com.lovestaff.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;

/**
 * Created by a1314 on 2018/5/24.
 */

public class DescribeSettingActivity extends BaseActivity {


    @Bind(R.id.common_action_bar)
    CommonActionBar commonActionBar;
    @Bind(R.id.mine_nick_linear)
    LinearLayout mineNickLinear;

    @Override
    public int getLayoutId() {
        return R.layout.activity_decribesetting;
    }

    @Override
    public void initView() {
        commonActionBar.setActionBarTitle("个性签名");
        commonActionBar.setActionBarSeparationLineVisiable(View.GONE);
        commonActionBar.setLeftImgBtn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commonActionBar.setBackground(ContextCompat.getDrawable(THIS, R.drawable.title_bg));
        commonActionBar.setRightTxtBtn(R.string.txt_complete, Color.WHITE, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
