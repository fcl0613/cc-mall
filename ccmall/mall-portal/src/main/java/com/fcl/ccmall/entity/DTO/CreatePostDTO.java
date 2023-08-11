package com.fcl.ccmall.entity.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CreatePostDTO {
    private String title;
    private String content;
    private List<String> pics;
}
