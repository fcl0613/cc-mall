package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.service.CcProductFavoriteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
 * @since 2023-03-24
 */
@RestController
@RequestMapping("/product/favorite")
public class CcProductFavoriteController {

    @Resource
    private CcProductFavoriteService productFavoriteService;

    @PostMapping("/add/{productId}")
    public CommonResult addFavorite(@PathVariable Integer productId) {
        return productFavoriteService.create(productId);
    }

    /**
     * 在商品详情页的取消收藏接口
     * @param productId
     * @return
     */
    @PostMapping("/cancel/{productId}")
    public CommonResult cancelFavorite(@PathVariable Integer productId) {
        return productFavoriteService.cancel(productId);
    }

    /**
     * 收藏列表页删除收藏商品接口
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public CommonResult removeFavorite(@PathVariable Integer id) {
        return productFavoriteService.delete(id);
    }

    @GetMapping("/list")
    public CommonResult queryFavorite(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Long pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "100") Long pageSize) {
        return productFavoriteService.getFavoriteList(pageNum, pageSize);
    }
}

