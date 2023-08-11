package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.GetProductListDO;
import lombok.Data;

import java.util.List;

@Data
public class GetProductListVO {
    private Long total;
    private List<GetProductListDO> list;
}
