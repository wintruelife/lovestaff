package love.wintrue.com.lovestaff.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import love.wintrue.com.lovestaff.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import love.wintrue.com.lovestaff.utils.AndroidUtil;

/**
 * 通用自定义样式AlertDialog
 *
 * @ClassName: CommonAlertDialog.java
 */
public class CallServiceDialog extends Dialog {
    @Bind(R.id.txt_dialog_title)
    TextView txtDialogTitle;
    @Bind(R.id.txt_dialog_message)
    TextView txtDialogMessage;
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
    private Context context;

    public CallServiceDialog(Context context) {
        super(context, R.style.base_keyboard);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_dialog_call_service);
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

    @OnClick(R.id.btn_dialog_negative)
    public void onViewClicked() {
        dismiss();
    }

    @OnClick({R.id.btn_dialog_negative, R.id.btn_dialog_positive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_negative:
                dismiss();
                break;
            case R.id.btn_dialog_positive:
                dismiss();
                dialPhoneNumber("028-82869536");
                break;
        }
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
