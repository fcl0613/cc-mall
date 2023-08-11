package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.LoginDTO;
import com.fcl.ccmall.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 后台用户管理Controller
 */

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Resource
    private AdminService adminService;

    @PostMapping("/login")
    public CommonResult login (@RequestBody LoginDTO loginDTO) {
        log.info("管理员登录");
//        Map<String, Object> map = new HashMap<>();
//        map.put("token","admin");
        return adminService.Login(loginDTO);
    }

    @GetMapping("/info")
    public CommonResult info(HttpServletRequest request) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("roles","[admin]");
//        map.put("name","admin");
//        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
//        return CommonResult.success(map);
        return adminService.getAdminInfo(request);
    }

    @PostMapping("/logout")
    public CommonResult logout() {
        return CommonResult.success();
    }
}
