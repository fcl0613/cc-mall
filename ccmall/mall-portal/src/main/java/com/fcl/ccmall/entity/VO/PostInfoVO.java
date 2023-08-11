package com.fcl.ccmall.entity.VO;

import lombok.Data;

import java.util.List;

@Data
public class PostInfoVO {
    private Integer id;
    private String title;
    private String content;
    private List<String> pics;
    private String createTime;
    private Boolean likeStatus;
}
