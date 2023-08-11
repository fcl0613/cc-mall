package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.entity.dto.ConferredMenuDTO;
import com.fcl.ccmall.model.SysRole;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-01-10
 */
public interface SysRoleService extends IService<SysRole> {
    CommonResult getList(PageParam pageParam, String keyword);
    CommonResult deleteByIds(List<Integer> ids);
    CommonResult getConferredMenu(Integer roleId);
    CommonResult conferredMenu(ConferredMenuDTO conferredMenuDTO);
}
