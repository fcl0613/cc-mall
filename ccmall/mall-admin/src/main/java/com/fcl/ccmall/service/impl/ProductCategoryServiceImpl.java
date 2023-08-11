package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.common.enums.ProductCategoryLevelEnum;
import com.fcl.ccmall.common.exception.Asserts;
import com.fcl.ccmall.dao.ProductCategoryDao;
import com.fcl.ccmall.entity.DO.GetProductCategoryListDo;
import com.fcl.ccmall.entity.DO.ParentCategoryDO;
import com.fcl.ccmall.entity.vo.CascaderItemVO;
import com.fcl.ccmall.entity.vo.CascaderVO;
import com.fcl.ccmall.entity.vo.ProductCategoryListVo;
import com.fcl.ccmall.mapper.ProductCategoryMapper;
import com.fcl.ccmall.model.ProductCategory;
import com.fcl.ccmall.service.ProductCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-02-12
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Resource
    private ProductCategoryDao productCategoryDao;

    @Resource
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public CommonResult getProductCategoryList(PageParam pageParam) {
        Page<GetProductCategoryListDo> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        Page<GetProductCategoryListDo> categoryPage = productCategoryDao.getProductCategoryList(page);
        ProductCategoryListVo productCategoryListVo = new ProductCategoryListVo();
        productCategoryListVo.setList(categoryPage.getRecords());
        productCategoryListVo.setSize(categoryPage.getTotal());
        return CommonResult.success(productCategoryListVo);
    }

    @Override
    public CommonResult create(ProductCategory productCategory) {
        int count = this.count(new LambdaQueryWrapper<ProductCategory>()
                .eq(ProductCategory::getName, productCategory.getName()));
        if (count > 0) {
            Asserts.fail("当前分类名已存在");
        }
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        }else {
            productCategory.setLevel(1);
        }
        this.save(productCategory);
        return CommonResult.success();
    }

    @Override
    public CommonResult updateProductCategory(ProductCategory productCategory) {
        int count = this.count(new LambdaQueryWrapper<ProductCategory>()
                .eq(ProductCategory::getName, productCategory.getName())
                .ne(ProductCategory::getId, productCategory.getId()));
        if (count > 0) {
            Asserts.fail("当前分类名已存在");
        }
        this.updateById(productCategory);
        return CommonResult.success();
    }

    @Override
    public CommonResult removeProductCategoryById(Integer id) {
        ProductCategory productCategory = productCategoryMapper.selectById(id);
        // 后面想到了再写
        if (productCategory.getLevel() == 0) {
            // 如果是父级分类下还有分类就不能删除
        }
        this.removeById(id);
        return CommonResult.success();
    }

    @Override
    public CommonResult getProductCategoryDetail(Integer id) {
        ProductCategory productCategory = this.getById(id);
        return CommonResult.success(productCategory);
    }

    @Override
    public CommonResult getParentList() {
        List<ParentCategoryDO> list = productCategoryDao.getAllParentCategoryList();
        return CommonResult.success(list);
    }

    @Override
    public CommonResult updateNavStatus(Integer categoryId, Integer status) {
        productCategoryMapper.update(null, new LambdaUpdateWrapper<ProductCategory>()
        .eq(ProductCategory::getId, categoryId)
        .set(ProductCategory::getNavStatus, status));
        return CommonResult.success();
    }

    @Override
    public CommonResult updateShowStatus(Integer categoryId, Integer status) {
        productCategoryMapper.update(null, new LambdaUpdateWrapper<ProductCategory>()
        .eq(ProductCategory::getId, categoryId)
        .set(ProductCategory::getShowStatus, status));
        return CommonResult.success();
    }

    @Override
    public CommonResult getAllCategory() {
        List<ProductCategory> productCategories = productCategoryMapper.selectList(null);
        ArrayList<CascaderVO> cascaderVOS = new ArrayList<>();
        for (ProductCategory productCategory : productCategories) {
            if (ProductCategoryLevelEnum.FATHER_LEVEL.getCode() == productCategory.getLevel()) {
                CascaderVO cascaderVO = new CascaderVO();
                cascaderVO.setValue(productCategory.getId());
                cascaderVO.setLabel(productCategory.getName());
                ArrayList<CascaderItemVO> cascaderItemVOS = new ArrayList<>();
                for (ProductCategory category : productCategories) {
                    if (productCategory.getId() == category.getParentId()) {
                        CascaderItemVO cascaderItemVO = new CascaderItemVO();
                        cascaderItemVO.setValue(category.getId());
                        cascaderItemVO.setLabel(category.getName());
                        cascaderItemVOS.add(cascaderItemVO);
                    }
                }
                cascaderVO.setChildren(cascaderItemVOS);
                cascaderVOS.add(cascaderVO);
            }
        }
        return CommonResult.success(cascaderVOS);
    }
}
