package com.fcl.ccmall.entity.DO;

import lombok.Data;

@Data
public class GetCustomerListDO {
    private Integer id;
    private String username;
    private String gender;
    private String nickName;
    private String phone;
}
