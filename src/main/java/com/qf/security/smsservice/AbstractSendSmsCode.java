package com.qf.security.smsservice;

public abstract class AbstractSendSmsCode {

    private String accessKeyId;  //运营商提供的key

    private String accessKeySecret; // 运营商提供的secret

    private String templateId; //模板ID

    private String code; //随机验证码

    private String mobile; //手机号

    public AbstractSendSmsCode(String accessKeyId, String accessKeySecret, String templateId, String code, String mobile) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.templateId = templateId;
        this.code = code;
        this.mobile = mobile;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    //不同运营商发送短信逻辑不同
    public abstract Object sendSms();
}
