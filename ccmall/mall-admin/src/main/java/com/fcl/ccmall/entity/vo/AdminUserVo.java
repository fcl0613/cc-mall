package com.fcl.ccmall.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class AdminUserVo {
    private String name;
    private String avatar;
    /**
     * 暂时不需要该字段
     */
    private String roles;
    private List<String> buttons;
    private List<RouterVo> routers;
}
