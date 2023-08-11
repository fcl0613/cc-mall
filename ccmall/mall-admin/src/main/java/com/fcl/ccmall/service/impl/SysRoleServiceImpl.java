package com.fcl.ccmall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.entity.dto.ConferredMenuDTO;
import com.fcl.ccmall.mapper.SysMenuMapper;
import com.fcl.ccmall.mapper.SysRoleMapper;
import com.fcl.ccmall.mapper.SysRoleMenuMapper;
import com.fcl.ccmall.model.SysMenu;
import com.fcl.ccmall.model.SysRole;
import com.fcl.ccmall.model.SysRoleMenu;
import com.fcl.ccmall.service.SysRoleService;
import com.fcl.ccmall.utils.MenuTreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-01-10
 */
@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public CommonResult getList(PageParam pageParam, String keyword) {
        Page<SysRole> rolePage = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isEmpty(keyword)) {
            queryWrapper.like("role_name", keyword);
        }
        queryWrapper.eq("deleted", 0);
        Page<SysRole> sysRolePage = sysRoleMapper.selectPage(rolePage, queryWrapper);
        return CommonResult.success(sysRolePage);
    }

    @Override
    public CommonResult deleteByIds(List<Integer> ids) {
        sysRoleMapper.deleteByIds(ids);
        return CommonResult.success();
    }

    @Override
    public CommonResult getConferredMenu(Integer roleId) {
        // 获取当前角色的权限集合
        Set<Integer> menuIdForRole = sysRoleMenuMapper.getMenuIdForRole(roleId);
        // 获取所有权限
        List<SysMenu> sysMenuList =
                sysMenuMapper.selectList(
                        new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getIsDeleted, 0));
        // 这里如果角色之前没有被赋予权限那么就直接返回
        if (menuIdForRole.isEmpty()) {
            List<SysMenu> sysMenus = MenuTreeUtil.buildTree(sysMenuList);
            // 需要将
            return CommonResult.success(sysMenus);
        }
        // 两者进行对比
        for (SysMenu sysMenu : sysMenuList) {
            if (menuIdForRole.contains(sysMenu.getId())) {
                sysMenu.setSelected(true);
            }else {
                sysMenu.setSelected(false);
            }
        }
        // 返回树形列表
        List<SysMenu> sysMenus = MenuTreeUtil.buildTree(sysMenuList);
        return CommonResult.success(sysMenus);
    }

    @Override
    public CommonResult conferredMenu(ConferredMenuDTO conferredMenuDTO) {
        // 删除角色之前的所有权限
        log.info("删除角色之前的所有权限");
        sysRoleMenuMapper.delete(
                new LambdaQueryWrapper<SysRoleMenu>()
                        .eq(SysRoleMenu::getRoleId, conferredMenuDTO.getRoleId()));
        // 添加新的权限
        log.info("添加新的权限");
        if (conferredMenuDTO.getMenuIdList().isEmpty()) {
            // 防止添加权限的列表id为空时sql拼接错误
            return CommonResult.success();
        }
        sysRoleMenuMapper.conferredMenu(conferredMenuDTO.getRoleId(), conferredMenuDTO.getMenuIdList());
        return CommonResult.success();
    }
}
