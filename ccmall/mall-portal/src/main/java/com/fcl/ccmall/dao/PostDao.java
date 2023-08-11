package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.entity.DO.HomePostListDO;
import com.fcl.ccmall.entity.DTO.PostListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostDao {
    void likePost(@Param("id") Integer id);
    void unLikePost(@Param("id") Integer id);
    Page<HomePostListDO> getHomePostList(@Param("page") Page<HomePostListDO> page,
                                         @Param("dto")PostListDTO postListDTO);
    void incrementPageView(@Param("id") Integer id);
    void incrementCommentCount(@Param("id") Integer id);
}
