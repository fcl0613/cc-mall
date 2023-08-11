package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.GetJobListDO;
import lombok.Data;

import java.util.List;

@Data
public class GetJobListVO {
    private List<GetJobListDO> list;
    private Long total;
}
