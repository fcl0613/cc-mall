package com.fcl.ccmall.entity.DO;

import lombok.Data;

@Data
public class CategoryDO {
    private Integer id;
    private Integer parentId;
    private String name;
    private Integer level;
    private String icon;
}
