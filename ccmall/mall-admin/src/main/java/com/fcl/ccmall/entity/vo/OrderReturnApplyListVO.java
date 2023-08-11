package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.OrderReturnApplyDO;
import lombok.Data;

import java.util.List;

@Data
public class OrderReturnApplyListVO {
    private List<OrderReturnApplyDO> list;
    private Long total;
}
