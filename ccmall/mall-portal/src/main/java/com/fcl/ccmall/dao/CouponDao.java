package com.fcl.ccmall.dao;

import com.fcl.ccmall.model.CcCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CouponDao {
    List<CcCoupon> getCouponList(@Param("memberId") Integer memberId,
                                 @Param("useStatus") Integer useStatus);

    List<CcCoupon> getCustomerCoupon(@Param("time") String time,
                                     @Param("status") Integer status,
                                     @Param("memberId") Integer memberId);

    void addReceivedCoupon(@Param("id") Integer id);

    void addUseCoupon(@Param("id") Integer id);
}
