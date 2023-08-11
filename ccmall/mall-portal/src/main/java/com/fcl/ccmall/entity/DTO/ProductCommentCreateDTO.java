package com.fcl.ccmall.entity.DTO;

import lombok.Data;

@Data
public class ProductCommentCreateDTO {
    private Integer id;
    private String content;
    private Integer score;
}
