package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.AliPayDTO;
import com.fcl.ccmall.service.AliPayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/alipay")
public class AliPayController {
    @Resource
    private AliPayService aliPayService;

    /**
     * 退款接口
     * @return
     */
    @PostMapping("/return")
    public CommonResult returnPay(AliPayDTO aliPayDTO) {
        return aliPayService.returnPay(aliPayDTO);
    }
}
