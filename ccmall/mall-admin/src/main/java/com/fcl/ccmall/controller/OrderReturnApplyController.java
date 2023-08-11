package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.OrderReturnApplyListDTO;
import com.fcl.ccmall.entity.dto.RefuseApplyDTO;
import com.fcl.ccmall.service.OrderReturnApplyService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order/returnApply")
public class OrderReturnApplyController {

    @Resource
    private OrderReturnApplyService orderReturnApplyService;

    @PostMapping("/list")
    public CommonResult getList(@RequestBody OrderReturnApplyListDTO dto) {
        return orderReturnApplyService.getApplyList(dto);
    }

    @PostMapping("/agree/{id}")
    public CommonResult agreeApply(@PathVariable Integer id) {
        return orderReturnApplyService.agreeApply(id);
    }

    @PostMapping("/refuse")
    public CommonResult refuseApply(@RequestBody RefuseApplyDTO refuseApplyDTO) {
        return orderReturnApplyService.refuseApply(refuseApplyDTO);
    }

    @PostMapping("/finish/{id}")
    public CommonResult finishApply(@PathVariable Integer id) {
        return orderReturnApplyService.finishApply(id);
    }
}
