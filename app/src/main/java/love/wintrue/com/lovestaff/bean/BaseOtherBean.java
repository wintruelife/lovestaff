package love.wintrue.com.lovestaff.bean;

import java.io.Serializable;

/**
 * @author th
 * @desc
 * @time 2017/3/20 10:00
 */
public class BaseOtherBean implements Serializable{
    private String error;
    private String msg;
    private String status;//返回结果（1：成功；0：失败）

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
