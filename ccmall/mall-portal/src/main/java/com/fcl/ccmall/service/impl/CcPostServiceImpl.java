package com.fcl.ccmall.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.dao.PostDao;
import com.fcl.ccmall.entity.DO.HomePostListDO;
import com.fcl.ccmall.entity.DO.MyPostListDO;
import com.fcl.ccmall.entity.DTO.CreatePostDTO;
import com.fcl.ccmall.entity.DTO.PostListDTO;
import com.fcl.ccmall.entity.DTO.UpdatePostDTO;
import com.fcl.ccmall.entity.VO.HomePostListVO;
import com.fcl.ccmall.entity.VO.MyPostListVO;
import com.fcl.ccmall.entity.VO.PostDetailVO;
import com.fcl.ccmall.entity.VO.PostInfoVO;
import com.fcl.ccmall.mapper.CcPostLikeRelationMapper;
import com.fcl.ccmall.mapper.CcPostMapper;
import com.fcl.ccmall.model.CcPost;
import com.fcl.ccmall.model.CcPostLikeRelation;
import com.fcl.ccmall.service.CcPostService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-04-03
 */
@Service
public class CcPostServiceImpl extends ServiceImpl<CcPostMapper, CcPost> implements CcPostService {

    private final String GAP = ",";
    private final String FORMAT_TIME = "yyyy-MM-dd";

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private HttpServletRequest request;

    @Resource
    private PostDao postDao;

    @Resource
    private CcPostLikeRelationMapper postLikeRelationMapper;

    @Override
    public CommonResult getHomePostList(PostListDTO postListDTO) {
        Page<HomePostListDO> postPage = new Page<>(postListDTO.getPageNum(), postListDTO.getPageSize());
        Page<HomePostListDO> page = postDao.getHomePostList(postPage, postListDTO);
        List<HomePostListDO> records = page.getRecords();
        for (HomePostListDO record : records) {
            if(record.getContent().length() > 30) {
                record.setContent(record.getContent().substring(0, 30) + "...");
            }
            if (!StrUtil.isBlank(record.getPic())) {
                record.setPic(record.getPic().split(GAP)[0]);
            }
        }
        HomePostListVO homePostListVO = new HomePostListVO();
        homePostListVO.setList(records);
        homePostListVO.setTotal(page.getTotal());
        return CommonResult.success(homePostListVO);
    }

    @Override
    public CommonResult createPost(CreatePostDTO createPostDTO) {
        CcPost post = new CcPost();
        post.setCustomerId(getCustomerId());
        post.setContent(createPostDTO.getContent());
        post.setTitle(createPostDTO.getTitle());
        if (!Objects.isNull(createPostDTO.getPics())) {
            StringBuilder sb = new StringBuilder();
            for (String pic : createPostDTO.getPics()) {
                sb.append(pic);
                sb.append(GAP);
            }
            post.setPic(sb.toString());
        }
        post.setCommentCount(0);
        post.setLikeCount(0);
        post.setPageview(0);
        post.setCreateTime(LocalDateTime.now());
        save(post);
        return CommonResult.success();
    }

