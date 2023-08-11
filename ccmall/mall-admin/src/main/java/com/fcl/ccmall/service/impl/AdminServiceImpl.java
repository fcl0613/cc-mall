package com.fcl.ccmall.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fcl.ccmall.bo.AdminUserDetails;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.UserFlagEnum;
import com.fcl.ccmall.common.exception.Asserts;
import com.fcl.ccmall.common.utils.RequestUtil;
import com.fcl.ccmall.entity.dto.LoginDTO;
import com.fcl.ccmall.mapper.SysLoginLogMapper;
import com.fcl.ccmall.mapper.SysMenuMapper;
import com.fcl.ccmall.mapper.SysUserMapper;
import com.fcl.ccmall.model.SysLoginLog;
import com.fcl.ccmall.model.SysMenu;
import com.fcl.ccmall.model.SysResource;
import com.fcl.ccmall.model.SysUser;
import com.fcl.ccmall.service.AdminService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import com.fcl.ccmall.utils.MenuTreeUtil;
import com.fcl.ccmall.utils.RouterUtil;
import com.fcl.ccmall.entity.vo.AdminUserVo;
import com.fcl.ccmall.entity.vo.LoginVo;
import com.fcl.ccmall.entity.vo.RouterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Value("${MANAGE_TOKEN_REDIS_PRE}")
    private String MANAGE_TOKEN_REDIS_PRE;
    @Value("${MANAGE_PERMISSIONS_REDIS_PRE}")
    private String MANAGE_PERMISSIONS_REDIS_PRE;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private SysLoginLogMapper sysLoginLogMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public CommonResult Login(LoginDTO loginDTO) {
        LoginVo loginVo = new LoginVo();
        String s = (String) redisTemplate.opsForValue().get(MANAGE_TOKEN_REDIS_PRE + loginDTO.getUsername());
        // 如果redis中存在则直接返回token
        if (!StrUtil.isEmpty(s)) {
            loginVo.setToken(s);
            // 这里也需要加入登录日志
            insertLoginLog(loginDTO.getUsername());
            return CommonResult.success(loginVo);
        }
        List<SysUser> sysUsers =
                sysUserMapper.selectList(
                        new LambdaQueryWrapper<SysUser>()
                                .eq(SysUser::getUserName, loginDTO.getUsername())
                                .eq(SysUser::getDeleted, 0));
        if (Objects.isNull(sysUsers) || sysUsers.size() == 0) {
            Asserts.fail("账号不存在");
        }
        if (sysUsers.size() > 1) {
            Asserts.fail("账号不唯一，请联系系统管理员排查");
        }
        SysUser sysUser = sysUsers.get(0);
        String password = sysUser.getPassword();
        if (!password.equals(DigestUtil.md5Hex(loginDTO.getPassword()))) {
            Asserts.fail("密码错误");
        }
        if (sysUser.getStatus() == 0) {
            Asserts.fail("该账号已被停用");
        }
        String token = jwtTokenUtils.createToken(sysUser.getId(),
                sysUser.getUserName(), UserFlagEnum.MANAGER.getDescription());
        log.info("用户名{}生成的token为{}", loginDTO.getUsername(),token);
        // 将token存入redis中过期时间为7天
        redisTemplate.opsForValue().set(MANAGE_TOKEN_REDIS_PRE + loginDTO.getUsername(), token, 7, TimeUnit.DAYS);
        loginVo.setToken(token);
        insertLoginLog(loginDTO.getUsername());
        return CommonResult.success(loginVo);
    }

    @Override
    public CommonResult getAdminInfo(HttpServletRequest request) {
        // 获取用户token
        String token = request.getHeader("token");
        // 从token中获取用户信息
        Integer userId = jwtTokenUtils.getUserId(token);
        // 根据用户id查询用户信息
        SysUser sysUser = sysUserMapper.selectById(userId);
        // 获取用户的菜单列表
        List<SysMenu> userMenuList = getUserMenuListByUserId(userId);
        // 构建用户菜单列表树形结构
        List<SysMenu> menuTreeList = MenuTreeUtil.buildTree(userMenuList);
        // 构建用户前端路由结构
        List<RouterVo> routerVos = RouterUtil.buildRouters(menuTreeList);
        // 获取用户的按钮权限
        List<String> userPermsList = getUserPermsList(userMenuList);
        AdminUserVo adminUserVo = new AdminUserVo();
        adminUserVo.setName(sysUser.getName());
        // 暂时先写死 后期使用oss
        adminUserVo.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        adminUserVo.setButtons(userPermsList);
        adminUserVo.setRouters(routerVos);
        return CommonResult.success(adminUserVo);
    }

    /**
     * 添加登录记录
     * @param username
     */
    private void insertLoginLog(String username) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setUsername(username);
        sysLoginLog.setCreateTime(LocalDateTime.now());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        sysLoginLog.setIpAddr(RequestUtil.getRequestIp(request));
        sysLoginLog.setUserAgent(RequestUtil.getBrowserName(request));
        sysLoginLogMapper.insert(sysLoginLog);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUser admin = getAdminByUsername(username);
        if (admin != null) {
            // 获取用户的权限
            List<SysMenu> sysMenuList = sysMenuMapper.selectUserMenuListByUserId(admin.getId());
            ArrayList<SysResource> sysResources = new ArrayList<>();
            for (SysMenu sysMenu : sysMenuList) {
                if (!StrUtil.isBlank(sysMenu.getPerms())) {
                    SysResource sysResource = new SysResource();
                    sysResource.setName(sysMenu.getPerms());
                    sysResources.add(sysResource);
                }
            }
            // 把AdminUserDetails对象序列化存入redis中
            AdminUserDetails adminUserDetails = new AdminUserDetails(admin, sysResources);
            redisTemplate.opsForValue().set(MANAGE_PERMISSIONS_REDIS_PRE + username,
                    adminUserDetails, 7, TimeUnit.DAYS);
            log.info("将用户数据存入到redis中");
            return adminUserDetails;
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    private SysUser getAdminByUsername(String username) {
        List<SysUser> sysUsers =
                sysUserMapper.selectList(
                        new LambdaQueryWrapper<SysUser>()
                                .eq(SysUser::getUserName, username)
                                .eq(SysUser::getDeleted, 0));
        if (Objects.isNull(sysUsers) || sysUsers.size() == 0) {
            Asserts.fail("账号不存在");
        }
        if (sysUsers.size() > 1) {
            Asserts.fail("账号不唯一，请联系系统管理员排查");
        }
        return sysUsers.get(0);
    }

    /**
     * 获取用户的按钮权限
     * @param userMenuTreeList
     * @return
     */
    private List<String> getUserPermsList(List<SysMenu> userMenuTreeList) {
        List<String> permissionList = new ArrayList<>();
        for (SysMenu sysMenu : userMenuTreeList) {
            if (sysMenu.getType() == 2) {
                permissionList.add(sysMenu.getPerms());
            }
        }
        return permissionList;
    }

    /**
     * 获取用户的菜单树形结构
     * @param userId
     * @return
     */
    private List<SysMenu> getUserMenuListByUserId(Integer userId) {
        List<SysMenu> sysMenuList = null;
        // 约定id为1是超级管理员 拥有全部权限
        if (userId == 1) {
            sysMenuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1)
                    .eq(SysMenu::getIsDeleted, 0)
                    .orderByAsc(SysMenu::getSortValue));
        }else {
            sysMenuList = sysMenuMapper.selectUserMenuListByUserId(userId);
        }
        return sysMenuList;
    }
}
