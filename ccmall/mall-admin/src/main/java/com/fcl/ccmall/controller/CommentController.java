package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.CommentListDTO;
import com.fcl.ccmall.service.CommentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping("/list")
    public CommonResult getCommentList(@RequestBody CommentListDTO commentListDTO) {
        return commentService.getCommentList(commentListDTO);
    }

    @PostMapping("/remove/{id}")
    public CommonResult remove(@PathVariable Integer id) {
        return commentService.deleteComment(id);
    }
}
