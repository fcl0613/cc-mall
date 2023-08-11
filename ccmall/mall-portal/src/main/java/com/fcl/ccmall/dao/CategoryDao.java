package com.fcl.ccmall.dao;

import com.fcl.ccmall.entity.DO.CategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryDao {
    List<CategoryDO> getAll();
}
