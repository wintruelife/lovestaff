package love.wintrue.com.lovestaff.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.ui.mine.MineActivity;
import love.wintrue.com.lovestaff.utils.ActivityUtil;
import love.wintrue.com.lovestaff.utils.AnimationUtils;
import love.wintrue.com.lovestaff.utils.Util;
import love.wintrue.com.lovestaff.widget.ClearEditText;
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
    @Bind(R.id.btn_register_next2)
    StateButton btnRegisterNext2;
    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.ll_login_view)
    LinearLayout llLoginView;
    @Bind(R.id.ll_register_view)
    LinearLayout llRegisterView;
    @Bind(R.id.ll_register_view2)
    LinearLayout llRegisterView2;
    @Bind(R.id.iv_triangle)
    ImageView ivTriangle;
    @Bind(R.id.tv_count_down)
    TextView tvCountDown;
    @Bind(R.id.et_account_register)
    ClearEditText etAccountRegister;
    @Bind(R.id.tv_forget_pwd)
    TextView tvForgetPwd;

    private int[] start_location;
    private int[] end_location;
    private boolean registerHasNext;

    private Handler timerHandler;
    private int time = 60;
    Thread timeThread;
    MyTimerTask timerTask;

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
        ActivityUtil.next(RegisterAndLoginActivity.this, MineActivity.class);
        timerTask = new MyTimerTask();
        timerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                int tempTime = msg.what;
                if (time <= 0) {
                    tvCountDown.setEnabled(true);
                    tvCountDown.setTextColor(ContextCompat.getColor(THIS, R.color.color_31b3ef));
                    tvCountDown.setText("重新发送短信");
                    time = 60;
                } else {
                    tvCountDown.setText(tempTime + "秒后重新发送短信");
                }
            }
        };

        cab.setActionBarTitle("爱员工");
        cab.setLeftImgBtn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnLogin.setNormalBackgroundColor(colors);
        btnRegisterNext.setNormalBackgroundColor(colors);
        btnRegisterNext2.setNormalBackgroundColor(colors);
    }

    @OnClick({R.id.btn_login, R.id.tv_login, R.id.tv_register, R.id.btn_register_next, R.id.btn_register_next2, R.id.tv_count_down, R.id.tv_forget_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                ActivityUtil.next(THIS, AddressBookActivity.class);
                break;
            case R.id.tv_login:
                llLoginView.setVisibility(View.VISIBLE);
                llRegisterView.setVisibility(View.GONE);
                llRegisterView2.setVisibility(View.GONE);
                startAnim("moveLeft");
                break;
            case R.id.tv_register:
                llLoginView.setVisibility(View.GONE);
                if (!registerHasNext) {
                    llRegisterView2.setVisibility(View.GONE);
                    llRegisterView.setVisibility(View.VISIBLE);
                } else {
                    llRegisterView2.setVisibility(View.VISIBLE);
                    llRegisterView.setVisibility(View.GONE);
                }

                startAnim("moveRight");
                break;
            case R.id.btn_register_next:
                if (!Util.isValidMobileNumber(etAccountRegister.getText().toString())) {
                    showToastMsg("请输入正确的手机号码");
                    return;
                }
                registerHasNext = true;
                llLoginView.setVisibility(View.GONE);
                llRegisterView.setVisibility(View.GONE);
                llRegisterView2.setVisibility(View.VISIBLE);

                startCountDown();
                break;
            case R.id.btn_register_next2:
                ActivityUtil.next(THIS, ImproveInformationActivity.class);
                break;
            case R.id.tv_count_down:
                startCountDown();
                break;
            case R.id.tv_forget_pwd:
                ActivityUtil.next(THIS, ForgetPasswordActivity.class);
                break;
        }
    }

    /**
     * 开始动画
     */
    private void startAnim(String direction) {
        start_location = new int[2];
        tvLogin.getLocationInWindow(start_location);
        end_location = new int[2];
        tvRegister.getLocationInWindow(end_location);
        AnimationUtils.translation(ivTriangle, start_location[0], end_location[0], null, 300, "translationX", direction);
        if (TextUtils.equals("moveLeft", direction)) {
            AnimationUtils.alphaAnimator(tvRegister, 1f, 0.6f);
            AnimationUtils.alphaAnimator(tvLogin, 0.6f, 1f);
        } else {
            AnimationUtils.alphaAnimator(tvLogin, 1f, 0.6f);
            AnimationUtils.alphaAnimator(tvRegister, 0.6f, 1f);
        }
    }

    /**
     * 开始倒计时
     */
    private void startCountDown() {
        tvCountDown.setEnabled(false);
        tvCountDown.setTextColor(ContextCompat.getColor(THIS, R.color.color_969696));

        timeThread = new Thread(timerTask);
        timeThread.start();
    }

    /**
     * 计时线程
     */
    class MyTimerTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                time--;
                Message msg = new Message();
                msg.what = time;
                timerHandler.sendMessage(msg);
                if (time <= 0)
                    break;
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
