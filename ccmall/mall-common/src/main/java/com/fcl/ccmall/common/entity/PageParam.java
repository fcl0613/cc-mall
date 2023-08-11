package com.fcl.ccmall.common.entity;

import lombok.Data;

/**
 *分页参数实体封装
 */

@Data
public class PageParam {
    private Long pageNum;
    private Long pageSize;
}
