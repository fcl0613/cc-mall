package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.GetProductDTO;
import com.fcl.ccmall.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @PostMapping("/list")
    public CommonResult getProductList(@RequestBody GetProductDTO dto) {
        return productService.getProductList(dto);
    }

    @GetMapping("/detail/{id}")
    public CommonResult getProductDetail(@PathVariable Integer id) {
        return productService.getProductDetail(id);
    }
}
