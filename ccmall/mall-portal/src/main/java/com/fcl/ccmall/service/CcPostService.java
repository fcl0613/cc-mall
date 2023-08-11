package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.CreatePostDTO;
import com.fcl.ccmall.entity.DTO.PostListDTO;
import com.fcl.ccmall.entity.DTO.UpdatePostDTO;
import com.fcl.ccmall.model.CcPost;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-04-03
 */
public interface CcPostService extends IService<CcPost> {

    CommonResult getHomePostList(PostListDTO postListDTO);

    CommonResult createPost(CreatePostDTO createPostDTO);

    CommonResult getMyPostList(PostListDTO postListDTO);

    CommonResult deletePost(Integer id);

    CommonResult getInfo(Integer id);

    CommonResult updatePost(UpdatePostDTO updatePostDTO);

    CommonResult getDetail(Integer id);

    CommonResult likePost(Integer id);

    CommonResult unLikePost(Integer id);
}
