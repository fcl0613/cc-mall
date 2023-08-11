package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.entity.DO.AllCouponDO;
import lombok.Data;

import java.util.List;

@Data
public class GetAllCouponVO {
    // 未领取
    private List<AllCouponDO> unReceivedList;
    // 未使用
    private List<AllCouponDO> unUsedList;
    // 已过期
    private List<AllCouponDO> exportedList;
}
