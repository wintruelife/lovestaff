package love.wintrue.com.lovestaff.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.utils.AndroidUtil;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import love.wintrue.com.lovestaff.utils.PrefersUtil;
import love.wintrue.com.lovestaff.utils.StringUtils;

/**
 * 通用自定义样式AlertDialog
 *
 * @ClassName: CommonAlertDialog.java
 */
public class ForgetGestureDialog extends Dialog {

    private Activity context;
    private ResetPawwsordGesture resetPawwsordGesture;

    @Bind(R.id.txt_dialog_title)
    TextView txtDialogTitle;
    @Bind(R.id.txt_dialog_input)
    EditText txtDialogInput;
    @Bind(R.id.v_dialog_line)
    View vDialogLine;
    @Bind(R.id.btn_dialog_negative)
    Button btnDialogNegative;
    @Bind(R.id.v_line)
    View vLine;
    @Bind(R.id.btn_dialog_positive)
    Button btnDialogPositive;
    @Bind(R.id.ll_dialog_bottom_bar)
    LinearLayout llDialogBottomBar;
    @Bind(R.id.lin_container)
    LinearLayout linContainer;
    @Bind(R.id.layout_all)
    LinearLayout layoutAll;

    public ForgetGestureDialog(Activity context,ResetPawwsordGesture resetPawwsordGesture) {
        super(context, R.style.base_keyboard);
        this.context = context;
        this.resetPawwsordGesture = resetPawwsordGesture;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_dialog_forget);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        initView();

    }


    protected void initView() {

        int screenWidth = AndroidUtil.getScreenWidthByContext(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(3 * screenWidth / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutAll.setLayoutParams(layoutParams);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
    }


    @OnClick({R.id.btn_dialog_negative, R.id.btn_dialog_positive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_negative:
                dismiss();
                break;
            case R.id.btn_dialog_positive:
                String inputPaw = txtDialogInput.getText().toString();
                if(StringUtils.isEmpty(inputPaw)){
                    Toast.makeText(context,"请输入用户密码",Toast.LENGTH_LONG).show();
                }else {

//                    MApplication mApplication = (MApplication) context.getApplication();
                    String password = PrefersUtil.getInstance().getStrValue("password");
                    if(inputPaw.equals(password)){
                        if(resetPawwsordGesture != null){
                            resetPawwsordGesture.resetGesture();
                        }
                        dismiss();
                    }else {
                        Toast.makeText(context,"密码错误",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

    }

    public interface ResetPawwsordGesture{
        public void resetGesture();
    }
}
