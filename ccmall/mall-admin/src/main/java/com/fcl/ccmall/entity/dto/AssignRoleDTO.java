package com.fcl.ccmall.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssignRoleDTO {
    private Integer userId;
    private List<Integer> roleIdList;
}
