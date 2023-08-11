package com.fcl.ccmall.entity.DO;

import lombok.Data;

@Data
public class WebLogDo {
    private String description;
    private String username;
    private String startTime;
    private String spendTime;
    private String url;
    private String method;
    private String ip;
}
