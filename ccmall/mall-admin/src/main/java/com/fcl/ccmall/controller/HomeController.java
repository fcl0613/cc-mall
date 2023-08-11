package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.service.HomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Resource
    private HomeService homeService;

    @GetMapping("/content")
    public CommonResult getContent() {
        return homeService.getContent();
    }

    @GetMapping("/orderTable")
    public CommonResult getOrderTable() {
        return homeService.getOrderTable();
    }
}
