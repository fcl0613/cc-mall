package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.*;
import com.fcl.ccmall.model.Product;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-03-03
 */
public interface ProductService extends IService<Product> {

    CommonResult create(AddProductDTO addProductDTO);

    CommonResult getProductList(GetProductDTO getProductDTO);

    CommonResult updateProduct(UpdateProductDTO updateProductDTO);

    CommonResult removeProduct(RemoveProductDTO removeProductDTO);

    CommonResult getProductDetailById(Integer id);

    CommonResult changePublishStatus(ProductChangeStatusDTO productChangeStatusDTO);

    CommonResult changeRecommendStatus(ProductChangeStatusDTO productChangeStatusDTO);

    CommonResult changeNewStatus(ProductChangeStatusDTO productChangeStatusDTO);

    CommonResult stockAdd(StockAddDTO stockAddDTO);

    CommonResult batchPublish(BatchOperationDTO batchOperationDTO);

    CommonResult batchUnPublish(BatchOperationDTO batchOperationDTO);

    CommonResult batchRecommend(BatchOperationDTO batchOperationDTO);

    CommonResult batchUnRecommend(BatchOperationDTO batchOperationDTO);

    CommonResult batchNew(BatchOperationDTO batchOperationDTO);

    CommonResult batchUnNew(BatchOperationDTO batchOperationDTO);

    CommonResult findAll(String name);
}
