package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DO.GetJobListDO;
import com.fcl.ccmall.entity.dto.GetJobListDTO;
import com.fcl.ccmall.entity.vo.GetJobListVO;
import com.fcl.ccmall.enums.JobStatusEnum;
import com.fcl.ccmall.mapper.ExcelOutputJobMapper;
import com.fcl.ccmall.model.ExcelOutputJob;
import com.fcl.ccmall.service.ExcelOutputJobService;
import com.fcl.ccmall.service.ExcelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-05-12
 */
@Service
public class ExcelOutputJobServiceImpl extends ServiceImpl<ExcelOutputJobMapper, ExcelOutputJob> implements ExcelOutputJobService {

    @Resource
    private ExcelService excelService;

    @Override
    public CommonResult createJob(String jobName) {
        ExcelOutputJob excelOutputJob = new ExcelOutputJob();
        excelOutputJob.setJobName(jobName);
        excelOutputJob.setCreateTime(LocalDateTime.now());
        excelOutputJob.setJobStatus(JobStatusEnum.NOT_STARTED.getCode());
        this.save(excelOutputJob);
        excelService.generateExcel(excelOutputJob.getId());
        return CommonResult.success();
    }

    @Override
    public CommonResult getJobList(GetJobListDTO dto) {
        Page<ExcelOutputJob> jobPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<ExcelOutputJob> queryWrapper = new LambdaQueryWrapper<>();
        if (!Objects.isNull(dto.getJobName())) {
            queryWrapper.like(ExcelOutputJob::getJobName, dto.getJobName());
        }
        if (!Objects.isNull(dto.getJobStatus())) {
            queryWrapper.eq(ExcelOutputJob::getJobStatus, dto.getJobStatus());
        }
        Page<ExcelOutputJob> page = this.page(jobPage, queryWrapper);
        List<ExcelOutputJob> records = page.getRecords();
        List<GetJobListDO> jobListDOS = new ArrayList<>();
        for (ExcelOutputJob record : records) {
            GetJobListDO jobListDO = new GetJobListDO();
            jobListDO.setId(record.getId());
            jobListDO.setCreateTime(record.getCreateTime());
            jobListDO.setDownloadUrl(record.getDownloadUrl());
            jobListDO.setJobStatus(record.getJobStatus());
            jobListDO.setFailedReason(record.getFailedReason());
            jobListDO.setFinishTime(record.getFinishTime());
            jobListDO.setJobName(record.getJobName());
            jobListDOS.add(jobListDO);
        }
        GetJobListVO getJobListVO = new GetJobListVO();
        getJobListVO.setList(jobListDOS);
        getJobListVO.setTotal(page.getTotal());
        return CommonResult.success(getJobListVO);
    }
}
