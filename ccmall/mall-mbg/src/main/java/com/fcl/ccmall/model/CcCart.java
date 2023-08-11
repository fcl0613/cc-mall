package com.fcl.ccmall.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author fcl
 * @since 2023-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CcCart implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer customerId;

    /**
     * 商品ID
     */
    private Integer productId;

    /**
     * 加入购物车商品数量
     */
    private Integer productAmount;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品展示图
     */
    private String productCover;

    /**
     * 商品价格
     */
    private BigDecimal productPrice;

    private Integer productCategoryId;
}
