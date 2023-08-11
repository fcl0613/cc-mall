package com.fcl.ccmall.entity.VO;

import lombok.Data;

@Data
public class VantAddressVO {
    private Integer id;
    private String name;
    private String tel;
    private String address;
    private Boolean isDefault;
}
