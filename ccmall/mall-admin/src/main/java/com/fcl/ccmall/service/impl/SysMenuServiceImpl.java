package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.mapper.SysMenuMapper;
import com.fcl.ccmall.model.SysMenu;
import com.fcl.ccmall.service.SysMenuService;
import com.fcl.ccmall.utils.MenuTreeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-01-15
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public CommonResult getAllMenu() {
        List<SysMenu> list = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getIsDeleted, 0));
        List<SysMenu> sysMenus = MenuTreeUtil.buildTree(list);
        return CommonResult.success(sysMenus);
    }

    @Override
    public CommonResult deleteMenuById(Integer id) {
        Integer count =
                sysMenuMapper.selectCount(new LambdaQueryWrapper<SysMenu>()
                        .eq(SysMenu::getParentId, id));
        if (count > 0) {
            return CommonResult.failed("请先删除子节点");
        }
        this.removeById(id);
        return CommonResult.success();
    }
}
