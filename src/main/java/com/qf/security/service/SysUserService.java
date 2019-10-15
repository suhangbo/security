package com.qf.security.service;

import com.qf.security.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SysUserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SysUser getSysUserByUsenameOrMobile(String usernameOrMobile) throws EmptyResultDataAccessException {
        String sql = "select * from sys_user where username = ? or mobile = ?";  // 耦合度

        SysUser sysUser = jdbcTemplate.queryForObject(sql,
                    new BeanPropertyRowMapper<SysUser>(SysUser.class), usernameOrMobile, usernameOrMobile);

        return sysUser;
    }
}
