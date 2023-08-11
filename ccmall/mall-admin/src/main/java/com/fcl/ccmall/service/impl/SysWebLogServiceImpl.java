package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.dao.SysWebLogDao;
import com.fcl.ccmall.entity.DO.WebLogDo;
import com.fcl.ccmall.entity.dto.QueryWebLogDTO;
import com.fcl.ccmall.entity.vo.WebLogVo;
import com.fcl.ccmall.mapper.SysWebLogMapper;
import com.fcl.ccmall.model.SysWebLog;
import com.fcl.ccmall.service.SysWebLogService;
import org.springframework.scheduling.annotation.Async;
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
public class SysWebLogServiceImpl extends ServiceImpl<SysWebLogMapper, SysWebLog> implements SysWebLogService {

    @Resource
    private SysWebLogDao sysWebLogDao;

    @Override
    @Async
    public void asyncInsert(SysWebLog sysWebLog) {
        this.save(sysWebLog);
    }

    @Override
    public CommonResult getList(QueryWebLogDTO queryWebLogDTO) {
        Page<WebLogDo> page =
                new Page<>(queryWebLogDTO.getPageNum(),
                        queryWebLogDTO.getPageSize());
        Page<WebLogDo> webLogDoPage =
                sysWebLogDao.getLogList(page,
                        queryWebLogDTO.getUsername(),
                        queryWebLogDTO.getStartTime(),
                        queryWebLogDTO.getEndTime());
        WebLogVo webLogVo = new WebLogVo();
        webLogVo.setSize(webLogDoPage.getTotal());
        webLogVo.setList(webLogDoPage.getRecords());
        return CommonResult.success(webLogVo);
    }
}
