package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.ProductCommentCreateDTO;
import com.fcl.ccmall.mapper.CustomerMapper;
import com.fcl.ccmall.mapper.ProductCommentMapper;
import com.fcl.ccmall.mapper.ProductToBeCommentMapper;
import com.fcl.ccmall.model.Customer;
import com.fcl.ccmall.model.ProductComment;
import com.fcl.ccmall.model.ProductToBeComment;
import com.fcl.ccmall.service.ProductCommentService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-04-01
 */
@Service
public class ProductCommentServiceImpl extends ServiceImpl<ProductCommentMapper, ProductComment> implements ProductCommentService {

    @Resource
    private ProductToBeCommentMapper productToBeCommentMapper;

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public CommonResult create(ProductCommentCreateDTO productCommentCreateDTO) {
        ProductToBeComment productToBeComment = productToBeCommentMapper.selectById(productCommentCreateDTO.getId());
        Customer customer = customerMapper.selectById(productToBeComment.getCustomerId());
        ProductComment productComment = new ProductComment();
        productComment.setOrderId(productToBeComment.getOrderId());
        productComment.setContent(productCommentCreateDTO.getContent());
        productComment.setCreateTime(LocalDateTime.now());
        productComment.setCustomerId(productToBeComment.getCustomerId());
        productComment.setProductId(productToBeComment.getProductId());
        productComment.setScore(productCommentCreateDTO.getScore());
        productComment.setUsername(customer.getUsername());
        productComment.setAvatar(customer.getAvatar());
        this.save(productComment);
        // 删除待评论商品列表
        productToBeCommentMapper.deleteById(productCommentCreateDTO.getId());
        return CommonResult.success();
    }
}
