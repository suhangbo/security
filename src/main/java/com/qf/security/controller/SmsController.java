package com.qf.security.controller;

import com.qf.security.service.SmsService;
import com.qf.security.validate.ValidateCode;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/sms/code")
public class SmsController {

    public final static String SMS_CODE_KEY = "SMS_CODE_KEY";

    @Resource
    private SmsService smsService;

    // Session的工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    // 发送短信
    @RequestMapping
    public void sendSms(HttpServletRequest request, String mobile) {
        // 生成随机数子的工具类
        RandomStringGenerator randomStringGenerator
                = new RandomStringGenerator.Builder().withinRange(new char[]{'0','9'}).build();

        //randomStringGenerator.generate(4) 生成0-9的四位随机字符串
        ValidateCode validateCode = new ValidateCode(randomStringGenerator.generate(4) ,120);
        sessionStrategy.setAttribute(new ServletWebRequest(request), SMS_CODE_KEY, validateCode);

        System.out.println("短信验证码：" + validateCode.getCode());

        smsService.send(validateCode.getCode(), mobile); //调用短信运营商的发送短信的功能
    }

    /**
    public static void main(String[] args) {
        RandomStringGenerator randomStringGenerator
                = new RandomStringGenerator.Builder()
                .withinRange(new char[]{'0','9'}, new char[]{'a', 'z'}, new char[]{'A', 'Z'}).build();

        System.out.println(randomStringGenerator.generate(10));
    }
     */
}
