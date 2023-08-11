package com.fcl.ccmall.entity.dto;

import com.fcl.ccmall.model.CcCouponProductCategoryRelation;
import com.fcl.ccmall.model.CcCouponProductRelation;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateCouponDTO {
    private Integer type;
    private String name;
    private Integer couponCount;
    private BigDecimal amount;
    private Integer perLimit;
    private BigDecimal minPoint;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer useType;
    private String note;
    private List<CcCouponProductCategoryRelation> categoryRelationList;
    private List<CcCouponProductRelation> productRelationList;
}
