package love.wintrue.com.lovestaff.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.ui.mine.MineActivity;
import love.wintrue.com.lovestaff.utils.ActivityUtil;
import love.wintrue.com.lovestaff.utils.AnimationUtils;
import love.wintrue.com.lovestaff.widget.StateButton;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;

/**
 * Created by XDHG on 2018/5/22.
 */

public class RegisterAndLoginActivity extends BaseActivity {
    @Bind(R.id.title_actionbar_login)
    CommonActionBar cab;
    @Bind(R.id.btn_login)
    StateButton btnLogin;
    @Bind(R.id.btn_register_next)
    StateButton btnRegisterNext;
    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.ll_login_view)
    LinearLayout llLoginView;
    @Bind(R.id.ll_register_view)
    LinearLayout llRegisterView;
    @Bind(R.id.iv_triangle)
    ImageView ivTriangle;
    private int[] start_location;
    private int[] end_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_and_login;
    }

    @Override
    public void initView() {
//        ActivityUtil.next(this, MineActivity.class);
        cab.setLeftImgBtnWithBg(R.mipmap.nav_bar_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cab.setActionBarTitle("爱员工");
        cab.setActionBarSeparationLineVisiable(View.GONE);
        cab.setTitleColor(ContextCompat.getColor(THIS, R.color.white));
        cab.setBackground(ContextCompat.getDrawable(THIS, R.drawable.title_bg));
        int colors[] = {0xffFF9B4C, 0xffFF2F50, 0xffFF2F50};
        btnLogin.setNormalBackgroundColor(colors);
        btnRegisterNext.setNormalBackgroundColor(colors);


        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llLoginView.setVisibility(View.GONE);
                llRegisterView.setVisibility(View.VISIBLE);
                start_location = new int[2];
                tvLogin.getLocationInWindow(start_location);
                end_location = new int[2];
                tvRegister.getLocationInWindow(end_location);
                AnimationUtils.translation(ivTriangle, start_location[0], end_location[0], null, 300, "translationX", "moveRight");
                AnimationUtils.alphaAnimator(tvLogin, 1f, 0.6f);
                AnimationUtils.alphaAnimator(tvRegister, 0.6f, 1f);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llLoginView.setVisibility(View.VISIBLE);
                llRegisterView.setVisibility(View.GONE);
                start_location = new int[2];
                tvLogin.getLocationInWindow(start_location);
                end_location = new int[2];
                tvRegister.getLocationInWindow(end_location);
                AnimationUtils.translation(ivTriangle, start_location[0], end_location[0], null, 300, "translationX", "moveLeft");
                AnimationUtils.alphaAnimator(tvRegister, 1f, 0.6f);
                AnimationUtils.alphaAnimator(tvLogin, 0.6f, 1f);
            }
        });
    }
}
