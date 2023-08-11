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
 * @since 2023-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductComment implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品编号
     */
    private Integer productId;

    /**
     * 订单编号
     */
    private Integer orderId;

    /**
     * 用户编号
     */
    private Integer customerId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论带的图片逗号分割最多3张
     */
    private String commentPic;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;

    /**
     * 评分
     */
    private Integer score;

    private String username;

    private String avatar;


}
