package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.CreatePostDTO;
import com.fcl.ccmall.entity.DTO.PostListDTO;
import com.fcl.ccmall.entity.DTO.UpdatePostDTO;
import com.fcl.ccmall.service.CcPostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 * @since 2023-04-03
 */
@RestController
@RequestMapping("/post")
public class CcPostController {

    @Resource
    private CcPostService postService;

    @PostMapping("/home/list")
    public CommonResult getHomePostList(@RequestBody PostListDTO postListDTO) {
        return postService.getHomePostList(postListDTO);
    }

    @PostMapping("/create")
    public CommonResult create(@RequestBody CreatePostDTO createPostDTO) {
        return postService.createPost(createPostDTO);
    }

    @PostMapping("/my/list")
    public CommonResult getMyPostList(@RequestBody PostListDTO postListDTO) {
        return postService.getMyPostList(postListDTO);
    }

    @GetMapping("/my/detail/{id}")
    public CommonResult getDetail(@PathVariable Integer id) {
        return postService.getDetail(id);
    }

    @PostMapping("/remove/{id}")
    public CommonResult delete(@PathVariable Integer id) {
        return postService.deletePost(id);
    }

    @GetMapping("/info/{id}")
    public CommonResult getInfo(@PathVariable Integer id) {
        return postService.getInfo(id);
    }

    @PostMapping("/update")
    public CommonResult updatePost(@RequestBody UpdatePostDTO updatePostDTO) {
        return postService.updatePost(updatePostDTO);
    }

    @PostMapping("/like/{id}")
    public CommonResult likePost(@PathVariable Integer id) {
        return postService.likePost(id);
    }

    @PostMapping("/unLike/{id}")
    public CommonResult unLikePost(@PathVariable Integer id) {
        return postService.unLikePost(id);
    }
}

