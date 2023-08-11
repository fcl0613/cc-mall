package com.fcl.ccmall.mapper;

import com.fcl.ccmall.model.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fcl
 * @since 2023-01-08
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    void deleteByIds(List<Integer> ids);
}
