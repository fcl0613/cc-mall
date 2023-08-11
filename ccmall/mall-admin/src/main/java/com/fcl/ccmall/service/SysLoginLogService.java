package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.QueryLoginLogDTO;
import com.fcl.ccmall.model.SysLoginLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-02-06
 */
public interface SysLoginLogService extends IService<SysLoginLog> {
    CommonResult getList(QueryLoginLogDTO queryLoginLogDTO);
}
