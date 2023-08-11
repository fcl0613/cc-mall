package com.fcl.ccmall.controller;


import com.fcl.ccmall.annotation.WebLog;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.entity.dto.AssignRoleDTO;
import com.fcl.ccmall.model.SysUser;
import com.fcl.ccmall.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * <p>
 *  用户的管理的相关操作
 * </p>
 *
 * @author fcl
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/sysuser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 创建新的系统用户
     * @param sysUser
     * @return
     */
    @PostMapping("/create")
    public CommonResult create(@RequestBody SysUser sysUser) {
        return sysUserService.create(sysUser);
    }

    /**
     * 查询用户分页及搜索
     * @param pageParam
     * @param keyword
     * @return
     */
    @GetMapping("/list")
    @WebLog(description = "查询用户分页及搜索")
    public CommonResult getList(PageParam pageParam,
                                @RequestParam(value = "keyword", required = false) String keyword) {
        return sysUserService.getUserList(pageParam,keyword);
    }

    /**
     * 根据用户id获取用户详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public CommonResult<SysUser> getOne(@PathVariable Integer id) {
        return CommonResult.success(sysUserService.getById(id));
    }

    /**
     * 删除系统用户 同时实现批量删除
     * 批量删除与单个删除公用一个接口
     * 删除是逻辑删除即实际sql是更新
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public CommonResult delete(@RequestBody List<Integer> ids) {
        return sysUserService.deleteByIds(ids);
    }

    /**
     * 系统用户更新接口
     * 复用包括更新用户状态主要参数用户id用户状态码
     * @param sysUser
     * @return
     */
    @PostMapping("/update")
    public CommonResult update(@RequestBody SysUser sysUser) {
        sysUserService.updateById(sysUser);
        return CommonResult.success();
    }

    /**
     * 为用户分配角色
     * @return
     */
    @PostMapping("/assignRole")
    public CommonResult assignRole(@RequestBody AssignRoleDTO assignRoleDTO) {
        return sysUserService.assignRole(assignRoleDTO);
    }

    /**
     * 根据用户编码获取用户已分配的角色以及全部的角色
     * @return
     */
    @GetMapping("/getRole/{id}")
    public CommonResult getRole(@PathVariable Integer id) {
        return sysUserService.getRole(id);
    }
}

