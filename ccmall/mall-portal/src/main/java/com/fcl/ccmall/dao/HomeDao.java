package com.fcl.ccmall.dao;

import com.fcl.ccmall.entity.DO.AdvertisingDO;
import com.fcl.ccmall.entity.DO.CommonProductDO;
import com.fcl.ccmall.entity.DO.HomeCateGoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HomeDao {
    List<AdvertisingDO> getHomeAdvertiseList();
    List<HomeCateGoryDO> getHomeCategory();
    List<CommonProductDO> getHomeRecommendProduct();
    List<CommonProductDO> getHomeNewProduct();
    List<CommonProductDO> getHomeProductList(@Param("categoryId") Integer category);
}
