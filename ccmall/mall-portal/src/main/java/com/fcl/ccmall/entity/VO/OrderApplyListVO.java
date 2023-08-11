package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.model.OrderDetail;
import lombok.Data;

import java.util.List;

@Data
public class OrderApplyListVO {
    private Integer id;
    private String orderSn;
    private Integer status;
    private String statusStr;
    private String handleNode;
    private List<OrderDetail> orderDetails;
}
