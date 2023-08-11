package com.fcl.ccmall.entity.VO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDetailVO {
    private String productName;
    private BigDecimal price;
    private String subtitle;
    private List<String> productCover;
    private List<String> productPics;
    /**
     * 是否收藏标志
     */
    private Boolean favoriteFlag;
    /**
     * 商品评论数量
     */
    private Integer commentCount;
    /**
     * 好评数量
     */
    private Integer positiveCount;
    /**
     *  中评数量
     */
    private Integer neutralCount;

    /**
     * 差评数量
     */
    private Integer negativeCount;

    private Integer stock;

    private String description;

    // 有一个广告后面再说
}
