package love.wintrue.com.lovestaff.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseFragment;
import love.wintrue.com.lovestaff.base.adapter.AddressBookAdapter;
import love.wintrue.com.lovestaff.bean.AddressBookItemBean;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * Created by XDHG on 2018/5/28.
 */

public class AddressBookFragment extends BaseFragment {
    @Bind(R.id.indexableLayout)
    IndexableLayout indexableLayout;
    @Bind(R.id.et_search_title)
    EditText et_search_title;
    @Bind(R.id.iv_reinput)
    View iv_reinput;
    @Bind(R.id.tv_no_result)
    TextView tvNoResult;
    private AddressBookAdapter addressBookAdapter;
    private List<AddressBookItemBean> webSiteDataList;
    private String mQueryText;

    public static AddressBookFragment instance() {
        AddressBookFragment fragment = new AddressBookFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_addressbook;
    }

    @Override
    protected void initView() {
        webSiteDataList = new ArrayList<>();
        AddressBookItemBean abib = new AddressBookItemBean();
        abib.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1frmuto5qlzj30ia0notd8.jpg");
        abib.setName("张三");
        abib.setJob("董事长");
        AddressBookItemBean abib0 = new AddressBookItemBean();
        abib0.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1frjd77dt8zj30k80q2aga.jpg");
        abib0.setName("李四");
        abib0.setJob("总经理");
        AddressBookItemBean abib1 = new AddressBookItemBean();
        abib1.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1frjd4var2bj30k80q0dlf.jpg");
        abib1.setName("王五");
        abib1.setJob("技术总监");
        AddressBookItemBean abib2 = new AddressBookItemBean();
        abib2.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1frja502w5xj30k80od410.jpg");
        abib2.setName("张三");
        abib2.setJob("董事长");
        AddressBookItemBean abib3 = new AddressBookItemBean();
        abib3.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1fri9zqwzkoj30ql0w3jy0.jpg");
        abib3.setName("李四");
        abib3.setJob("总经理");
        AddressBookItemBean abib4 = new AddressBookItemBean();
        abib4.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1frg40vozfnj30ku0qwq7s.jpg");
        abib4.setName("王五");
        abib4.setJob("技术总监");
        AddressBookItemBean abib5 = new AddressBookItemBean();
        abib5.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1frevscw2wej30je0ps78h.jpg");
        abib5.setName("张三");
        abib5.setJob("董事长");
        AddressBookItemBean abib6 = new AddressBookItemBean();
        abib6.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1frepozc5taj30qp0yg7aq.jpg");
        abib6.setName("李四");
        abib6.setJob("总经理");
        AddressBookItemBean abib7 = new AddressBookItemBean();
        abib7.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1frepq6mfvdj30p00wcwmq.jpg");
        abib7.setName("王五");
        abib7.setJob("技术总监");
        AddressBookItemBean abib8 = new AddressBookItemBean();
        abib8.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1frepqtwifwj30no0ti47n.jpg");
        abib8.setName("张三");
        abib8.setJob("董事长");
        AddressBookItemBean abib9 = new AddressBookItemBean();
        abib9.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1frepr2rhxvj30qo0yjth8.jpg");
        abib9.setName("李四");
        abib9.setJob("总经理");
        AddressBookItemBean abib10 = new AddressBookItemBean();
        abib10.setAvatarUrl("http://ww1.sinaimg.cn/large/0065oQSqly1freprc128lj30sg15m12u.jpg");
        abib10.setName("王五");
        abib10.setJob("技术总监");
        webSiteDataList.add(abib);
        webSiteDataList.add(abib0);
        webSiteDataList.add(abib1);
        webSiteDataList.add(abib2);
        webSiteDataList.add(abib3);
        webSiteDataList.add(abib4);
        webSiteDataList.add(abib5);
        webSiteDataList.add(abib6);
        webSiteDataList.add(abib7);
        webSiteDataList.add(abib8);
        webSiteDataList.add(abib9);
        webSiteDataList.add(abib10);

        addressBookAdapter = new AddressBookAdapter(mContext, new AddressBookAdapter.SearchCallBack() {
            @Override
            public void searchEmpty() {
                tvNoResult.setVisibility(View.VISIBLE);
            }
        });
        indexableLayout.setAdapter(addressBookAdapter);
        addressBookAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<AddressBookItemBean>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, AddressBookItemBean entity) {

            }
        });

        addressBookAdapter.setDatas(webSiteDataList);

        et_search_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    bindQueryText(s.toString());
                } else {
                    tvNoResult.setVisibility(View.GONE);
                    addressBookAdapter.setDatas(webSiteDataList);
                }

                if (TextUtils.isEmpty(s.toString())) {
                    iv_reinput.setVisibility(View.GONE);
                } else {
                    iv_reinput.setVisibility(View.VISIBLE);
                }
            }
        });

        iv_reinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search_title.setText("");
                iv_reinput.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 根据newText 进行查找, 显示
     */
    public void bindQueryText(String newText) {
        if (webSiteDataList == null) {
            mQueryText = newText.toLowerCase();
        } else if (!TextUtils.isEmpty(newText)) {
            addressBookAdapter.getFilter().filter(newText.toLowerCase());
        }
    }
}
