package com.fcl.ccmall.entity.dto;

import lombok.Data;

@Data
public class GetProductDTO {
    private String productName;
    private Integer categoryId;
    private Integer publishStatus;
    private Long pageNum;
    private Long pageSize;
}
