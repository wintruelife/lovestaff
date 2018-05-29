package love.wintrue.com.lovestaff.bean;

import android.net.Uri;

import java.io.Serializable;

import me.yokeyword.indexablerv.IndexableEntity;

/**
 * Created by XDHG on 2018/5/28.
 */

public class AddressBookItemBean implements Serializable,IndexableEntity {
    public Uri contactUri;
    private String id;
    private String avatarUrl;
    private String name;
    private String pinyin;
    private String job;

    public Uri getContactUri() {
        return contactUri;
    }

    public void setContactUri(Uri contactUri) {
        this.contactUri = contactUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String getFieldIndexBy() {
        return name;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        this.name = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {
        this.pinyin = pinyin;
    }
}
