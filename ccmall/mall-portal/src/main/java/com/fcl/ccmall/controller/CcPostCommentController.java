package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.PostCommentCreateDTO;
import com.fcl.ccmall.entity.DTO.PostCommentListDTO;
import com.fcl.ccmall.service.CcPostCommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-04-07
 */
@RestController
@RequestMapping("/post/comment")
public class CcPostCommentController {

    @Resource
    private CcPostCommentService postCommentService;

    @PostMapping("/list")
    public CommonResult getCommentList(@RequestBody PostCommentListDTO postCommentListDTO) {
        return postCommentService.getCommentList(postCommentListDTO);
    }

    @PostMapping("/create")
    public CommonResult createComment(@RequestBody PostCommentCreateDTO dto) {
        return postCommentService.createComment(dto);
    }
}

