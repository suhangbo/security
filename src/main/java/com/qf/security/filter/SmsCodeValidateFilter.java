package com.qf.security.filter;

import com.qf.security.controller.SmsController;
import com.qf.security.handler.MyAuthenticationFailureHandler;
import com.qf.security.validate.ValidateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证短信验证码的过滤器。
 */
@Component
public class SmsCodeValidateFilter extends OncePerRequestFilter {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(request.getMethod().equals("POST") && "/authentication/sms".equals(request.getRequestURI())) {


            try{
                validateSmsCode(request);
            }catch (InternalAuthenticationServiceException exception) {
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);
                return;
            }

            filterChain.doFilter(request, response);
        }else {
            filterChain.doFilter(request, response);
        }
    }

    private void validateSmsCode(HttpServletRequest request) {
        String smsCode = request.getParameter("smsCode"); //获取短信验证码

        // 取到验证码
        ValidateCode validateCode = (ValidateCode)sessionStrategy.getAttribute(new ServletWebRequest(request),
                SmsController.SMS_CODE_KEY);

        // 验证码不能为空或者
        if(StringUtils.isEmpty(smsCode) || "".equals(smsCode.trim())) {
            throw new InternalAuthenticationServiceException("短信验证码不能为空.");
        }

        if(validateCode.isExpire()) {  //是否过期
            throw new InternalAuthenticationServiceException("短信验证码过期.");
        }

        if(null == validateCode) {
            throw new InternalAuthenticationServiceException("短信验证码不存在.");
        }

        if(!smsCode.equals(validateCode.getCode())) {
            throw new InternalAuthenticationServiceException("短信验证码不正确");
        }
    }
}
