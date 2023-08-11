package com.fcl.ccmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author fcl
 * @since 2023-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CcPost implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer customerId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 点击量
     */
    private Integer pageview;

    /**
     * 点赞
     */
    private Integer likeCount;

    /**
     * 评论量
     */
    private Integer commentCount;

    /**
     * 图片逗号分割
     */
    private String pic;

    private LocalDateTime createTime;
}
