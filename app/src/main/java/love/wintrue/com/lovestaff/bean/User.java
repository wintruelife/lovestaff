package love.wintrue.com.lovestaff.bean;

import java.io.Serializable;
/**
 * @des: 用户信息
 * @author:th
 * @email:342592622@qq.com
 * @date: 2017/5/8 0008 下午 17:04
 */

public class User extends BaseBean implements Serializable {
    private String uid;
    private String token;
    private String nickname;
    private String icon;
    private int status;//返回结果（1：成功；0：失败）
    private String sessionId;
    private String userId;//用户ID

    private String agentNumber;//客户编号
    private String loginName;//登录名
    private String name;//用户名称
    private String gender;//性别

    private String mobile;//

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\''+ '\'' +
                ", token='" + token + '\'' +
                ", phone='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
