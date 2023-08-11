package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.entity.DO.OrderReturnApplyDO;
import com.fcl.ccmall.entity.dto.OrderReturnApplyListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderReturnApplyDao {
    Page<OrderReturnApplyDO> getApplyList(@Param("page") Page<OrderReturnApplyDO> page,
                                          @Param("dto") OrderReturnApplyListDTO dto);
}
