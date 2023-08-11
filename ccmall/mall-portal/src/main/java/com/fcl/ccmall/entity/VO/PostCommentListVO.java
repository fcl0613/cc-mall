package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.model.CcPostComment;
import lombok.Data;

import java.util.List;

@Data
public class PostCommentListVO {
    private List<CcPostComment> list;
    private Long total;
}
