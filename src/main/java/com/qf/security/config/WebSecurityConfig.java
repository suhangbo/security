package com.qf.security.config;

import com.qf.security.auth.UserSecurityService;
import com.qf.security.filter.ImageCodeValidateFilter;
import com.qf.security.filter.SmsCodeValidateFilter;
import com.qf.security.handler.MyAuthenticationFailureHandler;
import com.qf.security.handler.MySuccessAuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 该bean的作用是，在UserDetailsService接口的loadUserByUsername返回的UserDetail中包含了
     * password, 该bean就将用户从页面提交过来的密码进行处理，处理之后与UserDetail中密码进行比较。
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private MySuccessAuthenticationHandler mySuccessAuthenticationHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private ImageCodeValidateFilter imageCodeValidateFilter;

    @Autowired
    private SmsCodeValidateFilter smsCodeValidateFilter;

    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        // jdbcTokenRepository.setCreateTableOnStartup(false);  //true, 自动创建表，

        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 意思是将图片验证码过滤器，加载用户名密码验证过滤器之前
        http.addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class) //
                .addFilterBefore(smsCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()  //使用form进行登录
            .loginPage("/login.html")   //指定登录页面
                .loginProcessingUrl("/authentication/form")  //表示form往哪里进行提交
                .successHandler(mySuccessAuthenticationHandler)   //成功后的处理
                .failureHandler(myAuthenticationFailureHandler)   //失败处理
                .and()
                .rememberMe()
                .tokenValiditySeconds(36000000)
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userSecurityService)    // 因为用户传入过来的token, 需要再次进行校验
                .alwaysRemember(true)
                .and()  //表示进行其他的配置
                .authorizeRequests()   //表示所有的都需要认证
                .antMatchers("/login.html", "/js/**", "/css/**", "/sms/code",
                        "/validate/code").permitAll() //意思是让登录页面直接过
                .anyRequest() // 对于所有的请求
                .authenticated()
                .and()
                .csrf().disable()
                .apply(smsAuthenticationConfig);  //引用手机号登录整个逻辑
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("1"));
    }
}
