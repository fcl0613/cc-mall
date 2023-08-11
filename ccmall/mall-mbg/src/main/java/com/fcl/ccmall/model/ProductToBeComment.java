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
 * @since 2023-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductToBeComment implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户编号
     */
    private Integer customerId;

    /**
     * 商品编号
     */
    private Integer productId;

    private String productName;

    private String productCover;

    private Integer productCount;

    private BigDecimal productPrice;

    private LocalDateTime createTime;

    private Integer orderId;
}
