package com.fcl.ccmall.entity.DTO;

import lombok.Data;

@Data
public class RegisterDTO {
    private String phone;
    private String password;
    private String captcha;
}
