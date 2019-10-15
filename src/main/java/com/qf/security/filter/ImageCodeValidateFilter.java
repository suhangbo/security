package com.qf.security.filter;

import com.qf.security.controller.ValidataCodeController;
import com.qf.security.handler.MyAuthenticationFailureHandler;
import com.qf.security.validate.ImageCode;
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
 * 一次性的过滤器，过滤之前走，过滤之后不走了
 */
@Component
public class ImageCodeValidateFilter extends OncePerRequestFilter {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(request.getMethod().equals("POST") && "/authentication/form".equals(request.getRequestURI())) {


            try{
                validateImageCode(request);
            }catch (InternalAuthenticationServiceException exception) {
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);
                return;
            }

            filterChain.doFilter(request, response);
        }else {
            filterChain.doFilter(request, response);
        }
    }

    private void validateImageCode(HttpServletRequest request) {
        String validateCode = request.getParameter("validateCode"); //获取验证码

        // 取到验证码
        ImageCode imageCode = (ImageCode)sessionStrategy.getAttribute(new ServletWebRequest(request),
                ValidataCodeController.VALIDATE_CODE_KEY);

        // 验证码不能为空或者
        if(StringUtils.isEmpty(validateCode) || "".equals(validateCode.trim())) {
            throw new InternalAuthenticationServiceException("验证不能为空.");
        }

        if(imageCode.isExpire()) {  //是否过期
            throw new InternalAuthenticationServiceException("验证码过期.");
        }

        if(null == imageCode) {
            throw new InternalAuthenticationServiceException("验证码不存在.");
        }

        if(!validateCode.equals(imageCode.getCode())) {
            throw new InternalAuthenticationServiceException("验证码不正确");
        }
    }
}
