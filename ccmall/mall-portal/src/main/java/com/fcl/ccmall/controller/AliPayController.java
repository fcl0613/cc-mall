package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.AliPayDTO;
import com.fcl.ccmall.service.AliPayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/alipay")
public class AliPayController {

    @Resource
    private AliPayService payService;

    @PostMapping("/pay")
    public CommonResult aliPay() {
        return payService.aliPay();
    }

    /**
     * 支付接口成功回调
     * @return
     */
    @PostMapping("/notify")
    public CommonResult payNotify(AliPayDTO aliPayDTO) {
        return payService.payNotify(aliPayDTO);
    }

    /**
     * 退款接口
     * @return
     */
    @PostMapping("/return")
    public CommonResult returnPay(AliPayDTO aliPayDTO) {
        return payService.returnPay(aliPayDTO);
    }
}
