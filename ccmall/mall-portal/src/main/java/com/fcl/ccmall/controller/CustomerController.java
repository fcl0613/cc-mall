package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.LoginDTO;
import com.fcl.ccmall.entity.DTO.RegisterDTO;
import com.fcl.ccmall.entity.DTO.SendMessageDTO;
import com.fcl.ccmall.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private CustomerService customerService;

    @PostMapping("/login")
    public CommonResult login(@RequestBody LoginDTO loginDTO) {
        return customerService.login(loginDTO);
    }

    @PostMapping("/register")
    public CommonResult register(@RequestBody RegisterDTO registerDTO) {
        return customerService.register(registerDTO);
    }

    @PostMapping("/send/message")
    public CommonResult sendMessage(@RequestBody SendMessageDTO sendMessageDTO) {
        return customerService.sendMessage(sendMessageDTO);
    }

    /**
     * my页面的相关信息
     * @return
     */
    @GetMapping("/my/content")
    public CommonResult getMyPageContent() {
        return customerService.getMyPageContent();
    }

    @PostMapping("/update/avatar")
    public CommonResult updateAvatar(@RequestParam("ul") String avatar) {
        return customerService.updateAvatar(avatar);
    }

    @PostMapping("/update/gender")
    public CommonResult updateGender(@RequestParam("gender") Integer gender) {
        return customerService.updateGender(gender);
    }

    @PostMapping("/update/nickName")
    public CommonResult updateNickName(@RequestParam("name") String name) {
        return customerService.updateNickName(name);
    }

    @GetMapping("/info")
    public CommonResult getCustomerInfo() {
        return customerService.getCustomerInfo();
    }

    @GetMapping("/test")
    public String test() {
        return "2344343";
    }
}
