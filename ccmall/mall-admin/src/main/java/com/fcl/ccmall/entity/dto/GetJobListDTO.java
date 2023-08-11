package com.fcl.ccmall.entity.dto;

import com.fcl.ccmall.common.entity.PageParam;
import lombok.Data;

@Data
public class GetJobListDTO extends PageParam {
    private String jobName;
    private Integer jobStatus;
}
