package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.GetJobListDTO;
import com.fcl.ccmall.service.ExcelOutputJobService;
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
 * @since 2023-05-12
 */
@RestController
@RequestMapping("/excel/output/job")
public class ExcelOutputJobController {

    @Resource
    private ExcelOutputJobService excelOutputJobService;

    @PostMapping("/create")
    public CommonResult createJob(@RequestParam("jobName") String jobName) {
        return excelOutputJobService.createJob(jobName);
    }

    @PostMapping("/list")
    public CommonResult getJobList(@RequestBody GetJobListDTO dto) {
        return excelOutputJobService.getJobList(dto);
    }
}

