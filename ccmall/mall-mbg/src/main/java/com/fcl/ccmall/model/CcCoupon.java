package com.fcl.ccmall.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class CcCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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

    /**
     * 使用数量
     */
    private Integer useCount;

    /**
     * 领取数量
     */
    private Integer receiveCount;

    @TableField(exist = false)
    private Boolean hasUsed;

    @TableField(exist = false)
    private String noUsedReason;
}
