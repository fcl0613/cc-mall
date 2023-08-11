package com.fcl.ccmall.service;

import com.fcl.ccmall.common.api.CommonResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExcelService {
    CommonResult upload(MultipartFile file);
    void downLoad(HttpServletResponse response) throws IOException;
    void generateExcel(Integer jobId);

    void templateDownLoad(HttpServletResponse response) throws IOException;
}
