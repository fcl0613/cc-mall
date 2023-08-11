package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.entity.DO.WebLogDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysWebLogDao {
    Page<WebLogDo> getLogList(@Param("page") Page<WebLogDo> page,
                              @Param("username") String username,
                              @Param("startTime") String startTime,
                              @Param("endTime") String endTime);
}
