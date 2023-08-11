package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetJobListDO {
    private Integer id;

    private String jobName;

    private Integer jobStatus;

    private LocalDateTime createTime;

    private LocalDateTime finishTime;

    private String downloadUrl;

    private String failedReason;
}
