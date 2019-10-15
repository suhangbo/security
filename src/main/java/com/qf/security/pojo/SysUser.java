package com.qf.security.pojo;

public class SysUser {
    private Integer id;

    private String username;
    private String password;
    private String mobile;

    public SysUser(Integer id, String username, String password, String mobile) {
        this.mobile = mobile;
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public SysUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
