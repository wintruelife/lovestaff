package love.wintrue.com.lovestaff.ui.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kyleduo.switchbutton.SwitchButton;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.bean.AddressBookItemBean;
import love.wintrue.com.lovestaff.utils.ImageUtil;
import love.wintrue.com.lovestaff.widget.CircleImageView;
import love.wintrue.com.lovestaff.widget.ClearEditText;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;
import love.wintrue.com.lovestaff.widget.circularcontactview.ContactImageUtil;

/**
 * Created by XDHG on 2018/5/30.
 */

public class EditContactActivity extends BaseActivity {
    private int REQUEST_CODE = 120;
    @Bind(R.id.title_actionbar_login)
    CommonActionBar cab;
    @Bind(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @Bind(R.id.sb_text1)
    SwitchButton sb_text1;
    @Bind(R.id.et_contact_name)
    ClearEditText etContactName;
    @Bind(R.id.et_contact_phone)
    ClearEditText etContactPhone;
    @Bind(R.id.et_company_name)
    ClearEditText etCompanyName;
    @Bind(R.id.et_job)
    ClearEditText etJob;
    @Bind(R.id.et_email)
    ClearEditText etEmail;
    private String contactAvatarPath;
    private AddressBookItemBean addressBookItemBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_editcontact;
    }

    @Override
    public void initView() {
        addressBookItemBean = (AddressBookItemBean) getIntent().getSerializableExtra("contactDetail");
        cab.setRightTxtBtn(R.string.contact_edit_save, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sb_text1.isChecked()) {
                    Acp.getInstance(THIS).request(new AcpOptions.Builder()
                                    .setPermissions(Manifest.permission.WRITE_CONTACTS)
                                    .build(),
                            new AcpListener() {
                                @Override
                                public void onGranted() {
                                    //addContact(etContactName.getText().toString(), etContactPhone.getText().toString(), etEmail.getText().toString());
                                    updateContact(etContactName.getText().toString());
                                }

                                @Override
                                public void onDenied(List<String> permissions) {
                                    Toast.makeText(THIS, permissions.toString() + "权限拒绝", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        cab.setActionBarTitle("编辑名片");
        cab.setLeftBtn(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (null != addressBookItemBean) {
            etContactName.setText(addressBookItemBean.getName());
            etContactPhone.setText(addressBookItemBean.getPhone());
            ivAvatar.setImageBitmap(ContactImageUtil.getContactPhotoThumbnail(THIS, addressBookItemBean.getContactId()));
        }
    }

    @OnClick({R.id.iv_avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                choosePhoto();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            contactAvatarPath = pathList.get(0);
            Glide.with(THIS).load(pathList.get(0))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade().into(ivAvatar);
        }
    }

    /**
     * 开启图片选择器
     */
    private void choosePhoto() {
        ImgSelConfig config = new ImgSelConfig.Builder(loader)
                // 是否多选
                .multiSelect(false)
                // 确定按钮背景色
                .btnBgColor(Color.TRANSPARENT)
                .titleBgColor(ContextCompat.getColor(this, R.color.colorPrimary))
                // 使用沉浸式状态栏
                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .title("图片")
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(1)
                .needCrop(true)
                .build();
        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
    }

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            //目前用Glide进行测试
            Glide.with(context).load(path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bg_jz)
                    .error(R.drawable.bg_jz)
                    .crossFade().into(imageView);
        }
    };

    // 一个添加联系人信息的例子
    public void addContact(String name, String phoneNumber, String email) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phoneNumber)) {
            // 创建一个空的ContentValues
            ContentValues values = new ContentValues();
            // 向RawContacts.CONTENT_URI空值插入，
            // 先获取Android系统返回的rawContactId
            // 后面要基于此id插入值
            Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);
            values.clear();

            values.put(Data.RAW_CONTACT_ID, rawContactId);
            // 内容类型
            values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
            // 联系人名字
            values.put(StructuredName.GIVEN_NAME, name);
            // 向联系人URI添加联系人名字
            getContentResolver().insert(Data.CONTENT_URI, values);
            values.clear();

            values.put(Data.RAW_CONTACT_ID, rawContactId);
            values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
            // 联系人的电话号码
            values.put(Phone.NUMBER, phoneNumber);
            // 电话类型
            values.put(Phone.TYPE, Phone.TYPE_MOBILE);
            // 向联系人电话号码URI添加电话号码
            getContentResolver().insert(Data.CONTENT_URI, values);
            values.clear();

            values.put(Data.RAW_CONTACT_ID, rawContactId);
            values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
            // 联系人的Email地址
            values.put(Email.DATA, email);
            // 电子邮件的类型
            values.put(Email.TYPE, Email.TYPE_WORK);
            // 向联系人Email URI添加Email数据
            getContentResolver().insert(Data.CONTENT_URI, values);
            values.clear();

            if (null != contactAvatarPath) {
                //修改联系人的头像
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                // 将Bitmap压缩成PNG编码，质量为100%存储
                ImageUtil.getImage(contactAvatarPath).compress(Bitmap.CompressFormat.PNG, 100, os);
                byte[] avatar = os.toByteArray();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, avatar);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }
            Toast.makeText(this, "联系人数据添加成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "联系人姓名或电话不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateContact(String name) {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, "DISPLAY_NAME = '" + name + "'", null,
                null);
        if (cursor.moveToFirst()) {
            //获得联系人的id
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            //修改联系人的头像
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            // 将Bitmap压缩成PNG编码，质量为100%存储
            ImageUtil.getImage(contactAvatarPath).compress(Bitmap.CompressFormat.PNG, 100, os);
            byte[] avatar = os.toByteArray();
            ContentValues values = new ContentValues();
            values.put(Data.RAW_CONTACT_ID, contactId);
            values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, avatar);
            getContentResolver().update(Data.CONTENT_URI, values, null, null);
        }
    }
}
