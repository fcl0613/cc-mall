package com.fcl.ccmall.entity.vo;

import lombok.Data;

@Data
public class OssCallbackParam {
    /**
     * 请求的回调地址
     */
    private String callbackUrl;
    /**
     * 回调时传入了request参数
     */
    private String callbackBody;
    /**
     * 回调时传入的参数格式
     */
    private String callbackBodyType;
}
