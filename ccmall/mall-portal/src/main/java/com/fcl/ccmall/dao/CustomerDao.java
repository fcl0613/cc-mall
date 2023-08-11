package com.fcl.ccmall.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerDao {
    void updatePoints(@Param("points") Integer points,
                      @Param("customerId") Integer customer);
}
