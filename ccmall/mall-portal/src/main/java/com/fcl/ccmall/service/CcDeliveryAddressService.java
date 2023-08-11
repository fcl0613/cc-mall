package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.model.CcDeliveryAddress;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-03-27
 */
public interface CcDeliveryAddressService extends IService<CcDeliveryAddress> {

    CommonResult create(CcDeliveryAddress deliveryAddress);

    CommonResult getAddressList();

    CommonResult deleteAddress(Integer id);

    CommonResult setDefault(Integer id);

    CommonResult updateAddress(CcDeliveryAddress deliveryAddress);

    CommonResult getAddressInfo(Integer id);
}
