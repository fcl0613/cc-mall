package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.CommentListDTO;
import com.fcl.ccmall.entity.vo.CommentListVO;
import com.fcl.ccmall.mapper.ProductCommentMapper;
import com.fcl.ccmall.model.CcPostComment;
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
        Page<ProductComment> commentPage = new Page<>(commentListDTO.getPageNum(), commentListDTO.getPageSize());
        Page<ProductComment> page = productCommentMapper.selectPage(commentPage, new LambdaQueryWrapper<ProductComment>()
        .orderByDesc(ProductComment::getId));
        CommentListVO commentListVO = new CommentListVO();
        commentListVO.setList(page.getRecords());
        commentListVO.setTotal(page.getTotal());
        return CommonResult.success(commentListVO);
    }

    @Override
    public CommonResult deleteComment(Integer id) {
        productCommentMapper.deleteById(id);
        return CommonResult.success();
    }
}
