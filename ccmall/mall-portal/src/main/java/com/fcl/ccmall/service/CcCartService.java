package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.CartItemDTO;
import com.fcl.ccmall.model.CcCart;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-03-24
 */
public interface CcCartService extends IService<CcCart> {

    CommonResult addCart(CartItemDTO cartItemDTO);

    CommonResult delete(Integer id);

    CommonResult plusCount(Integer id);

    CommonResult minusCount(Integer id);

    CommonResult getCartList(Long pageNum, Long pageSize);
}
