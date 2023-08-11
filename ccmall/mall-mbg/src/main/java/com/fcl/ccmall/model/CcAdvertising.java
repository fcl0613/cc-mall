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
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CcAdvertising implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String adName;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    /**
     * 0下线1上线
     */
    private Integer publishStatus;

    /**
     * 广告图片
     */
    private String adPic;

    /**
     * 广告连接
     */
    private String adLink;

    /**
     * 广告备注
     */
    private String adNote;


}
