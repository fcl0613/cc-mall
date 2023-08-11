package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.ProductCommentCreateDTO;
import com.fcl.ccmall.model.ProductComment;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-04-01
 */
public interface ProductCommentService extends IService<ProductComment> {

    CommonResult create(ProductCommentCreateDTO productCommentCreateDTO);
}
