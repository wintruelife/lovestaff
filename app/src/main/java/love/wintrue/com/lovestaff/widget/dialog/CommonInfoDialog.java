package love.wintrue.com.lovestaff.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.utils.DensityUtil;

/**
 * Created by dell on 2016/8/25.
 */
public class CommonInfoDialog extends Dialog {
    private View rootView;
    private View titleView;
    private TextView titleTv;
    private TextView contentTv;
    private TextView cancelTv;
    private TextView okTv;
    private ViewGroup.LayoutParams params;
    private Activity activity;

    public CommonInfoDialog(Activity activity) {
        super(activity, R.style.alert_dialog);
        this.activity = activity;
        rootView = LayoutInflater.from(activity).inflate(R.layout.widget_common_info_dialog,null);
        int width = DensityUtil.getScreenWidth(activity) / 5 * 4;
        params = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleTv = (TextView) rootView.findViewById(R.id.common_info_dialog_title_tv);
        titleView = rootView.findViewById(R.id.common_info_dialog_title_ll);
        contentTv = (TextView) rootView.findViewById(R.id.common_info_dialog_content);
        cancelTv = (TextView) rootView.findViewById(R.id.common_info_dialog_cancel);
        okTv = (TextView) rootView.findViewById(R.id.common_info_dialog_ok);
        titleView.setVisibility(View.GONE);
        contentTv.setVisibility(View.GONE);
        cancelTv.setVisibility(View.GONE);
        okTv.setVisibility(View.GONE);
        setContentView(rootView,params);
    }

    public CommonInfoDialog setTitle(String title){
        titleTv.setText(title);
        titleView.setVisibility(TextUtils.isEmpty(title)?View.GONE:View.VISIBLE);
        return this;
    }

    public CommonInfoDialog setContent(String content){
        contentTv.setText(content);
        contentTv.setVisibility(TextUtils.isEmpty(content)?View.GONE:View.VISIBLE);
        return this;
    }

    public CommonInfoDialog setCancelBtn(String cancelStr, final View.OnClickListener onClickListener){
        cancelTv.setText(cancelStr);
        cancelTv.setVisibility(TextUtils.isEmpty(cancelStr)?View.GONE:View.VISIBLE);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(onClickListener != null){
                    onClickListener.onClick(v);
                }
            }
        });
        return this;
    }

    public CommonInfoDialog setOkBtn(String okStr, final View.OnClickListener onClickListener){
        okTv.setText(okStr);
        okTv.setVisibility(TextUtils.isEmpty(okStr)?View.GONE:View.VISIBLE);
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(onClickListener != null){
                    onClickListener.onClick(v);
                }
            }
        });
        return this;
    }
}
