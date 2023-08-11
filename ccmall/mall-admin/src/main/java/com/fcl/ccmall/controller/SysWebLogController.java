package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.QueryWebLogDTO;
import com.fcl.ccmall.service.SysWebLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/sysweblog")
public class SysWebLogController {

    @Resource
    private SysWebLogService sysWebLogService;

    @PostMapping("/list")
    public CommonResult getList(@RequestBody QueryWebLogDTO queryWebLogDTO) {
        return sysWebLogService.getList(queryWebLogDTO);
    }
}

