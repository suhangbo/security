package com.qf.security.auth;

import com.qf.security.pojo.SysUser;
import com.qf.security.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Arrays;

// 该类的作用是处理用户登录名和密码
@Component
public class UserSecurityService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserSecurityService.class);

    @Resource
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("用户名或者电话：" + username);

        SysUser sysUser = null;

        try{
            sysUser = sysUserService.getSysUserByUsenameOrMobile(username);
        }catch (EmptyResultDataAccessException exception) {  //没有对应的用户名异常
            throw new UsernameNotFoundException("用户或密码错误");
        }

        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }else {
            /**
             * User第一参数是：用户名
             *     第二个参数是：pssword, 是从数据库查出来的
             *     第三个参数是: 权限
             */
            User user =  null;

            try{
                user = new User(username,
                        sysUser.getPassword(),
                        Arrays.asList(new SimpleGrantedAuthority("admin")));
            }catch (InternalAuthenticationServiceException exception) {
                throw exception;  // 在此处，将异常接着往外抛，抛给AuthenticationFailureHandler处理
            }
            return user;
        }
    }

}
