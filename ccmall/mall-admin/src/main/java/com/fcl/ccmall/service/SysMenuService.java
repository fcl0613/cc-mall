package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.model.SysMenu;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-01-15
 */
public interface SysMenuService extends IService<SysMenu> {
    CommonResult getAllMenu();
    CommonResult deleteMenuById(Integer id);
}
