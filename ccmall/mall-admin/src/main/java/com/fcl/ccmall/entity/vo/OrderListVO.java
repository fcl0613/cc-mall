package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.OrderListDO;
import lombok.Data;

import java.util.List;

@Data
public class OrderListVO {
    private List<OrderListDO> list;
    private Long total;
}
