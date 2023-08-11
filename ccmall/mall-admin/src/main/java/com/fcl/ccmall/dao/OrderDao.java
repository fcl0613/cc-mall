package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.entity.DO.OrderListDO;
import com.fcl.ccmall.entity.dto.GetOrderListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao {
    Page<OrderListDO> getOrderList(@Param("page") Page<OrderListDO> page,
                                   @Param("dto") GetOrderListDTO dto);
}
