package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.entity.dto.ConferredMenuDTO;
import com.fcl.ccmall.model.SysRole;
import com.fcl.ccmall.service.SysRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-01-10
 */
@RestController
@RequestMapping("/sysrole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 系统角色添加
     * @param sysRole
     * @return
     */
    @PostMapping("/create")
    public CommonResult create (@RequestBody SysRole sysRole) {
        sysRoleService.save(sysRole);
        return CommonResult.success();
    }

    /**
     * 查询用户分页及搜索
     * @param pageParam
     * @param keyword
     * @return
     */
    @GetMapping("/list")
    public CommonResult getList(PageParam pageParam,
                                @RequestParam(value = "keyword", required = false) String keyword) {
        return sysRoleService.getList(pageParam, keyword);
    }

    /**
     * 根据编码获取角色详情 由于之前在查找的时候没有把有删除标记的用户查找出来
     * 因此这里不需要考虑查找有删除标记的用户
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public CommonResult getDetail(@PathVariable Integer id) {
        SysRole sysRole = sysRoleService.getById(id);
        return CommonResult.success(sysRole);
    }

    /**
     * 更新角色接口
     * @param sysRole
     * @return
     */
    @PostMapping("/update")
    public CommonResult update(@RequestBody SysRole sysRole) {
        sysRoleService.updateById(sysRole);
        return CommonResult.success();
    }

    /**
     * 删除系统角色 同时实现批量删除
     * 批量删除与单个删除公用一个接口
     * 删除是逻辑删除即实际sql是更新
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public CommonResult delete(@RequestBody List<Integer> ids) {
        return sysRoleService.deleteByIds(ids);
    }

    /**
     * 为角色赋予权限
     * @return
     */
    @PostMapping("/conferredMenu")
    public CommonResult conferredMenu(@RequestBody ConferredMenuDTO conferredMenuDTO) {
        return sysRoleService.conferredMenu(conferredMenuDTO);
    }

    /**
     * 获取当前的角色的权限 并返回树形列表
     * @param roleId
     * @return
     */
    @GetMapping("/getConferredMenu/{roleId}")
    public CommonResult getConferredMenu(@PathVariable Integer roleId) {
        return sysRoleService.getConferredMenu(roleId);
    }
}

