package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.WebLogDo;
import lombok.Data;

import java.util.List;

@Data
public class WebLogVo {
    private Long size;
    private List<WebLogDo> list;
}
