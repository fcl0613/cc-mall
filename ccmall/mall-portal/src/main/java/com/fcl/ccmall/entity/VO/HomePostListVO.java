package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.entity.DO.HomePostListDO;
import com.fcl.ccmall.model.CcPost;
import lombok.Data;

import java.util.List;

@Data
public class HomePostListVO {
    private List<HomePostListDO> list;
    private Long total;
}
