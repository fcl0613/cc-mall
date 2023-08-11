package com.fcl.ccmall.entity.dto;

import com.fcl.ccmall.common.entity.PageParam;
import lombok.Data;

@Data
public class GetOrderListDTO extends PageParam {
    private String orderId;
    private Integer orderStatus;
    private String startTime;
    private String endTime;
}
