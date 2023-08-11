package com.fcl.ccmall.entity.dto;

import lombok.Data;

@Data
public class AddCustomerDTO {
    private String username;
    private String phone;
    private String nickName;
    private Integer gender;
}
