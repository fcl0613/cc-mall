package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.entity.DO.GetCustomerListDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface CustomerDao {
    Page<GetCustomerListDO> getCustomerDO(@Param("page") Page<GetCustomerListDO> page,
                                          @Param("keyword") String keyword,
                                          @Param("map") Map<Integer,String> map);
}
