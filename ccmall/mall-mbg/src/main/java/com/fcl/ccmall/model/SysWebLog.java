package com.fcl.ccmall.model;

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
 * @since 2023-02-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysWebLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String description;

    private String username;

    /**
     * 操作时间
     */
    private LocalDateTime startTime;

    /**
     * 消耗时间
     */
    private Integer spendTime;

    private String basePath;

    private String uri;

    private String url;

    private String method;

    private String ip;

    /**
     * 请求参数
     */
    private String parameter;

    /**
     * 返回结果
     */
    private String result;


}
