package com.fcl.ccmall.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
public class CcProductFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品编号
     */
    private Integer productId;

    /**
     * 收藏用户编号
     */
    private Integer customerId;

    /**
     * 商品名字
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

    /**
     * 收藏时间
     */
    private LocalDateTime createTime;


}
