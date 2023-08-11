package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.entity.dto.AssignRoleDTO;
import com.fcl.ccmall.model.SysUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-01-08
 */
public interface SysUserService extends IService<SysUser> {
    CommonResult create(SysUser sysUser);
    CommonResult getUserList(PageParam pageParam, String keyword);
    CommonResult deleteByIds(List<Integer> ids);
    CommonResult getRole(Integer id);
    CommonResult assignRole(AssignRoleDTO assignRoleDTO);
}
