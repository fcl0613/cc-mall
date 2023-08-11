package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.dao.SysLoginLogDao;
import com.fcl.ccmall.entity.DO.LoginLogDo;
import com.fcl.ccmall.entity.dto.QueryLoginLogDTO;
import com.fcl.ccmall.mapper.SysLoginLogMapper;
import com.fcl.ccmall.model.SysLoginLog;
import com.fcl.ccmall.service.SysLoginLogService;
import com.fcl.ccmall.entity.vo.LoginLogVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-02-06
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Resource
    private SysLoginLogDao sysLoginLogDao;

    @Override
    public CommonResult getList(QueryLoginLogDTO queryLoginLogDTO) {
        Page<LoginLogDo> page = new Page<>(queryLoginLogDTO.getPageNum(), queryLoginLogDTO.getPageSize());
        Page<LoginLogDo> loginLogVoPage =
                sysLoginLogDao.getLogList(page,
                        queryLoginLogDTO.getUsername(),
                        queryLoginLogDTO.getStartTime(),
                        queryLoginLogDTO.getEndTime());
        LoginLogVo loginLogVo = new LoginLogVo();
        loginLogVo.setSize(loginLogVoPage.getTotal());
        loginLogVo.setList(loginLogVoPage.getRecords());
        return CommonResult.success(loginLogVo);
    }
}
