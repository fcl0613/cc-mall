package com.fcl.ccmall.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class CascaderVO {
    private Integer value;
    private String label;
    private List<CascaderItemVO> children;
}
