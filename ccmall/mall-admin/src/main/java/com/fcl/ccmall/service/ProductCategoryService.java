package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.model.ProductCategory;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-02-12
 */
public interface ProductCategoryService extends IService<ProductCategory> {
    CommonResult getProductCategoryList(PageParam pageParam);
    CommonResult create(ProductCategory productCategory);
    CommonResult updateProductCategory(ProductCategory productCategory);
    CommonResult removeProductCategoryById(Integer id);
    CommonResult getProductCategoryDetail(Integer id);
    CommonResult getParentList();

    CommonResult updateNavStatus(Integer categoryId, Integer status);

    CommonResult updateShowStatus(Integer categoryId, Integer status);

    CommonResult getAllCategory();
}
