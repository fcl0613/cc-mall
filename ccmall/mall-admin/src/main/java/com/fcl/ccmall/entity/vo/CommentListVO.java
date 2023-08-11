package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.model.ProductComment;
import lombok.Data;

import java.util.List;

@Data
public class CommentListVO {
    private List<ProductComment> list;
    private Long total;
}
