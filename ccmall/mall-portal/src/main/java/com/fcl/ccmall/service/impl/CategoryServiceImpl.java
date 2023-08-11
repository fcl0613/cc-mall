package com.fcl.ccmall.service.impl;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.ProductCategoryLevelEnum;
import com.fcl.ccmall.dao.CategoryDao;
import com.fcl.ccmall.entity.DO.CategoryDO;
import com.fcl.ccmall.entity.VO.CategoryVO;
import com.fcl.ccmall.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Override
    public CommonResult getAll() {
        List<CategoryDO> categoryDOS = categoryDao.getAll();
        ArrayList<CategoryVO> categoryVOS = new ArrayList<>();
        for (CategoryDO categoryDO : categoryDOS) {
            if (categoryDO.getLevel() == ProductCategoryLevelEnum.FATHER_LEVEL.getCode()) {
                CategoryVO categoryVO = new CategoryVO();
                categoryVO.setId(categoryDO.getId());
                categoryVO.setName(categoryDO.getName());
                ArrayList<CategoryVO> categoryVOS1 = new ArrayList<>();
                for (CategoryDO aDo : categoryDOS) {
                    if (aDo.getParentId() == categoryDO.getId()) {
                        CategoryVO categoryVO1 = new CategoryVO();
                        categoryVO1.setId(aDo.getId());
                        categoryVO1.setName(aDo.getName());
                        categoryVO1.setIcon(aDo.getIcon());
                        categoryVOS1.add(categoryVO1);
                    }
                }
                categoryVO.setList(categoryVOS1);
                categoryVOS.add(categoryVO);
            }
        }
        return CommonResult.success(categoryVOS);
    }
}
