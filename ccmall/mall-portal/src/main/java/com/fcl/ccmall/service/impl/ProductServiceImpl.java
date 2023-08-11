package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.ProductPublishEnum;
import com.fcl.ccmall.common.exception.Asserts;
import com.fcl.ccmall.dao.PorProductDao;
import com.fcl.ccmall.entity.DO.ProductListDO;
import com.fcl.ccmall.entity.DTO.GetProductDTO;
import com.fcl.ccmall.entity.VO.ProductDetailVO;
import com.fcl.ccmall.entity.VO.ProductListVO;
import com.fcl.ccmall.mapper.CcProductFavoriteMapper;
import com.fcl.ccmall.mapper.ProductCommentMapper;
import com.fcl.ccmall.mapper.ProductMapper;
import com.fcl.ccmall.model.CcProductFavorite;
import com.fcl.ccmall.model.Product;
import com.fcl.ccmall.model.ProductComment;
import com.fcl.ccmall.service.ProductService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final String GAP = ",";

    @Resource
    private PorProductDao porProductDao;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CcProductFavoriteMapper productFavoriteMapper;

    @Resource
    private HttpServletRequest request;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private ProductCommentMapper productCommentMapper;

    @Override
    public CommonResult getProductList(GetProductDTO getProductDTO) {
        Page<ProductListDO> pageDO = new Page<>(getProductDTO.getPageNum(), getProductDTO.getPageSize());
        Page<ProductListDO> productList = porProductDao.getProductList(pageDO, getProductDTO);
        List<ProductListDO> productListDOS = productList.getRecords();
        for (ProductListDO productListDO : productListDOS) {
            productListDO.setProductCover(productListDO.getProductCover().split(GAP)[0]);
        }
        ProductListVO productListVO = new ProductListVO();
        productListVO.setList(productListDOS);
        productListVO.setTotal(productList.getTotal());
        return CommonResult.success(productListVO);
    }

    @Override
    public CommonResult getProductDetail(Integer id) {
        Product product = productMapper.selectById(id);
        if(Objects.isNull(product)) {
            Asserts.fail("当前商品不存在");
        }
        if (ProductPublishEnum.UN_PUBLISH.getCode().equals(product.getPublishStatus())) {
            Asserts.fail("当前商品已下架");
        }
        Integer count = productFavoriteMapper.selectCount(new LambdaQueryWrapper<CcProductFavorite>()
                .eq(CcProductFavorite::getCustomerId, getCustomerId())
                .eq(CcProductFavorite::getProductId, id));
        // TODO 这里还需要获取当前商品评论相关的信息等信息 还有一个广告 评论相关暂时默认0
        // 获取商品的总评论数
        Integer commentCount = productCommentMapper.selectCount(new LambdaQueryWrapper<ProductComment>()
                .eq(ProductComment::getProductId, id));
        // 获取差评数量
        Integer negativeCount = productCommentMapper.selectCount(new LambdaQueryWrapper<ProductComment>()
                .eq(ProductComment::getProductId, id)
                .eq(ProductComment::getScore, 1));
        // 获取好评数量
        Integer positiveCount = productCommentMapper.selectCount(new LambdaQueryWrapper<ProductComment>()
                .eq(ProductComment::getProductId, id)
                .eq(ProductComment::getScore, 5));
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setCommentCount(commentCount);
        if (count == 0) {
            productDetailVO.setFavoriteFlag(false);
        }else {
            productDetailVO.setFavoriteFlag(true);
        }
        productDetailVO.setNegativeCount(negativeCount);
        productDetailVO.setNeutralCount(commentCount - negativeCount - positiveCount);
        productDetailVO.setPositiveCount(positiveCount);
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setProductCover(Arrays.asList(product.getProductCover().split(GAP)));
        productDetailVO.setProductName(product.getProductName());
        productDetailVO.setProductPics(Arrays.asList(product.getProductPics().split(GAP)));
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setDescription(product.getDescription());
        return CommonResult.success(productDetailVO);
    }

    private Integer getCustomerId() {
        String token = request.getHeader("token");
        return jwtTokenUtils.getUserId(token);
    }
}

