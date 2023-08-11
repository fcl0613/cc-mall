package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.LoginLogDo;
import lombok.Data;

import java.util.List;

@Data
public class LoginLogVo {
    private Long size;
    private List<LoginLogDo> list;
}
