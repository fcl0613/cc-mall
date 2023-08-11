package com.fcl.ccmall.entity.dto;

import lombok.Data;

@Data
public class QueryLoginLogDTO {
    private String username;
    private String startTime;
    private String endTime;
    private Long pageNum;
    private Long pageSize;
}
