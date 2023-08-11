package com.fcl.ccmall.entity.DTO;

import lombok.Data;

import java.util.List;

@Data
public class GenerateConfirmOrderDTO {
    private List<Integer> cartIds;
}
