package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.VO.OssCallbackResult;
import com.fcl.ccmall.entity.VO.OssPolicyResult;
import com.fcl.ccmall.service.OssService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/aliyun/oss")
public class OssController {

    @Resource
    private OssService ossService;

    @GetMapping("/policy")
    public CommonResult getPolicy() {
        OssPolicyResult policy = ossService.getPolicy();
        return CommonResult.success(policy);
    }

    @PostMapping("/callback")
    public CommonResult callback(HttpServletRequest request) {
        OssCallbackResult callback = ossService.callback(request);
        return CommonResult.success(callback);
    }
}
