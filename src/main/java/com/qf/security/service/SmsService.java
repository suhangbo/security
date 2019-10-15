package com.qf.security.service;

import com.aliyuncs.CommonResponse;
import com.qf.security.smsservice.AbstractSendSmsCode;
import com.qf.security.smsservice.AliSmsSendCodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.templateId}")
    private String templateId;

    @Value("${aliyun.sms.sign}")
    private String sign;

    // 发送短信, 要调用第三方的短信运营商
    public void send(String code, String mobile) {
        // http://wwww.cloud.alibaba/sms
        /**
        System.out.println("模板Id为：" + templateId);
        System.out.println("往手机号为：" + mobile + " 发送的验证码为：" + code);
         */

        AbstractSendSmsCode abstractSendSmsCode =
                new AliSmsSendCodeService(accessKeyId, accessKeySecret, templateId, code, mobile, sign);

        CommonResponse commonResponse = (CommonResponse)abstractSendSmsCode.sendSms();

        /*
          101
          102
          103
         */
        System.out.println("短信发送状态: " + commonResponse.getHttpStatus());  //返回状态码
        // 入库
    }
}
