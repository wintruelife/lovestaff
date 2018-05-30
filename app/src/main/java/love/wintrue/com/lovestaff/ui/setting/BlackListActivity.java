package love.wintrue.com.lovestaff.ui.setting;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.bean.BlackListInfoBean;
import love.wintrue.com.lovestaff.ui.setting.adapter.BlackListAdapter;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;

/**
 * Created by a1314 on 2018/5/30.
 */

public class BlackListActivity extends BaseActivity {
    @Bind(R.id.common_action_bar)
    CommonActionBar commonActionBar;
    @Bind(R.id.activity_blacklist_view)
    PullToRefreshListView listview;
    private BlackListAdapter adapter;

    List<BlackListInfoBean> listInfoBeen = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_blacklist;
    }

    @Override
    public void initView() {
        commonActionBar.setActionBarTitle("黑名单");
        commonActionBar.setActionBarSeparationLineVisiable(View.GONE);
        commonActionBar.setLeftImgBtn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commonActionBar.setBackground(ContextCompat.getDrawable(THIS, R.drawable.title_bg));

        adapter = new BlackListAdapter(this);
        listview.setAdapter(adapter);
        listview.setMode(PullToRefreshBase.Mode.DISABLED);

        BlackListInfoBean blackListInfoBean = new BlackListInfoBean();
        blackListInfoBean.setName("李湘");
        blackListInfoBean.setTime("2018-07-09 09:13:13");
        BlackListInfoBean blackListInfoBean1 = new BlackListInfoBean();
        blackListInfoBean1.setName("豆瓣");
        blackListInfoBean1.setTime("2018-07-08 09:16:13");

        listInfoBeen.add(blackListInfoBean);
        listInfoBeen.add(blackListInfoBean1);

        adapter.setList(listInfoBeen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
