package com.fcl.ccmall.entity.dto;

import lombok.Data;

@Data
public class UpdateCustomerDTO {
    private Integer id;
    private String phone;
    private Integer gender;
    private String nickName;
}
