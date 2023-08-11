package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.GetCouponHistoryListDO;
import lombok.Data;

import java.util.List;

@Data
public class GetCouponHistoryListVO {
    private List<GetCouponHistoryListDO> list;
    private Long total;
}
