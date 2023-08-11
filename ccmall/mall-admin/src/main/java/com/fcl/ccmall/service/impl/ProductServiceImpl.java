package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.ProductPublishEnum;
import com.fcl.ccmall.dao.ProductDao;
import com.fcl.ccmall.entity.DO.FindAllProductDO;
import com.fcl.ccmall.entity.DO.GetProductListDO;
import com.fcl.ccmall.entity.dto.*;
import com.fcl.ccmall.entity.vo.GetProductDetailByIdVO;
import com.fcl.ccmall.entity.vo.GetProductListVO;
import com.fcl.ccmall.enums.ProductNewEnum;
import com.fcl.ccmall.enums.ProductRecommendEnum;
import com.fcl.ccmall.mapper.ProductMapper;
import com.fcl.ccmall.model.Product;
import com.fcl.ccmall.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-03-03
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final String GAP = ",";

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductDao productDao;

    @Override
    public CommonResult create(AddProductDTO addProductDTO) {
        Product product = new Product();
        product.setCategoryId(addProductDTO.getCategoryId());
        product.setCategoryIdParent(addProductDTO.getCategoryIdParent());
        product.setProductName(addProductDTO.getProductName());
        product.setSubtitle(addProductDTO.getSubtitle());
        product.setPrice(addProductDTO.getPrice());
        product.setStock(addProductDTO.getStock());
        product.setGiftPoint(addProductDTO.getGiftPoint());
        product.setPublishStatus(addProductDTO.getPublishStatus());
        product.setDescription(addProductDTO.getDescription());
        product.setPromotionPrice(addProductDTO.getPromotionPrice());
        product.setPromotionStartTime(addProductDTO.getPromotionStartTime());
        product.setPromotionEndTime(addProductDTO.getPromotionEndTime());
        product.setPromotionPerLimit(addProductDTO.getPromotionPerLimit());
        product.setRecommendStatus(addProductDTO.getRecommendStatus());
        product.setNewStatus(addProductDTO.getNewStatus());
        List<String> productCoverList = addProductDTO.getProductCover();
        StringBuilder builder = new StringBuilder();
        for (String s : productCoverList) {
            builder.append(s);
            builder.append(GAP);
        }
        product.setProductCover(builder.toString());
        builder.setLength(0);
        List<String> productPicsList = addProductDTO.getProductPics();
        for (String s : productPicsList) {
            builder.append(s);
            builder.append(GAP);
        }
        product.setProductPics(builder.toString());
        productMapper.insert(product);
        return CommonResult.success();
    }

    @Override
    public CommonResult getProductList(GetProductDTO getProductDTO) {
        Page<GetProductListDO> productListDOPage =
                new Page<>(getProductDTO.getPageNum(), getProductDTO.getPageSize());
        // 取消sql优化
//        productListDOPage.setOptimizeCountSql(false);
        // 搜索条件
        // 商品名称 分类 上架状态
        // 商品id 图片 名称 价格 上架新品推荐 库存
        Page<GetProductListDO> page = productDao.getProductList(productListDOPage, getProductDTO);
        GetProductListVO getProductListVO = new GetProductListVO();
        List<GetProductListDO> records = page.getRecords();
        for (GetProductListDO record : records) {
            String s = record.getProductCover().split(GAP)[0];
            record.setProductCover(s);
        }
        getProductListVO.setList(records);
        getProductListVO.setTotal(page.getTotal());
        return CommonResult.success(getProductListVO);
    }

    @Override
    public CommonResult updateProduct(UpdateProductDTO updateProductDTO) {
        Product product = new Product();
        product.setId(updateProductDTO.getId());
        product.setCategoryId(updateProductDTO.getCategoryId());
        product.setCategoryIdParent(updateProductDTO.getCategoryIdParent());
        product.setProductName(updateProductDTO.getProductName());
        product.setSubtitle(updateProductDTO.getSubtitle());
        product.setPrice(updateProductDTO.getPrice());
        product.setStock(updateProductDTO.getStock());
        product.setGiftPoint(updateProductDTO.getGiftPoint());
        product.setPublishStatus(updateProductDTO.getPublishStatus());
        product.setDescription(updateProductDTO.getDescription());
        product.setPromotionPrice(updateProductDTO.getPromotionPrice());
        product.setPromotionStartTime(updateProductDTO.getPromotionStartTime());
        product.setPromotionEndTime(updateProductDTO.getPromotionEndTime());
        product.setPromotionPerLimit(updateProductDTO.getPromotionPerLimit());
        product.setRecommendStatus(updateProductDTO.getRecommendStatus());
        product.setNewStatus(updateProductDTO.getNewStatus());
        List<String> productCoverList = updateProductDTO.getProductCover();
        StringBuilder builder = new StringBuilder();
        for (String s : productCoverList) {
            builder.append(s);
            builder.append(GAP);
        }
        product.setProductCover(builder.toString());
        builder.setLength(0);
        List<String> productPicsList = updateProductDTO.getProductPics();
        for (String s : productPicsList) {
            builder.append(s);
            builder.append(GAP);
        }
        product.setProductPics(builder.toString());
        productMapper.updateById(product);
        return CommonResult.success();
    }

    @Override
    public CommonResult removeProduct(RemoveProductDTO removeProductDTO) {
        productMapper.deleteBatchIds(removeProductDTO.getList());
        return CommonResult.success();
    }

    @Override
    public CommonResult getProductDetailById(Integer id) {
        Product product = productMapper.selectById(id);
        GetProductDetailByIdVO productVO = new GetProductDetailByIdVO();
        productVO.setId(product.getId());
        productVO.setProductName(product.getProductName());
        productVO.setCategoryId(product.getCategoryId());
        productVO.setDescription(product.getDescription());
        productVO.setGiftPoint(product.getGiftPoint());
        productVO.setNewStatus(product.getNewStatus());
        productVO.setPrice(product.getPrice());
        productVO.setProductCover(Arrays.asList(product.getProductCover().split(GAP)));
        productVO.setProductPics(Arrays.asList(product.getProductPics().split(GAP)));
        productVO.setPromotionEndTime(product.getPromotionEndTime());
        productVO.setPromotionPerLimit(product.getPromotionPerLimit());
        productVO.setPromotionPrice(product.getPromotionPrice());
        productVO.setPromotionStartTime(product.getPromotionStartTime());
        productVO.setPublishStatus(product.getPublishStatus());
        productVO.setRecommendStatus(product.getRecommendStatus());
        productVO.setStock(product.getStock());
        productVO.setSubtitle(product.getSubtitle());
        productVO.setCategoryIdParent(product.getCategoryIdParent());
        return CommonResult.success(productVO);
    }

    @Override
    public CommonResult changePublishStatus(ProductChangeStatusDTO productChangeStatusDTO) {
        productMapper.update(null, new LambdaUpdateWrapper<Product>()
        .eq(Product::getId, productChangeStatusDTO.getId())
        .set(Product::getPublishStatus, productChangeStatusDTO.getStatus()));
        return CommonResult.success();
    }

    @Override
    public CommonResult changeRecommendStatus(ProductChangeStatusDTO productChangeStatusDTO) {
        productMapper.update(null, new LambdaUpdateWrapper<Product>()
                .eq(Product::getId, productChangeStatusDTO.getId())
                .set(Product::getRecommendStatus, productChangeStatusDTO.getStatus()));
        return CommonResult.success();
    }

    @Override
    public CommonResult changeNewStatus(ProductChangeStatusDTO productChangeStatusDTO) {
        productMapper.update(null, new LambdaUpdateWrapper<Product>()
                .eq(Product::getId, productChangeStatusDTO.getId())
                .set(Product::getNewStatus, productChangeStatusDTO.getStatus()));
        return CommonResult.success();
    }

    @Override
    public CommonResult stockAdd(StockAddDTO stockAddDTO) {
        productMapper.update(null, new LambdaUpdateWrapper<Product>()
        .eq(Product::getId, stockAddDTO.getId())
        .set(Product::getStock, stockAddDTO.getStock()));
        return CommonResult.success();
    }

    @Override
    public CommonResult batchPublish(BatchOperationDTO batchOperationDTO) {
        productDao.batchProductPublishStatus(batchOperationDTO.getList(),
                ProductPublishEnum.PUBLISH.getCode());
        return CommonResult.success();
    }

    @Override
    public CommonResult batchUnPublish(BatchOperationDTO batchOperationDTO) {
        productDao.batchProductPublishStatus(batchOperationDTO.getList(),
                ProductPublishEnum.UN_PUBLISH.getCode());
        return CommonResult.success();
    }

    @Override
    public CommonResult batchRecommend(BatchOperationDTO batchOperationDTO) {
        productDao.batchProductRecommendStatus(batchOperationDTO.getList(),
                ProductRecommendEnum.RECOMMEND.getCode());
        return CommonResult.success();
    }

    @Override
    public CommonResult batchUnRecommend(BatchOperationDTO batchOperationDTO) {
        productDao.batchProductRecommendStatus(batchOperationDTO.getList(),
                ProductRecommendEnum.UN_RECOMMEND.getCode());
        return CommonResult.success();
    }

    @Override
    public CommonResult batchNew(BatchOperationDTO batchOperationDTO) {
        productDao.batchProductNewStatus(batchOperationDTO.getList(),
                ProductNewEnum.NEW.getCode());
        return CommonResult.success();
    }

    @Override
    public CommonResult batchUnNew(BatchOperationDTO batchOperationDTO) {
        productDao.batchProductNewStatus(batchOperationDTO.getList(),
                ProductNewEnum.UN_NEW.getCode());
        return CommonResult.success();
    }

    @Override
    public CommonResult findAll(String name) {
        return CommonResult.success(productDao.findAll(name));
    }
}
