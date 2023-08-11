package com.fcl.ccmall.entity.VO;

import lombok.Data;

@Data
public class VantAddressInfoVO {
    private Integer id;
    private String name;
    private String tel;
    private String province;
    private String city;
    private String county;
    private String addressDetail;
    private String areaCode;
    private Boolean isDefault;
}
