package com.fcl.ccmall.entity.DTO;

import lombok.Data;

@Data
public class LoginDTO {
    /**
     * 登录账号 可以是用户名也可以是手机号
     */
    private String loginAccount;
    private String password;
}
