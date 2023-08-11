package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.GetADListDO;
import lombok.Data;

import java.util.List;

@Data
public class GetADListVO {
    private Long total;
    private List<GetADListDO> list;
}
