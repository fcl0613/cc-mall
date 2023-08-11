package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.entity.DO.FindAllProductDO;
import com.fcl.ccmall.entity.DO.GetProductListDO;
import com.fcl.ccmall.entity.dto.GetProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductDao {
    Page<GetProductListDO> getProductList(@Param("page") Page<GetProductListDO> page,
                                          @Param("dto")GetProductDTO getProductDTO);
    void batchProductPublishStatus(@Param("list") List<Integer> list, @Param("status") Integer status);
    void batchProductRecommendStatus(@Param("list") List<Integer> list, @Param("status") Integer status);
    void batchProductNewStatus(@Param("list") List<Integer> list, @Param("status") Integer status);
    List<FindAllProductDO> findAll(@Param("name") String name);
}
