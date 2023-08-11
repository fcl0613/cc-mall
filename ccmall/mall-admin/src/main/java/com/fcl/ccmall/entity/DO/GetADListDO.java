package com.fcl.ccmall.entity.DO;

import lombok.Data;

@Data
public class GetADListDO {
    private Integer id;
    private String adName;
    private String beginTime;
    private String endTime;
    private Integer publishStatus;
    private String adPic;
}
