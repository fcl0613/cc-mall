package com.fcl.ccmall.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.CouponGetTypeEnum;
import com.fcl.ccmall.common.enums.CouponUseStatusEnum;
import com.fcl.ccmall.entity.DO.GetCouponHistoryListDO;
import com.fcl.ccmall.entity.DO.GetCouponListDO;
import com.fcl.ccmall.entity.dto.GetCouponHistoryListDTO;
import com.fcl.ccmall.entity.vo.GetCouponHistoryListVO;
import com.fcl.ccmall.mapper.CcCouponHistoryMapper;
import com.fcl.ccmall.model.CcCouponHistory;
import com.fcl.ccmall.service.CcCouponHistoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-03-29
 */
@Service
public class CcCouponHistoryServiceImpl extends ServiceImpl<CcCouponHistoryMapper, CcCouponHistory> implements CcCouponHistoryService {

    private final String TIME_FORMAT = "yyyy-MM-dd";

    @Override
    public CommonResult getCouponHistoryList(GetCouponHistoryListDTO dto) {
        Page<CcCouponHistory> historyPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<CcCouponHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CcCouponHistory::getCouponId, dto.getCouponId());
        if (!StrUtil.isBlank(dto.getOrderId())) {
            queryWrapper.eq(CcCouponHistory::getOrderId, dto.getOrderId());
        }
        if (!Objects.isNull(dto.getUseStatus())) {
            queryWrapper.eq(CcCouponHistory::getUseStatus, dto.getUseStatus());
        }
        Page<CcCouponHistory> page = this.page(historyPage, queryWrapper);
        ArrayList<GetCouponHistoryListDO> couponHistoryListDOS = new ArrayList<>();
        for (CcCouponHistory record : page.getRecords()) {
            GetCouponHistoryListDO historyListDO = new GetCouponHistoryListDO();
            historyListDO.setCurStatus(CouponUseStatusEnum.getDes(record.getUseStatus()));
            historyListDO.setGetTime(DateUtil.format(record.getGetTime(), TIME_FORMAT));
            historyListDO.setGetType(CouponGetTypeEnum.getDes(record.getGetType()));
            historyListDO.setOrderId(record.getOrderId());
            historyListDO.setUsername(record.getUsername());
            historyListDO.setUseTime(DateUtil.format(record.getGetTime(), TIME_FORMAT));
            couponHistoryListDOS.add(historyListDO);
        }
        GetCouponHistoryListVO vo = new GetCouponHistoryListVO();
        vo.setList(couponHistoryListDOS);
        vo.setTotal(page.getTotal());
        return CommonResult.success(vo);
    }
}
