package com.fcl.ccmall.mapper;

import com.fcl.ccmall.model.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fcl
 * @since 2023-01-10
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    void deleteByIds(List<Integer> ids);
}
