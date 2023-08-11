package com.fcl.ccmall.entity.VO;

import lombok.Data;

import java.util.List;

@Data
public class CategoryVO {
    private Integer id;
    private String name;
    private String icon;
    private List<CategoryVO> list;
}
