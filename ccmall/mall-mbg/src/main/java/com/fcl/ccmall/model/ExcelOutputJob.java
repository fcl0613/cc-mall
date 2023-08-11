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
 * @since 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExcelOutputJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String jobName;

    private Integer jobStatus;

    private LocalDateTime createTime;

    private LocalDateTime finishTime;

    private String downloadUrl;

    private String failedReason;
}
