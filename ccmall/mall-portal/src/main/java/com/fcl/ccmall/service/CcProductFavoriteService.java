package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.model.CcProductFavorite;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-03-24
 */
public interface CcProductFavoriteService extends IService<CcProductFavorite> {

    CommonResult create(Integer productId);

    CommonResult cancel(Integer productId);

    CommonResult getFavoriteList(Long pageNum, Long pageSize);

    CommonResult delete(Integer id);
}
