package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.entity.DO.ProductListDO;
import lombok.Data;

import java.util.List;

@Data
public class ProductListVO {
   private List<ProductListDO> list;
   private Long total;
}
