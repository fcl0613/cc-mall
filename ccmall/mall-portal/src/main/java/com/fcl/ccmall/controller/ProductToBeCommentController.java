package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.service.ProductToBeCommentService;
import org.springframework.web.bind.annotation.GetMapping;
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
 * @since 2023-03-31
 */
@RestController
@RequestMapping("/product/to/comment")
public class ProductToBeCommentController {

    @Resource
    private ProductToBeCommentService productToBeCommentService;

    @GetMapping("/list")
    public CommonResult getList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Long pageNum,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "20") Long pageSize) {
        return productToBeCommentService.getProductList(pageNum, pageSize);
    }
}

