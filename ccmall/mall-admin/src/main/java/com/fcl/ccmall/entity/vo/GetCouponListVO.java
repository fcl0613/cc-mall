package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.GetCouponListDO;
import lombok.Data;

import java.util.List;

@Data
public class GetCouponListVO {
    private List<GetCouponListDO> list;
    private Long total;
}
