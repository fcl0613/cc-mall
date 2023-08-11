package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.exception.Asserts;
import com.fcl.ccmall.entity.VO.FavoriteListVO;
import com.fcl.ccmall.mapper.CcProductFavoriteMapper;
import com.fcl.ccmall.mapper.ProductMapper;
import com.fcl.ccmall.model.CcProductFavorite;
import com.fcl.ccmall.model.Product;
import com.fcl.ccmall.service.CcProductFavoriteService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-03-24
 */
@Service
public class CcProductFavoriteServiceImpl extends ServiceImpl<CcProductFavoriteMapper, CcProductFavorite> implements CcProductFavoriteService {

    private final String GAP = ",";

    @Resource
    private CcProductFavoriteMapper productFavoriteMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private HttpServletRequest request;

    @Resource
    JwtTokenUtils jwtTokenUtils;

    @Override
    public CommonResult create(Integer productId) {
        Integer customerId = getCustomerId();
        Integer count = productFavoriteMapper.selectCount(new LambdaQueryWrapper<CcProductFavorite>()
                .eq(CcProductFavorite::getProductId, productId)
                .eq(CcProductFavorite::getCustomerId, customerId));
        if (count > 0) {
            Asserts.fail("当前商品已在收藏夹内");
        }
        Product product = productMapper.selectById(productId);
        CcProductFavorite productFavorite = new CcProductFavorite();
        productFavorite.setCustomerId(customerId);
        productFavorite.setProductId(productId);
        productFavorite.setProductCover(product.getProductCover().split(GAP)[0]);
        productFavorite.setProductName(product.getProductName());
        productFavorite.setProductPrice(product.getPrice());
        productFavoriteMapper.insert(productFavorite);
        return CommonResult.success();
    }

    @Override
    public CommonResult cancel(Integer productId) {
        productFavoriteMapper.delete(new LambdaQueryWrapper<CcProductFavorite>()
        .eq(CcProductFavorite::getCustomerId, getCustomerId())
        .eq(CcProductFavorite::getProductId, productId));
        return CommonResult.success();
    }

    @Override
    public CommonResult getFavoriteList(Long pageNum, Long pageSize) {
        Page<CcProductFavorite> favoritePage = new Page<>(pageNum, pageSize);
        Page<CcProductFavorite> page =
                productFavoriteMapper.selectPage(favoritePage,
                        new LambdaQueryWrapper<CcProductFavorite>()
                .eq(CcProductFavorite::getCustomerId, getCustomerId()));
        FavoriteListVO favoriteListVO = new FavoriteListVO();
        favoriteListVO.setList(page.getRecords());
        favoriteListVO.setTotal(page.getTotal());
        return CommonResult.success(favoriteListVO);
    }

    @Override
    public CommonResult delete(Integer id) {
        productFavoriteMapper.deleteById(id);
        return CommonResult.success();
    }

    private Integer getCustomerId() {
        String token = request.getHeader("token");
        return jwtTokenUtils.getUserId(token);
    }
}
