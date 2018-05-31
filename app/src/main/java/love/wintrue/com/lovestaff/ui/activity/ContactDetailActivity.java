package love.wintrue.com.lovestaff.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import butterknife.Bind;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.bean.AddressBookItemBean;
import love.wintrue.com.lovestaff.utils.ActivityUtil;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;
import love.wintrue.com.lovestaff.widget.circularcontactview.CircularContactView;

/**
 * Created by XDHG on 2018/5/30.
 */

public class ContactDetailActivity extends BaseActivity {
    @Bind(R.id.title_actionbar_login)
    CommonActionBar cab;
    @Bind(R.id.iv_contact_avatar)
    CircularContactView ivContactAvatar;
    @Bind(R.id.tv_contact_name)
    TextView tvContactName;
    @Bind(R.id.tv_contact_phone)
    TextView tvContactPhone;
    @Bind(R.id.tv_contact_email)
    TextView tv_ContactEmail;
    private AddressBookItemBean addressBookItemBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_contactdetail;
    }

    @Override
    public void initView() {
        addressBookItemBean = (AddressBookItemBean) getIntent().getSerializableExtra("contactDetail");
        cab.setRightTxtBtn(R.string.contact_detail_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("contactDetail", addressBookItemBean);
                ActivityUtil.next(THIS, EditContactActivity.class, bundle, false);
            }
        });
        cab.setActionBarTitle("");
        cab.setLeftImgBtn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (null != addressBookItemBean) {
            tvContactName.setText(addressBookItemBean.getName());
            tvContactPhone.setText(addressBookItemBean.getPhone());
            //ivContactAvatar.setImageBitmap(ContactImageUtil.getContactPhotoThumbnail(THIS, addressBookItemBean.getContactId()));
//            Glide.with(THIS).load(addressBookItemBean.getAvatarUrl())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .crossFade().
//                    into(ivContactAvatar);
            final String characterToShow = TextUtils.isEmpty(addressBookItemBean.getName()) ? "" : addressBookItemBean.getName().substring(0, 1).toUpperCase(Locale.getDefault());
            ivContactAvatar.setTextAndBackgroundColor(characterToShow, addressBookItemBean.getBackgroundColor());
        }
    }
}
