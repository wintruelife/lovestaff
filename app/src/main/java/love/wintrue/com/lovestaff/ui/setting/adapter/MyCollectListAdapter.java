package love.wintrue.com.lovestaff.ui.setting.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.MApplication;
import love.wintrue.com.lovestaff.base.adapter.CommonBaseAdapter;
import love.wintrue.com.lovestaff.bean.BlackListInfoBean;
import love.wintrue.com.lovestaff.image.Handle;
import love.wintrue.com.lovestaff.widget.CircleImageView;

/**
 * Created by lhe on 2017/8/24.
 */

public class MyCollectListAdapter extends CommonBaseAdapter<BlackListInfoBean> {

    private Activity activity;
    private BlackListInfoBean blackListInfoBean;
    private Handler handler;

    public MyCollectListAdapter(Activity activity, Handler handler) {
        super(MApplication.getInstance());
        this.activity = activity;
        this.handler = handler;
    }


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.adapter_mycollectlist_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        blackListInfoBean = getList().get(position);
//        ImageLoaderUtils.createImageLoader(activity).displayImage(blackListInfoBean.getUrl()
//                , holder.adapterMycollectlistImg, ImageLoaderUtils.OPTIONS);
        holder.adapterMycollectlistTitle.setText(blackListInfoBean.getName());
        holder.adapterMycollectlistName.setText(blackListInfoBean.getName());
        holder.adapterMycollectlistTime.setText(blackListInfoBean.getTime());

        holder.adapterMycollectItme.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message msg = new Message();
                msg.what = position;
                handler.sendMessage(msg);
                return false;
            }
        });
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.adapter_mycollectlist_title)
        TextView adapterMycollectlistTitle;
        @Bind(R.id.adapter_mycollectlist_img)
        CircleImageView adapterMycollectlistImg;
        @Bind(R.id.adapter_mycollectlist_name)
        TextView adapterMycollectlistName;
        @Bind(R.id.adapter_mycollectlist_time)
        TextView adapterMycollectlistTime;
        @Bind(R.id.adapter_mycollect_itme)
        RelativeLayout adapterMycollectItme;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
