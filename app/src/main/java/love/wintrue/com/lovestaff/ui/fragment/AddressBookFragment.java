package love.wintrue.com.lovestaff.ui.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseFragment;
import love.wintrue.com.lovestaff.base.MApplication;
import love.wintrue.com.lovestaff.base.adapter.AddressBookAdapter;
import love.wintrue.com.lovestaff.bean.AddressBookItemBean;
import love.wintrue.com.lovestaff.ui.activity.ContactDetailActivity;
import love.wintrue.com.lovestaff.utils.ACache;
import love.wintrue.com.lovestaff.utils.ActivityUtil;
import love.wintrue.com.lovestaff.widget.circularcontactview.ContactsQuery;
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
    @Bind(R.id.tv_no_content)
    TextView tvNoContent;
    @Bind(R.id.ll_bottom_view)
    LinearLayout llBottomView;
    @Bind(R.id.ll_add_contact)
    LinearLayout llAddContact;
    @Bind(R.id.ll_import_contact)
    LinearLayout llImportContact;
    @Bind(R.id.tv_bottom_separation)
    TextView tvBottomSeparation;
    private AddressBookAdapter addressBookAdapter;
    private List<AddressBookItemBean> webSiteDataList;
    private String mQueryText;
    private int mAddressBookType;

    public static AddressBookFragment instance(int addressBookType) {
        AddressBookFragment fragment = new AddressBookFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.mAddressBookType = addressBookType;
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        if (TextUtils.equals(ACache.get(MApplication.getInstance()).getAsString("hasImport"), "true")) {
            setWhetherLazyLoad(false);
        }
        return R.layout.fragment_addressbook;
    }

    @Override
    protected void initView() {
        if (mAddressBookType == 0) {
            llBottomView.setVisibility(View.GONE);
            tvBottomSeparation.setVisibility(View.GONE);
        } else {
            llBottomView.setVisibility(View.VISIBLE);
            tvBottomSeparation.setVisibility(View.VISIBLE);
        }
        webSiteDataList = new ArrayList<>();
        addressBookAdapter = new AddressBookAdapter(mContext, new AddressBookAdapter.SearchCallBack() {
            @Override
            public void searchEmpty() {
                tvNoResult.setVisibility(View.VISIBLE);
            }
        }, mAddressBookType);

        addressBookAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<AddressBookItemBean>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition,AddressBookItemBean entity) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("contactDetail", entity);
                ActivityUtil.next(getActivity(), ContactDetailActivity.class, bundle, false);
            }
        });
        indexableLayout.setAdapter(addressBookAdapter);

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

    @OnClick({R.id.ll_import_contact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_import_contact:
                Acp.getInstance(getActivity()).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.READ_CONTACTS)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                webSiteDataList.clear();
                                tvNoContent.setVisibility(View.INVISIBLE);
                                showWaitDialog("正在导入...");
                                ViewTreeObserver vto = indexableLayout.getViewTreeObserver();
                                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        indexableLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                        hideWaitDialog();
                                    }
                                });

                                getContacts();
                                Collections.sort(webSiteDataList, new Comparator<AddressBookItemBean>() {
                                    @Override
                                    public int compare(AddressBookItemBean lhs, AddressBookItemBean rhs) {
                                        char lhsFirstLetter = TextUtils.isEmpty(lhs.getName()) ? ' ' : lhs.getName().charAt(0);
                                        char rhsFirstLetter = TextUtils.isEmpty(rhs.getName()) ? ' ' : rhs.getName().charAt(0);
                                        int firstLetterComparison = Character.toUpperCase(lhsFirstLetter) - Character.toUpperCase(rhsFirstLetter);
                                        if (firstLetterComparison == 0)
                                            return lhs.getName().compareTo(rhs.getName());
                                        return firstLetterComparison;
                                    }
                                });
                                addressBookAdapter.setDatas(webSiteDataList);
                                ACache.get(mContext).put("hasImport", "true");
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                Toast.makeText(getActivity(), permissions.toString() + "权限拒绝", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }

    @Override
    protected void lazyLoad() {
        webSiteDataList.clear();
        if (mAddressBookType == 0) {
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
            addressBookAdapter.setDatas(webSiteDataList);
        } else {
            if (TextUtils.equals(ACache.get(mContext).getAsString("hasImport"), "true")) {
                getContacts();
                Collections.sort(webSiteDataList, new Comparator<AddressBookItemBean>() {
                    @Override
                    public int compare(AddressBookItemBean lhs, AddressBookItemBean rhs) {
                        char lhsFirstLetter = TextUtils.isEmpty(lhs.getName()) ? ' ' : lhs.getName().charAt(0);
                        char rhsFirstLetter = TextUtils.isEmpty(rhs.getName()) ? ' ' : rhs.getName().charAt(0);
                        int firstLetterComparison = Character.toUpperCase(lhsFirstLetter) - Character.toUpperCase(rhsFirstLetter);
                        if (firstLetterComparison == 0)
                            return lhs.getName().compareTo(rhs.getName());
                        return firstLetterComparison;
                    }
                });
                addressBookAdapter.setDatas(webSiteDataList);
            } else {
                tvNoContent.setVisibility(View.VISIBLE);
            }
        }
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

    private void getContacts() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor;
        try {
            cursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI
                    },
                    null, null, ContactsQuery.SORT_ORDER);
        } catch (Exception e) {
            cursor = null;
        }

        if (null == cursor)
            return;

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AddressBookItemBean info = new AddressBookItemBean();
                info.setContactId(String.valueOf(cursor.getInt(0)));
                info.setName(cursor.getString(1));
                info.setPhone(cursor.getString(2));
                info.setAvatarUrl(cursor.getString(3));
                webSiteDataList.add(info);
            }
        }
        cursor.close();
    }
}
