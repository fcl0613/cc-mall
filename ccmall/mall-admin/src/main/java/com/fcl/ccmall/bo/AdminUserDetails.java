package com.fcl.ccmall.bo;

import com.fcl.ccmall.model.SysResource;
import com.fcl.ccmall.model.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SpringSecurity需要的用户信息封装类
 */
public class AdminUserDetails implements UserDetails {
    private static final long serialVersionUID = 997471759990951828L;
    //后台用户
    private final SysUser sysUser;
    //拥有资源(权限)列表
    private final List<SysResource> resourceList;

    public AdminUserDetails(SysUser sysUser, List<SysResource> resourceList) {
        this.sysUser = sysUser;
        this.resourceList = resourceList;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的角色
        return resourceList.stream()
                .map(role ->new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return sysUser.getStatus().equals(1);
    }
}
