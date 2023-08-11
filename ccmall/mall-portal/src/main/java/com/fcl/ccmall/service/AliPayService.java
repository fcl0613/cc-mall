package com.fcl.ccmall.service;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.AliPayDTO;

public interface AliPayService {
    CommonResult aliPay();

    CommonResult payNotify(AliPayDTO aliPayDTO);

    CommonResult returnPay(AliPayDTO aliPayDTO);
}
