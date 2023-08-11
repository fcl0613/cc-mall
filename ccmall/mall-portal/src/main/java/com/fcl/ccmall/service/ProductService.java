package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.GetProductDTO;
import com.fcl.ccmall.model.Product;

public interface ProductService extends IService<Product> {
    CommonResult getProductList(GetProductDTO getProductDTO);

    CommonResult getProductDetail(Integer id);
}
