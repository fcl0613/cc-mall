package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.entity.DO.AdvertisingDO;
import com.fcl.ccmall.entity.DO.CommonProductDO;
import com.fcl.ccmall.entity.DO.HomeCateGoryDO;
import lombok.Data;

import java.util.List;

@Data
public class HomeContentVO {
    List<AdvertisingDO> homeAdvertiseList;
    List<HomeCateGoryDO> homeCategory;
    List<CommonProductDO> homeRecommendProduct;
    List<CommonProductDO> homeNewProduct;
    List<CommonProductDO> homeTeaProductList;
    List<CommonProductDO> homeWineProductList;
    List<CommonProductDO> homeSnackProductList;
}
