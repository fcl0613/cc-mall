package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.dao.PostDao;
import com.fcl.ccmall.entity.DTO.PostCommentCreateDTO;
import com.fcl.ccmall.entity.DTO.PostCommentListDTO;
import com.fcl.ccmall.entity.VO.PostCommentListVO;
import com.fcl.ccmall.mapper.CcPostCommentMapper;
import com.fcl.ccmall.mapper.CustomerMapper;
import com.fcl.ccmall.model.CcPostComment;
import com.fcl.ccmall.model.Customer;
import com.fcl.ccmall.service.CcPostCommentService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-04-07
 */
@Service
public class CcPostCommentServiceImpl extends ServiceImpl<CcPostCommentMapper, CcPostComment> implements CcPostCommentService {

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private HttpServletRequest request;

    @Resource
    private PostDao postDao;

    @Override
    public CommonResult getCommentList(PostCommentListDTO postCommentListDTO) {
        Page<CcPostComment> queryPage = new Page<>(postCommentListDTO.getPageNum(),
                postCommentListDTO.getPageSize());
        Page<CcPostComment> page = page(queryPage, new LambdaQueryWrapper<CcPostComment>()
                .eq(CcPostComment::getPostId, postCommentListDTO.getPostId())
        .orderByDesc(CcPostComment::getCreateTime));
        PostCommentListVO postCommentListVO = new PostCommentListVO();
        postCommentListVO.setList(page.getRecords());
        postCommentListVO.setTotal(page.getTotal());
        return CommonResult.success(postCommentListVO);
    }

    @Override
    public CommonResult createComment(PostCommentCreateDTO dto) {
        CcPostComment postComment = new CcPostComment();
        postComment.setPostId(dto.getPostId());
        postComment.setContent(dto.getContent());
        postComment.setCreateTime(LocalDateTime.now());
        Integer customerId = getCustomerId();
        Customer customer = customerMapper.selectById(customerId);
        postComment.setCustomerId(customerId);
        postComment.setUsername(customer.getUsername());
        postComment.setAvatar(customer.getAvatar());
        save(postComment);
        postDao.incrementCommentCount(dto.getPostId());
        return CommonResult.success();
    }

    private Integer getCustomerId() {
        String token = request.getHeader("token");
        return jwtTokenUtils.getUserId(token);
    }
}
