package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.entity.dto.RemoveProductCategoryDTO;
import com.fcl.ccmall.model.ProductCategory;
import com.fcl.ccmall.service.ProductCategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-02-12
 */
@RestController
@RequestMapping("/product/category")
public class ProductCategoryController {
    @Resource
    private ProductCategoryService productCategoryService;

    /**
     * 获取商品分类
     * @param pageParam
     * @return
     */
    @GetMapping("/list")
    public CommonResult getProductCategory(PageParam pageParam) {
        return productCategoryService.getProductCategoryList(pageParam);
    }

    /**
     * 添加商品分类
     * @param productCategory
     * @return
     */
    @PostMapping("/add")
    public CommonResult addProductCategory(@RequestBody ProductCategory productCategory) {
        return productCategoryService.create(productCategory);
    }

    /**
     * 更新商品分类
     * @param productCategory
     * @return
     */
    @PostMapping("/update")
    public CommonResult updateProductCategory(@RequestBody ProductCategory productCategory) {
        return productCategoryService.updateProductCategory(productCategory);
    }

    /**
     * 删除商品分类
     * @param removeProductCategoryDTO
     * @return
     */
    @PostMapping("/remove")
    public CommonResult removeProductCategory(@RequestBody RemoveProductCategoryDTO removeProductCategoryDTO) {
        return productCategoryService.removeProductCategoryById(removeProductCategoryDTO.getCategoryId());
    }

    /**
     * 获取商品分类编号
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public CommonResult getProductCateGoryDetail(@PathVariable Integer id) {
        return productCategoryService.getProductCategoryDetail(id);
    }

    /**
     * 获取所有父级分类的列表
     * @return
     */
    @GetMapping("/getParentList")
    public CommonResult getParentList() {
        return productCategoryService.getParentList();
    }

    /**
     *修改导航栏显示状态
     * @param categoryId
     * @param status
     * @return
     */
    @PostMapping("/update/navStatus")
    public CommonResult updateNavStatus(@RequestParam("id") Integer categoryId,
                                        @RequestParam("status") Integer status) {
        return productCategoryService.updateNavStatus(categoryId, status);
    }

    /**
     * 修改显示状态
     * @param categoryId
     * @param status
     * @return
     */
    @PostMapping("/update/showStatus")
    public CommonResult updateShowStatus(@RequestParam("id") Integer categoryId,
                                        @RequestParam("status") Integer status) {
        return productCategoryService.updateShowStatus(categoryId, status);
    }

    @GetMapping("/all")
    public CommonResult getAllCategory() {
        return productCategoryService.getAllCategory();
    }
}

