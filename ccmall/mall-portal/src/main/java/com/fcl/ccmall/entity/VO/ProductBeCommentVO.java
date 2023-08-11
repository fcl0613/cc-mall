package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.model.ProductToBeComment;
import lombok.Data;

import java.util.List;

@Data
public class ProductBeCommentVO {
    private List<ProductToBeComment> list;
    private Long total;
}
