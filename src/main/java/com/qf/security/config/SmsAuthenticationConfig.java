package com.qf.security.config;

import com.qf.security.auth.UserSecurityService;
import com.qf.security.filter.SmsAuthenticationFilter;
import com.qf.security.handler.MyAuthenticationFailureHandler;
import com.qf.security.handler.MySuccessAuthenticationHandler;
import com.qf.security.provider.SmsAuthenticatoinProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sun.net.www.http.HttpClient;

@Configuration
public class SmsAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private MySuccessAuthenticationHandler mySuccessAuthenticationHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private UserSecurityService userSecurityService;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 实例化过滤器
        SmsAuthenticationFilter filter = new SmsAuthenticationFilter();

        // 设置AuthenticatoinManager, 因为filter和Provider中间的桥梁就是 AuthenticationManager
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        filter.setAuthenticationSuccessHandler(mySuccessAuthenticationHandler);   //设置成功后的处理逻辑
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);


        SmsAuthenticatoinProvider smsAuthenticatoinProvider = new SmsAuthenticatoinProvider();

        /**
         *  因为在 SmsAuthenticatoinProvider要根据电话查询用户信息
         *  UserDetails userDetails = userSecurityService.loadUserByUsername(mobile);
         */
        smsAuthenticatoinProvider.setUserSecurityService(userSecurityService);

        // 统一设置Filter和Provider
        http.authenticationProvider(smsAuthenticatoinProvider)
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
