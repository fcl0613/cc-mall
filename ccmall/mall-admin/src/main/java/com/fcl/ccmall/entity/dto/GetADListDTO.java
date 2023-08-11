package com.fcl.ccmall.entity.dto;

import lombok.Data;

@Data
public class GetADListDTO {
    private String ADName;
    private String endTime;
    private Integer publishStatus;
    private Long pageNum;
    private Long pageSize;
}
