package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.model.SysMenu;
import com.fcl.ccmall.service.SysMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-01-15
 */
@RestController
@RequestMapping("/sysmenu")
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuService;

    @PostMapping("/create")
    public CommonResult create(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return CommonResult.success();
    }

    /**
     * 此方法直接返回一个树形结构菜单列表
     * @return
     */
    @GetMapping("/all")
    public CommonResult getAll() {
        return sysMenuService.getAllMenu();
    }

    @PostMapping("/update")
    public CommonResult update(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return CommonResult.success();
    }

    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Integer id) {
        return sysMenuService.deleteMenuById(id);
    }

    @GetMapping("/{id}")
    public CommonResult getOne(@PathVariable Integer id) {
        SysMenu sysMenu = sysMenuService.getById(id);
        return CommonResult.success(sysMenu);
    }
}

