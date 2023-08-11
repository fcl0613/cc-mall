package com.fcl.ccmall.config;

import com.fcl.ccmall.component.CustomMd5PasswordEncoder;
import com.fcl.ccmall.component.JwtAuthenticationTokenFilter;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonSecurityConfig {
    @Bean
    public JwtTokenUtils jwtTokenUtil() {
        return new JwtTokenUtils();
    }

    @Bean
    public CustomMd5PasswordEncoder customMd5PasswordEncoder() {
        return new CustomMd5PasswordEncoder();
    }

    @Bean
    public IgnoreUrlsConfig ignoreUrlsConfig() {
        return new IgnoreUrlsConfig();
    }

    @Bean
    public RestfulAccessDeniedHandler restfulAccessDeniedHandler() {
        return new RestfulAccessDeniedHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}
