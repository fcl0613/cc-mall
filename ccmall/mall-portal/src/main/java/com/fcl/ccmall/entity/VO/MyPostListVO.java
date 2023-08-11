package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.entity.DO.MyPostListDO;
import lombok.Data;

import java.util.List;

@Data
public class MyPostListVO {
    private List<MyPostListDO> list;
    private Long total;
}
