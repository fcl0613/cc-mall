package com.fcl.ccmall.service;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.LoginDTO;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

public interface AdminService {
    CommonResult Login(LoginDTO loginDTO);
    CommonResult getAdminInfo(HttpServletRequest request);
    UserDetails loadUserByUsername(String username);
}
