package com.fcl.ccmall.service;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.LoginDTO;
import com.fcl.ccmall.entity.DTO.RegisterDTO;
import com.fcl.ccmall.entity.DTO.SendMessageDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomerService {
    CommonResult login(LoginDTO loginDTO);
    CommonResult getCustomerInfo();
    UserDetails loadUserByUsername(String username);

    CommonResult register(RegisterDTO registerDTO);

    CommonResult sendMessage(SendMessageDTO sendMessageDTO);

    CommonResult getMyPageContent();

    CommonResult updateAvatar(String avatar);

    CommonResult updateGender(Integer gender);

    CommonResult updateNickName(String name);
}
