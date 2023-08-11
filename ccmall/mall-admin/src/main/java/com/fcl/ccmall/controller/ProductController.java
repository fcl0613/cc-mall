package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.AddProductDTO;
import com.fcl.ccmall.entity.dto.BatchOperationDTO;
import com.fcl.ccmall.entity.dto.GetProductDTO;
import com.fcl.ccmall.entity.dto.ProductChangeStatusDTO;
import com.fcl.ccmall.entity.dto.RemoveProductDTO;
import com.fcl.ccmall.entity.dto.StockAddDTO;
import com.fcl.ccmall.entity.dto.UpdateProductDTO;
import com.fcl.ccmall.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-03-03
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductService productService;

    @PostMapping("/add")
    public CommonResult addProduct(@RequestBody AddProductDTO addProductDTO) {
       return productService.create(addProductDTO);
    }

    @PostMapping("/list")
    public CommonResult getProduct(@RequestBody GetProductDTO getProductDTO) {
        return productService.getProductList(getProductDTO);
    }

    @PostMapping("/update")
    public CommonResult updateProduct(@RequestBody UpdateProductDTO updateProductDTO) {
        return productService.updateProduct(updateProductDTO);
    }

    @PostMapping("/remove")
    public CommonResult removeProduct(@RequestBody RemoveProductDTO removeProductDTO) {
        return productService.removeProduct(removeProductDTO);
    }

    @GetMapping("/detail/{id}")
    public CommonResult getProductDetail(@PathVariable Integer id) {
        return productService.getProductDetailById(id);
    }

    @PostMapping("/change/publish")
    public CommonResult changePublishStatus(@RequestBody ProductChangeStatusDTO productChangeStatusDTO) {
        return productService.changePublishStatus(productChangeStatusDTO);
    }

    @PostMapping("/change/recommend")
    public CommonResult changeRecommendStatus(@RequestBody ProductChangeStatusDTO productChangeStatusDTO) {
        return productService.changeRecommendStatus(productChangeStatusDTO);
    }

    @PostMapping("/change/new")
    public CommonResult changeNewStatus(@RequestBody ProductChangeStatusDTO productChangeStatusDTO) {
        return productService.changeNewStatus(productChangeStatusDTO);
    }

    @PostMapping("/stock/add")
    public CommonResult stockAdd(@RequestBody StockAddDTO stockAddDTO) {
        return productService.stockAdd(stockAddDTO);
    }

    @PostMapping("/batch/publish")
    public CommonResult batchPublish(@RequestBody BatchOperationDTO batchOperationDTO) {
        return productService.batchPublish(batchOperationDTO);
    }

    @PostMapping("/batch/unPublish")
    public CommonResult batchUnPublish(@RequestBody BatchOperationDTO batchOperationDTO) {
        return productService.batchUnPublish(batchOperationDTO);
    }

    @PostMapping("/batch/recommend")
    public CommonResult batchRecommend(@RequestBody BatchOperationDTO batchOperationDTO) {
        return productService.batchRecommend(batchOperationDTO);
    }

    @PostMapping("/batch/unRecommend")
    public CommonResult batchUnRecommend(@RequestBody BatchOperationDTO batchOperationDTO) {
        return productService.batchUnRecommend(batchOperationDTO);
    }

    @PostMapping("/batch/new")
    public CommonResult batchNew(@RequestBody BatchOperationDTO batchOperationDTO) {
        return productService.batchNew(batchOperationDTO);
    }

    @PostMapping("/batch/unNew")
    public CommonResult batchUnNew(@RequestBody BatchOperationDTO batchOperationDTO) {
        return productService.batchUnNew(batchOperationDTO);
    }

    /**
     * 在优惠券页面查找商品需要的接口
     * @return
     */
    @GetMapping("/all")
    public CommonResult findAll(@RequestParam("name") String name) {
        return productService.findAll(name);
    }
}

