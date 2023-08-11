package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.model.CcProductFavorite;
import lombok.Data;

import java.util.List;

@Data
public class FavoriteListVO {
    private List<CcProductFavorite> list;
    private Long total;
}
