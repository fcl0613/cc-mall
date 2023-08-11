package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.entity.DO.OrderListDO;
import lombok.Data;

import java.util.List;

@Data
public class OrderListVO {
    private List<OrderListDO> orderList;
    private Long total;
}
