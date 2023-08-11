package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.entity.DO.ProductListDO;
import com.fcl.ccmall.entity.DTO.GetProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PorProductDao {
    Page<ProductListDO> getProductList(@Param("page") Page<ProductListDO> page,
                                       @Param("dto") GetProductDTO getProductDTO);
}
