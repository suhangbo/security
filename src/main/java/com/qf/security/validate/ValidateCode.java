package com.qf.security.validate;

import java.time.LocalDateTime;

// 发送短信的pojo类
public class ValidateCode {
    private String code;
    private LocalDateTime expire;

    public ValidateCode() {
    }

    public ValidateCode(String code, int num) {
        this.code = code;
        this.expire = LocalDateTime.now().plusSeconds(num);
    }

    //判断验证时间是否有效
    public boolean isExpire() {
        return LocalDateTime.now().isAfter(this.expire);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpire() {
        return expire;
    }

    public void setExpire(LocalDateTime expire) {
        this.expire = expire;
    }
}
