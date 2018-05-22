package love.wintrue.com.lovestaff.bean;

import java.io.Serializable;

/**
 * @author th
 * @desc
 * @time 2017/3/20 10:00
 */
public class BaseBean implements Serializable{
    private String error;
    private String msg;
    private int status;//返回结果（1：成功；0：失败）

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
