package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.GetJobListDTO;
import com.fcl.ccmall.model.ExcelOutputJob;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-05-12
 */
public interface ExcelOutputJobService extends IService<ExcelOutputJob> {

    CommonResult createJob(String jobName);

    CommonResult getJobList(GetJobListDTO dto);
}
