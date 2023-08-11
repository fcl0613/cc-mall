package com.fcl.ccmall.mapper;

import com.fcl.ccmall.model.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fcl
 * @since 2023-01-28
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    Set<Integer> getMenuIdForRole(@Param("roleId") Integer roleId);
    void conferredMenu(@Param("roleId") Integer roleId, List<Integer> list);
}
