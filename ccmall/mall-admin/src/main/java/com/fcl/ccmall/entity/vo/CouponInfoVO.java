package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.model.CcCouponProductCategoryRelation;
import com.fcl.ccmall.model.CcCouponProductRelation;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponInfoVO {
    private Integer id;

    /**
     * 优惠券类型0全场券1会员赠券2购物赠券3注册赠券
     */
    private Integer type;

    /**
     * 优惠券名
     */
    private String name;

    /**
     * 数量
     */
    private Integer couponCount;

    /**
     * 优惠金额
     */
    private BigDecimal amount;

    /**
     * 每人限领次数
     */
    private Integer perLimit;

    /**
     * 使用门槛(满减)
     */
    private BigDecimal minPoint;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 使用类型0全场通用1指定分类2指定商品
     */
    private Integer useType;

    /**
     * 备注
     */
    private String note;

    private List<CcCouponProductCategoryRelation> productCategoryRelationList;

    private List<CcCouponProductRelation> productRelationList;


}
