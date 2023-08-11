package com.fcl.ccmall.service;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.OrderReturnApplyListDTO;
import com.fcl.ccmall.entity.dto.RefuseApplyDTO;

public interface OrderReturnApplyService {
    CommonResult getApplyList(OrderReturnApplyListDTO dto);

    CommonResult agreeApply(Integer id);

    CommonResult refuseApply(RefuseApplyDTO refuseApplyDTO);

    CommonResult finishApply(Integer id);
}
