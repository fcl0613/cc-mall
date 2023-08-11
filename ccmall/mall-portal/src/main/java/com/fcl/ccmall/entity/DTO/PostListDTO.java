package com.fcl.ccmall.entity.DTO;

import com.fcl.ccmall.common.entity.PageParam;
import lombok.Data;

@Data
public class PostListDTO extends PageParam {
    private String title;
}
