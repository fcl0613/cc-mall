package com.fcl.ccmall.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PorCustomerDao {
    Integer getMaxId();
}
