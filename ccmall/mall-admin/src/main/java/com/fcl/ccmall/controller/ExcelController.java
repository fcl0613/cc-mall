package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.service.ExcelService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping("/excel")
@RestController
public class ExcelController {

    @Resource
    @Lazy
    private ExcelService excelService;

    @PostMapping("/upload")
    public CommonResult upload(MultipartFile file) {
       return excelService.upload(file);
    }

    @GetMapping("/download")
    public void downLoad(HttpServletResponse response) throws IOException {
        excelService.downLoad(response);
    }

    @GetMapping("/template/download")
    public void templateDownLoad(HttpServletResponse response) throws IOException {
        excelService.templateDownLoad(response);
    }
}
