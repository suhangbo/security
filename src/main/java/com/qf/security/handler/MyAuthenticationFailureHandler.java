package com.qf.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 失败处理
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        Map<String, Object> map = new HashMap<>();

        // 用户名或者秘密错误，在UserDetailsService中，将异常已经抛过来了
        if(exception instanceof InternalAuthenticationServiceException) {
            map.put("code", -2);
            map.put("msg", exception.getMessage());
        }else if(exception instanceof BadCredentialsException) {
            map.put("code", -1);
            map.put("msg", exception.getMessage());
        }else if(exception instanceof UsernameNotFoundException) {
            map.put("code", -2);
            map.put("msg", exception.getMessage());
        }

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(map));  //返回json数据
    }
}
