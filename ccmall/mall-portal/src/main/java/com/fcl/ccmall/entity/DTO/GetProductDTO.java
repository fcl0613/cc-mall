package com.fcl.ccmall.entity.DTO;

import com.fcl.ccmall.common.entity.PageParam;
import lombok.Data;

@Data
public class GetProductDTO extends PageParam {
    private Integer categoryId;
    private String keyword;
    /**
     * 价格排序 false 价格升序 true 价格降序
     */
    private Boolean priceFlag;

    /**
     * 是否有货标志 true有货 false都找
     */
    private Boolean stockFlag;
}
