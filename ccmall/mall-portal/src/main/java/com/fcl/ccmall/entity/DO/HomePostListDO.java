package com.fcl.ccmall.entity.DO;

import lombok.Data;

@Data
public class HomePostListDO {
    private String username;
    private String avatar;
    private Integer id;
    private String title;
    private String content;
    private String pic;
    private Integer pageview;
    private Integer likeCount;
    private Integer commentCount;
}
