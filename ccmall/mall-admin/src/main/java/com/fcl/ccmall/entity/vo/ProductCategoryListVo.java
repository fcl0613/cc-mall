package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.GetProductCategoryListDo;
import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryListVo {
    private List<GetProductCategoryListDo> list;
    private Long size;
}
