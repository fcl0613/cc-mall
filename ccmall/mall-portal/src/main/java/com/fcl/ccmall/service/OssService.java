package com.fcl.ccmall.service;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.VO.OssCallbackResult;
import com.fcl.ccmall.entity.VO.OssPolicyResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface OssService {
    OssPolicyResult getPolicy();
    OssCallbackResult callback(HttpServletRequest request);
    CommonResult fileUpload(MultipartFile multipartFile);
}
