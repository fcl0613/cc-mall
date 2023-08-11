package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.service.DataAnalysis;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/dataAnalysis")
@RestController
public class DataAnalysisController {

    @Resource
    private DataAnalysis dataAnalysis;

    @GetMapping("/persona")
    public CommonResult getPersonaAnalysis() {
        return dataAnalysis.getPersonaAnalysis();
    }
}
