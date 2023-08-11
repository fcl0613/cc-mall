package com.fcl.ccmall.entity.DO;

import lombok.Data;

@Data
public class GetProductCategoryListDo {
    private Integer id;


    private String name;

    /**
     * 0=1级 1=2级
     */
    private String level;

    /**
     * 是否显示在导航栏 0不显示 1显示
     */
    private Integer navStatus;

    /**
     * 显示状态 0不显示 1显示
     */
    private Integer showStatus;

    /**
     * 排序
     */
    private Integer sort;

}
