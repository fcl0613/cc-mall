package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.model.ProductToBeComment;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-03-31
 */
public interface ProductToBeCommentService extends IService<ProductToBeComment> {

    CommonResult getProductList(Long pageNum, Long pageSize);
}
