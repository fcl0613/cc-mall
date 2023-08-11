package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.CommentListDTO;
import com.fcl.ccmall.service.CommentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @RequestMapping("/list")
    public CommonResult getCommentList(@RequestBody CommentListDTO commentListDTO) {
        return commentService.getCommentList(commentListDTO);
    }
}
