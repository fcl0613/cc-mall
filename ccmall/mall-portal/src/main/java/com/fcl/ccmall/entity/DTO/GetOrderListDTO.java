package com.fcl.ccmall.entity.DTO;

import com.fcl.ccmall.common.entity.PageParam;
import lombok.Data;

@Data
public class GetOrderListDTO extends PageParam {
    // 订单状态
    private Integer orderStatus;
}
