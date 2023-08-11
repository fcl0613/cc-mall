package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.entity.DO.GetProductCategoryListDo;
import com.fcl.ccmall.entity.DO.ParentCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductCategoryDao {
    Page<GetProductCategoryListDo> getProductCategoryList(@Param("page") Page<GetProductCategoryListDo> page);
    List<ParentCategoryDO> getAllParentCategoryList();
}
