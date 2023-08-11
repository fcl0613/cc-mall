package com.fcl.ccmall.dao;

import com.fcl.ccmall.entity.DO.OrderCountDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface HomeDao {
    OrderCountDO getOrderCount(@Param("time") String time);

    Integer todayOrderCount(@Param("time") String time);

    BigDecimal getIncome(@Param("time") String time);
}
