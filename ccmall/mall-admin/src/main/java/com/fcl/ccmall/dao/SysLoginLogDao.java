package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.entity.DO.LoginLogDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysLoginLogDao {
    Page<LoginLogDo> getLogList(@Param("page") Page<LoginLogDo> page,
                                @Param("username") String username,
                                @Param("startTime") String startTime,
                                @Param("endTime") String endTime);
}
