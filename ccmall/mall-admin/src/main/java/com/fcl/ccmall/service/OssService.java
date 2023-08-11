package com.fcl.ccmall.service;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.vo.OssCallbackResult;
import com.fcl.ccmall.entity.vo.OssPolicyResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;

public interface OssService {
    OssPolicyResult getPolicy();
    OssCallbackResult callback(HttpServletRequest request);
    CommonResult fileUpload(MultipartFile multipartFile);
    Boolean excelUpload(File file, String fileName);
}
