package love.wintrue.com.lovestaff.bean;

/**
 * Created by lhe on 2017/9/7.
 */

public class GesturePasswordBean extends BaseBean {
    private String id;

    private String custNo;
    private String code;
    private String state;//状态（enable:可用；disable:不可用）
    private String createTime;

    private int mUnLockCount = 5;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getmUnLockCount() {
        return mUnLockCount;
    }

    public void setmUnLockCount(int mUnLockCount) {
        this.mUnLockCount = mUnLockCount;
    }
}
