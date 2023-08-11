package com.fcl.ccmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author fcl
 * @since 2023-02-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 上级分类编号0表示一级
     */
    private Integer parentId;

    private String name;

    /**
     * 0=1级 1=2级
     */
    private Integer level;

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

    /**
     * 图标
     */
    private String icon;

    /**
     * 描述
     */
    private String description;


}