    @Override
    public CommonResult getMyPostList(PostListDTO postListDTO) {
        Integer customerId = getCustomerId();
        // title content pic
        Page<CcPost> postPage = new Page<>(postListDTO.getPageNum(), postListDTO.getPageSize());
        LambdaQueryWrapper<CcPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CcPost::getCustomerId, customerId);
        if (!StrUtil.isBlank(postListDTO.getTitle())) {
            queryWrapper.like(CcPost::getTitle, postListDTO.getTitle());
        }
        queryWrapper.orderByDesc(CcPost::getCreateTime);
        Page<CcPost> page = page(postPage, queryWrapper);
        List<MyPostListDO> myPostListDOS = new ArrayList<>();
        for (CcPost record : page.getRecords()) {
            MyPostListDO myPostListDO = new MyPostListDO();
            myPostListDO.setId(record.getId());
            myPostListDO.setTitle(record.getTitle());
            if (record.getContent().length() > 30) {
                myPostListDO.setContent(record.getContent().substring(0, 30));
            }
            if (!StrUtil.isBlank(record.getPic())) {
                myPostListDO.setPic(record.getPic().split(GAP)[0]);
            }
            myPostListDOS.add(myPostListDO);
        }
        MyPostListVO myPostListVO = new MyPostListVO();
        myPostListVO.setList(myPostListDOS);
        myPostListVO.setTotal(page.getTotal());
        return CommonResult.success(myPostListVO);
    }

    @Override
    public CommonResult deletePost(Integer id) {
        removeById(id);
        postLikeRelationMapper.delete(new LambdaUpdateWrapper<CcPostLikeRelation>()
        .eq(CcPostLikeRelation::getPostId, id));
        return CommonResult.success();
    }

    /**
     * 首页进入详情页
     * @param id
     * @return
     */
    @Override
    public CommonResult getInfo(Integer id) {
        CcPost post = getById(id);
        // 增加点击量
        postDao.incrementPageView(id);
        PostInfoVO postDetailVO = new PostInfoVO();
        postDetailVO.setId(post.getId());
        postDetailVO.setTitle(post.getTitle());
        postDetailVO.setContent(post.getContent());
        if (!StrUtil.isBlank(post.getPic())) {
            postDetailVO.setPics(Arrays.asList(post.getPic().split(GAP)));
        }
        postDetailVO.setCreateTime(DateUtil.format(post.getCreateTime(), FORMAT_TIME));
        Integer count = postLikeRelationMapper.selectCount(new LambdaQueryWrapper<CcPostLikeRelation>()
                .eq(CcPostLikeRelation::getCustomerId, getCustomerId())
                .eq(CcPostLikeRelation::getPostId, id));
        if (count == 0) {
            postDetailVO.setLikeStatus(false);
        }else  {
            postDetailVO.setLikeStatus(true);
        }
        return CommonResult.success(postDetailVO);
    }

    @Override
    public CommonResult updatePost(UpdatePostDTO updatePostDTO) {
        CcPost post = new CcPost();
        post.setId(updatePostDTO.getId());
        post.setTitle(updatePostDTO.getTitle());
        post.setContent(updatePostDTO.getContent());
        if (!Objects.isNull(updatePostDTO.getPics())) {
            StringBuilder sb = new StringBuilder();
            for (String pic : updatePostDTO.getPics()) {
                sb.append(pic);
                sb.append(GAP);
            }
            post.setPic(sb.toString());
        }else {
            post.setPic("");
        }
        updateById(post);
        return CommonResult.success();
    }

    /**
     * 作者获取自己的文章信息
     * @param id
     * @return
     */
    @Override
    public CommonResult getDetail(Integer id) {
        CcPost post = getById(id);
        PostDetailVO postDetailVO = new PostDetailVO();
        postDetailVO.setId(post.getId());
        postDetailVO.setTitle(post.getTitle());
        postDetailVO.setContent(post.getContent());
        if (!StrUtil.isBlank(post.getPic())) {
            postDetailVO.setPics(Arrays.asList(post.getPic().split(GAP)));
        }
        return CommonResult.success(postDetailVO);
    }

    @Override
    public CommonResult likePost(Integer id) {
        postDao.likePost(id);
        CcPostLikeRelation postLikeRelation = new CcPostLikeRelation();
        postLikeRelation.setPostId(id);
        postLikeRelation.setCustomerId(getCustomerId());
        postLikeRelationMapper.insert(postLikeRelation);
        return CommonResult.success();
    }

    @Override
    public CommonResult unLikePost(Integer id) {
        postDao.unLikePost(id);
        postLikeRelationMapper.delete(new LambdaQueryWrapper<CcPostLikeRelation>()
        .eq(CcPostLikeRelation::getCustomerId, getCustomerId())
        .eq(CcPostLikeRelation::getPostId, id));
        return CommonResult.success();
    }

    private Integer getCustomerId() {
        String token = request.getHeader("token");
        return jwtTokenUtils.getUserId(token);
    }
}
