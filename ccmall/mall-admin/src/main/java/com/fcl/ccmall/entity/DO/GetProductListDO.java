package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetProductListDO {
    private Integer id;
    private String productName;
    private BigDecimal price;
    private String productCover;
    /**
     * 推荐状态；0->不推荐；1->推荐
     */
    private Integer recommendStatus;

    /**
     * 新品状态:0->不是新品；1->新品
     */
    private Integer newStatus;
    /**
     * 上架状态0上架1下架
     */
    private Integer publishStatus;

    private Integer stock;
}
