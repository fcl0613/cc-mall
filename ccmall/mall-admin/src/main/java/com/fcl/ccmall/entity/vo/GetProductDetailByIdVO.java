package com.fcl.ccmall.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetProductDetailByIdVO {
    private Integer id;

    private String productName;

    private BigDecimal price;

    /**
     * 封面图，最多5张 以逗号分割
     */
    private List<String> productCover;

    /**
     * 详情图，最多4张 以逗号分割
     */
    private List<String> productPics;

    private Integer categoryId;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 上架状态0上架1下架
     */
    private Integer publishStatus;

    /**
     * 促销价格
     */
    private BigDecimal promotionPrice;

    /**
     * 促销开始时间
     */
    private LocalDateTime promotionStartTime;

    /**
     * 促销结束时间
     */
    private LocalDateTime promotionEndTime;

    /**
     * 活动限购数量
     */
    private Integer promotionPerLimit;

    /**
     * 描述
     */
    private String description;

    /**
     * 推荐状态；0->不推荐；1->推荐
     */
    private Integer recommendStatus;

    /**
     * 新品状态:0->不是新品；1->新品
     */
    private Integer newStatus;

    /**
     * 赠送的积分
     */
    private Integer giftPoint;

    private Integer categoryIdParent;
}
