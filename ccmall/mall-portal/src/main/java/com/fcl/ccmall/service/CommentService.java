package com.fcl.ccmall.service;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.CommentListDTO;

public interface CommentService {
    CommonResult getCommentList(CommentListDTO commentListDTO);
}
