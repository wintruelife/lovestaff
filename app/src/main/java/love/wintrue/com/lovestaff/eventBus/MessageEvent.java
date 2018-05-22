package love.wintrue.com.lovestaff.eventBus;

import android.os.Bundle;

/**
 * Created by dell on 2017/5/5.
 */

public class MessageEvent {

    public static final String MESSAGE_UNREAD_CHANGE = "MESSAGE_UNREAD_CHANGE";//未读消息数量变动
    public static final String MESSAGE_UPDATEuI = "MESSAGE_UPDATEuI";//未读消息更新小红点
    public static final String MESSAGE_UPDAT_FC = "MESSAGE_UPDAT_FC";//系统消息浮窗
    public static final String MESSAGE_HIDE_FC = "MESSAGE_HIDE_FC";//切换后台影藏息浮窗

    private int num;
    private String mType;
    private boolean isChangeMessageView;
    private Bundle mBundle;

    public MessageEvent(String type) {
        mType = type;
    }
    public MessageEvent(String type, int num) {
        mType= type;
        this.num = num;
    }
    public MessageEvent(String type, Bundle bd) {
        mType = type;
        mBundle = bd;
    }

    public String getType() {
        return mType;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isChangeMessageView() {
        return isChangeMessageView;
    }

    public void setChangeMessageView(boolean changeMessageView) {
        isChangeMessageView = changeMessageView;
    }

}
