package com.fcl.ccmall.service;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.CommentListDTO;

public interface CommentService {
    CommonResult getCommentList(CommentListDTO commentListDTO);

    CommonResult deleteComment(Integer id);
}
