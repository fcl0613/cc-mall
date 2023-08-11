package com.fcl.ccmall.service;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.AliPayDTO;

public interface AliPayService {
    CommonResult returnPay(AliPayDTO aliPayDTO);
}
