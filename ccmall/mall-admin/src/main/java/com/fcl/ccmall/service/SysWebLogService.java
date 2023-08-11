package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.QueryWebLogDTO;
import com.fcl.ccmall.model.SysWebLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-02-06
 */
public interface SysWebLogService extends IService<SysWebLog> {
    void asyncInsert(SysWebLog sysWebLog);
    CommonResult getList(QueryWebLogDTO queryWebLogDTO);
}
