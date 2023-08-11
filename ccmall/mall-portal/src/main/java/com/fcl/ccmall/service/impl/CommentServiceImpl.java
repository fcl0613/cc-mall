package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.CommentListDTO;
import com.fcl.ccmall.entity.VO.CommentListVO;
import com.fcl.ccmall.mapper.ProductCommentMapper;
import com.fcl.ccmall.model.ProductComment;
import com.fcl.ccmall.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private ProductCommentMapper productCommentMapper;

    @Override
    public CommonResult getCommentList(CommentListDTO commentListDTO) {
        Page<ProductComment> productCommentPage = new Page<>(commentListDTO.getPageNum(), commentListDTO.getPageSize());
        Page<ProductComment> page = productCommentMapper.selectPage(productCommentPage,
                new LambdaQueryWrapper<ProductComment>()
        .eq(ProductComment::getProductId, commentListDTO.getProductId()));
        CommentListVO commentListVO = new CommentListVO();
        commentListVO.setList(page.getRecords());
        commentListVO.setTotal(page.getTotal());
        return CommonResult.success(commentListVO);
    }
}
