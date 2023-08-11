package com.fcl.ccmall.entity.VO;

import lombok.Data;

import java.util.List;

@Data
public class OrderApplyVO {
    private List<OrderApplyListVO> list;
    private Long total;
}
