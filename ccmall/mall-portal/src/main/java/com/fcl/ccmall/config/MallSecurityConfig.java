package com.fcl.ccmall.config;

import com.fcl.ccmall.service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

@Configuration
public class MallSecurityConfig {
    @Resource
    private CustomerService customerService;

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> customerService.loadUserByUsername(username);
    }
}
