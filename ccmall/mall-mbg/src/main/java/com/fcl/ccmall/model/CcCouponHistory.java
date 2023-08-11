package com.fcl.ccmall.model;

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
 * @since 2023-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CcCouponHistory implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 优惠券编号
     */
    private Integer couponId;

    /**
     * 用户编号
     */
    private Integer memberId;

    /**
     * 领取人用户名
     */
    private String username;

    /**
     * 获取类型0后台赠送1主动获取
     */
    private Integer getType;

    /**
     * 使用状态0未使用1已使用2已过期
     */
    private Integer useStatus;

    /**
     * 使用时间
     */
    private LocalDateTime useTime;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 订单号码
     */
//    private String orderSn;

    private LocalDateTime getTime;
}
