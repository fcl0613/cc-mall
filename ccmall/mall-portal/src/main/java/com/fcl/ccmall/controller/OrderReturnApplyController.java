package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.AfterSaleApplyDTO;
import com.fcl.ccmall.service.OrderReturnApplyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-03-31
 */
@RestController
@RequestMapping("/order/return/apply")
public class OrderReturnApplyController {

    @Resource
    private OrderReturnApplyService orderReturnApplyService;

    @PostMapping("/create")
    public CommonResult afterSaleApply(@RequestBody AfterSaleApplyDTO afterSaleApplyDTO) {
        return orderReturnApplyService.create(afterSaleApplyDTO);
    }

    /**
     * 退货申请列表
     */
    @GetMapping("/list")
    public CommonResult getApplyList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Long pageNum,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "20") Long pageSize) {
        return orderReturnApplyService.getApplyList(pageNum, pageSize);
    }

    @PostMapping("/remove/{id}")
    public CommonResult removeApply(@PathVariable Integer id) {
        return orderReturnApplyService.removeApply(id);
    }
}

