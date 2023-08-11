package com.fcl.ccmall.entity.dto;

import com.fcl.ccmall.common.entity.PageParam;
import lombok.Data;

@Data
public class OrderReturnApplyListDTO extends PageParam {
    private String orderId;
    private Integer status;
    private String startTime;
    private String endTime;
}
