package com.fcl.ccmall.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.model.ProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fcl
 * @since 2023-02-12
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
    Page<ProductCategory> getProductCategoryList(@Param("page") Page<ProductCategory> page);
}
