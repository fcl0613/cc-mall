package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.VO.ProductBeCommentVO;
import com.fcl.ccmall.mapper.ProductToBeCommentMapper;
import com.fcl.ccmall.model.ProductToBeComment;
import com.fcl.ccmall.service.ProductToBeCommentService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-03-31
 */
@Service
public class ProductToBeCommentServiceImpl extends ServiceImpl<ProductToBeCommentMapper, ProductToBeComment> implements ProductToBeCommentService {

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private HttpServletRequest request;

    @Override
    public CommonResult getProductList(Long pageNum, Long pageSize) {
        Page<ProductToBeComment> productToBeCommentPage = new Page<>(pageNum, pageSize);
        Page<ProductToBeComment> page = this.page(productToBeCommentPage, new LambdaQueryWrapper<ProductToBeComment>()
                .eq(ProductToBeComment::getCustomerId, getCustomerId()));
        ProductBeCommentVO productBeCommentVO = new ProductBeCommentVO();
        productBeCommentVO.setList(page.getRecords());
        productBeCommentVO.setTotal(page.getTotal());
        return CommonResult.success(productBeCommentVO);
    }

    private Integer getCustomerId() {
        return jwtTokenUtils.getUserId(request.getHeader("token"));
    }
}
