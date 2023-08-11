package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.PostCommentCreateDTO;
import com.fcl.ccmall.entity.DTO.PostCommentListDTO;
import com.fcl.ccmall.model.CcPostComment;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-04-07
 */
public interface CcPostCommentService extends IService<CcPostComment> {

    CommonResult getCommentList(PostCommentListDTO postCommentListDTO);

    CommonResult createComment(PostCommentCreateDTO dto);
}
