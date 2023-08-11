package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.entity.DO.CartListDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartListVO {
    private List<CartListDO> list;
    private Long total;
    private BigDecimal totalPrice;
}
