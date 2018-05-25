package love.wintrue.com.lovestaff.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.utils.ActivityUtil;
import love.wintrue.com.lovestaff.widget.StateButton;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;

/**
 * Created by XDHG on 2018/5/25.
 */

public class ForgetPasswordActivity extends BaseActivity {
    @Bind(R.id.title_actionbar_login)
    CommonActionBar cab;
    @Bind(R.id.btn_forget_next)
    StateButton btnForgetNext;
    @Bind(R.id.btn_forget_next2)
    StateButton btnForgetNext2;
    @Bind(R.id.ll_forget_one)
    LinearLayout llForgetOne;
    @Bind(R.id.ll_forget_two)
    LinearLayout llForgetTwo;
    @Bind(R.id.tv_count_down)
    TextView tvCountDown;

    private Handler timerHandler;
    private int time = 60;
    Thread timeThread;
    MyTimerTask timerTask;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initView() {
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

        cab.setActionBarTitle("忘记密码");
        cab.setLeftImgBtn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnForgetNext2.setNormalBackgroundColor(colors);
        btnForgetNext.setNormalBackgroundColor(colors);
        btnForgetNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llForgetOne.setVisibility(View.GONE);
                llForgetTwo.setVisibility(View.VISIBLE);
                startCountDown();
            }
        });

        btnForgetNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("intentFrom", "ForgetPasswordActivity");
                ActivityUtil.next(THIS, ChangePasswordActivity.class, bundle, false);
            }
        });

        /**
         * 发送短信TextView点击
         */
        tvCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountDown();
            }
        });
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
