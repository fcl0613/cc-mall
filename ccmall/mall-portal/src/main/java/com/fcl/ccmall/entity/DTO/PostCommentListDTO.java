package com.fcl.ccmall.entity.DTO;

import com.fcl.ccmall.common.entity.PageParam;
import lombok.Data;

@Data
public class PostCommentListDTO extends PageParam {
    private Integer postId;
}
