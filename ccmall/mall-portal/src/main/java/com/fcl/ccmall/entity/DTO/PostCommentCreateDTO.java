package com.fcl.ccmall.entity.DTO;

import lombok.Data;

@Data
public class PostCommentCreateDTO {
    private Integer postId;
    private String content;
}
