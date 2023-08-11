package com.fcl.ccmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fcl.ccmall.model.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    List<Integer> getRoleIdByUserId(@Param("userId") Integer id);
    void assignRole(List<Integer> list, @Param("userId") Integer userId);
}
