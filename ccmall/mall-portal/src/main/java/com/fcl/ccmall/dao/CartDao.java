package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.entity.DO.CartListDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartDao {
    void plusCount(@Param("id") Integer id);
    void minusCount(@Param("id") Integer id);
    Page<CartListDO> getCartList(@Param("page") Page<CartListDO> page,
                                 @Param("customerId") Integer customerId);
}
