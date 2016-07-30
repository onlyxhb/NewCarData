package com.xhb.onlystar.bean;

public class User {
    private String DeptCode;//部门代码
    private String Password;//口令
    private String TrueName;//真实姓名
    private String UserId;//用户id
    private String UserName;//用户名
    private String dwdm;//单位代码
    private String dwmc;//单位名称
    private String grdm;//员工代码
    private String grlb;//个人类别

    public User() {
    }

    public User(String deptCode, String password, String trueName, String userId, String userName, String dwdm,
                String dwmc, String grdm, String grlb) {
        super();
        DeptCode = deptCode;
        Password = password;
        TrueName = trueName;
        UserId = userId;
        UserName = userName;
        this.dwdm = dwdm;
        this.dwmc = dwmc;
        this.grdm = grdm;
        this.grlb = grlb;
    }

    public String getDeptCode() {
        return DeptCode;
    }

    public void setDeptCode(String deptCode) {
        DeptCode = deptCode;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getTrueName() {
        return TrueName;
    }

    public void setTrueName(String trueName) {
        TrueName = trueName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDwdm() {
        return dwdm;
    }

    public void setDwdm(String dwdm) {
        this.dwdm = dwdm;
    }

    public String getDwmc() {
        return dwmc;
    }

    public void setDwmc(String dwmc) {
        this.dwmc = dwmc;
    }

    public String getGrdm() {
        return grdm;
    }

    public void setGrdm(String grdm) {
        this.grdm = grdm;
    }

    public String getGrlb() {
        return grlb;
    }

    public void setGrlb(String grlb) {
        this.grlb = grlb;
    }


}
