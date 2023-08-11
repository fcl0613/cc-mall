package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.GetCustomerListDO;
import lombok.Data;

import java.util.List;

@Data
public class GetCustomerListVO {
    private List<GetCustomerListDO> list;
    private Long total;
}
