package com.fcl.ccmall.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class ConferredMenuDTO {
    private Integer roleId;
    private List<Integer> menuIdList;
}
