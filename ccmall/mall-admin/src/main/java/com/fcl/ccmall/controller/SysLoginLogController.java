package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.QueryLoginLogDTO;
import com.fcl.ccmall.service.SysLoginLogService;
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
@RequestMapping("/sysloginlog")
public class SysLoginLogController {

    @Resource
    private SysLoginLogService sysLoginLogService;

    @PostMapping("/list")
    public CommonResult getList(@RequestBody QueryLoginLogDTO queryLoginLogDTO) {
        return sysLoginLogService.getList(queryLoginLogDTO);
    }
}

