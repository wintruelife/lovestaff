package love.wintrue.com.lovestaff.ui.setting.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.MApplication;
import love.wintrue.com.lovestaff.base.adapter.CommonBaseAdapter;
import love.wintrue.com.lovestaff.bean.BlackListInfoBean;
import love.wintrue.com.lovestaff.utils.ImageLoaderUtils;
import love.wintrue.com.lovestaff.widget.CircleImageView;

/**
 * Created by lhe on 2017/8/24.
 */

public class BlackListAdapter extends CommonBaseAdapter<BlackListInfoBean> {

    private Activity activity;
    private BlackListInfoBean blackListInfoBean;

    public BlackListAdapter(Activity activity) {
        super(MApplication.getInstance());
        this.activity = activity;
    }


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.adapter_blacklist_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        blackListInfoBean = getList().get(position);
//        ImageLoaderUtils.createImageLoader(activity).displayImage(blackListInfoBean.getUrl()
//                , holder.adapterBlacklistImg, ImageLoaderUtils.OPTIONS);
        holder.adapterBlacklistName.setText(blackListInfoBean.getName());
        holder.adapterBlacklistTime.setText(blackListInfoBean.getTime());
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.adapter_blacklist_img)
        CircleImageView adapterBlacklistImg;
        @Bind(R.id.adapter_blacklist_name)
        TextView adapterBlacklistName;
        @Bind(R.id.adapter_blacklist_time)
        TextView adapterBlacklistTime;
        @Bind(R.id.adapter_blacklist_cancle)
        TextView adapterBlacklistCancle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
