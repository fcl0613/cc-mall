package com.fcl.ccmall.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.common.exception.ApiException;
import com.fcl.ccmall.entity.dto.AssignRoleDTO;
import com.fcl.ccmall.mapper.SysRoleMapper;
import com.fcl.ccmall.mapper.SysUserMapper;
import com.fcl.ccmall.mapper.SysUserRoleMapper;
import com.fcl.ccmall.model.SysRole;
import com.fcl.ccmall.model.SysUser;
import com.fcl.ccmall.model.SysUserRole;
import com.fcl.ccmall.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-01-08
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    // 添加用户的密码暂时先这样吧 还有其他一些初始化的数据 后面再完善吧
    // 密码初始化想个身份证右联系
    private final String initialPassword = "123456";
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public CommonResult create(SysUser sysUser) {
        sysUser.setPassword(DigestUtil.md5Hex(initialPassword));
        try {
            sysUserMapper.insert(sysUser);
        } catch (Exception e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new ApiException("该账号已存在，请更换后重试");
            }
        }
        return CommonResult.success(null);
    }

    @Override
    public CommonResult getUserList(PageParam pageParam, String keyword) {
        Page<SysUser> sysUserPage = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isEmpty(keyword)) {
            queryWrapper.like("user_name", keyword);
        }
        queryWrapper.eq("deleted", 0);
        IPage<SysUser> userPage = sysUserMapper.selectPage(sysUserPage, queryWrapper);
        return CommonResult.success(userPage);
    }

    @Override
    public CommonResult deleteByIds(List<Integer> ids) {
        sysUserMapper.deleteByIds(ids);
        return CommonResult.success();
    }

    @Override
    public CommonResult getRole(Integer id) {
        // 获取所有未删除角色的信息
        List<SysRole> sysRoles = sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getDeleted, 0));
        // 获取当前用户所赋予的角色id
        List<Integer> roleIds = sysUserRoleMapper.getRoleIdByUserId(id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("sysRoles", sysRoles);
        map.put("roleIds", roleIds);
        return CommonResult.success(map);
    }

    @Override
    public CommonResult assignRole(AssignRoleDTO assignRoleDTO) {
        // 删除当前用户所分配的角色
        sysUserRoleMapper
                .delete(new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, assignRoleDTO.getUserId()));
        // 分配用户角色
        sysUserRoleMapper.assignRole(assignRoleDTO.getRoleIdList(), assignRoleDTO.getUserId());
        return CommonResult.success();
    }
}
