package com.fcl.ccmall.model;

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
 * @since 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CcCouponProductRelation implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer couponId;

    private Integer productId;

    /**
     * 商品名称
     */
    private String productName;


}
