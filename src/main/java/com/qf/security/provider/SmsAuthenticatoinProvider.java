package com.qf.security.provider;

import com.qf.security.auth.UserSecurityService;
import com.qf.security.token.SmsAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 具体校验处理逻辑
 */
public class SmsAuthenticatoinProvider implements AuthenticationProvider {

    private UserSecurityService userSecurityService;

    public UserSecurityService getUserSecurityService() {
        return userSecurityService;
    }

    public void setUserSecurityService(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }

    /**
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsAuthenticationToken smsAuthenticationToken = (SmsAuthenticationToken)authentication;

        String mobile = (String)smsAuthenticationToken.getPrincipal();  //获取到电话


        UserDetails userDetails = userSecurityService.loadUserByUsername(mobile);

        SmsAuthenticationToken token = new SmsAuthenticationToken(userDetails, userDetails.getAuthorities());

        // 设置用户的其他的详细信息(登录一些)
        token.setDetails(smsAuthenticationToken.getDetails());

        return token;
    }

    /**
     * 该方法就是来判断，该Provider要处理哪一个Filter丢过来的Token,
     * 返回true, 上面的方法 authenticate(Authentication authentication)
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // 判断类型是否一致
        return authentication.isAssignableFrom(SmsAuthenticationToken.class);
    }

    public static void main(String[] args) {
        Class clazz = String.class;
        SmsAuthenticationToken smsAuthenticationToken = new SmsAuthenticationToken("ajfie");
        System.out.println(clazz.isAssignableFrom(smsAuthenticationToken.getClass()));
    }
}
