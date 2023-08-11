package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.ProductCommentCreateDTO;
import com.fcl.ccmall.service.ProductCommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-04-01
 */
@RestController
@RequestMapping("/product/comment")
public class ProductCommentController {

    @Resource
    private ProductCommentService productCommentService;

    @PostMapping("/create")
    public CommonResult create(@RequestBody ProductCommentCreateDTO productCommentCreateDTO) {
        return productCommentService.create(productCommentCreateDTO);
    }
}

