package com.fcl.ccmall.service.impl;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.dao.HomeDao;
import com.fcl.ccmall.entity.DO.AdvertisingDO;
import com.fcl.ccmall.entity.DO.CommonProductDO;
import com.fcl.ccmall.entity.DO.HomeCateGoryDO;
import com.fcl.ccmall.entity.VO.HomeContentVO;
import com.fcl.ccmall.service.HomeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    private final String GAP = ",";

    @Resource
    private HomeDao homeDao;

    @Override
    public CommonResult content() {
        // 广告
        List<AdvertisingDO> homeAdvertiseList = homeDao.getHomeAdvertiseList();
        // 分类
        List<HomeCateGoryDO> homeCategory = homeDao.getHomeCategory();
        // 推荐商品
        List<CommonProductDO> homeRecommendProduct = homeDao.getHomeRecommendProduct();
        handleProductCover(homeRecommendProduct);
        // 新品
        List<CommonProductDO> homeNewProduct = homeDao.getHomeNewProduct();
        handleProductCover(homeNewProduct);
        // 春日茶礼
        List<CommonProductDO> homeTeaProductList = homeDao.getHomeProductList(2);
        handleProductCover(homeTeaProductList);
        // 美酒佳酿
        List<CommonProductDO> homeWineProductList = homeDao.getHomeProductList(1);
        handleProductCover(homeWineProductList);
        // 休闲零食
        List<CommonProductDO> homeSnackProductList = homeDao.getHomeProductList(33);
        handleProductCover(homeSnackProductList);
        HomeContentVO homeContentVO = new HomeContentVO();
        homeContentVO.setHomeAdvertiseList(homeAdvertiseList);
        homeContentVO.setHomeCategory(homeCategory);
        homeContentVO.setHomeNewProduct(homeNewProduct);
        homeContentVO.setHomeSnackProductList(homeSnackProductList);
        homeContentVO.setHomeTeaProductList(homeTeaProductList);
        homeContentVO.setHomeRecommendProduct(homeRecommendProduct);
        homeContentVO.setHomeWineProductList(homeWineProductList);
        return CommonResult.success(homeContentVO);
    }

    private void handleProductCover(List<CommonProductDO> list) {
        for (CommonProductDO commonProductDO : list) {
            String[] split = commonProductDO.getProductCover().split(GAP);
            commonProductDO.setProductCover(split[0]);
        }
    }
}
