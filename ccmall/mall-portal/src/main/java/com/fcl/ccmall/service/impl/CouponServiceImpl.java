package com.fcl.ccmall.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.CouponGetTypeEnum;
import com.fcl.ccmall.common.enums.CouponUesTypeEnum;
import com.fcl.ccmall.common.enums.CouponUseStatusEnum;
import com.fcl.ccmall.common.exception.Asserts;
import com.fcl.ccmall.common.utils.TimeUtil;
import com.fcl.ccmall.dao.CouponDao;
import com.fcl.ccmall.entity.DO.AllCouponDO;
import com.fcl.ccmall.entity.VO.GetAllCouponVO;
import com.fcl.ccmall.mapper.CcCouponHistoryMapper;
import com.fcl.ccmall.mapper.CcCouponMapper;
import com.fcl.ccmall.mapper.CcCouponProductCategoryRelationMapper;
import com.fcl.ccmall.mapper.CcCouponProductRelationMapper;
import com.fcl.ccmall.model.CcCoupon;
import com.fcl.ccmall.model.CcCouponHistory;
import com.fcl.ccmall.model.CcCouponProductCategoryRelation;
import com.fcl.ccmall.model.CcCouponProductRelation;
import com.fcl.ccmall.service.CouponService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CouponServiceImpl implements CouponService {

    private final String FORMAT_TIME = "yyyy-MM-dd";

    @Resource
    private CcCouponMapper couponMapper;

    @Resource
    private CcCouponHistoryMapper couponHistoryMapper;

    @Resource
    private HttpServletRequest request;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private CouponDao couponDao;

    @Resource
    private CcCouponProductCategoryRelationMapper couponProductCategoryRelationMapper;

    @Resource
    private CcCouponProductRelationMapper couponProductRelationMapper;

    @Override
    public CommonResult getAllCoupon() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTime dateTime = DateUtil.offsetDay(DateUtil.date(), -10);
        String format = DateUtil.format(localDateTime, FORMAT_TIME);
        GetAllCouponVO couponVO = new GetAllCouponVO();
        // 这里找近一星期的优惠券
        List<CcCoupon> couponList = couponMapper.selectList(new LambdaQueryWrapper<CcCoupon>()
                .ge(CcCoupon::getStartTime, dateTime));
        // 找用户已获取的优惠券
        Integer customerId = getCustomerId();
        if (couponList.isEmpty() || couponList.size() == 0) {
            return CommonResult.success(couponVO);
        }
        // 未领取
        List<AllCouponDO> unReceivedList = new ArrayList<>();
        // 未使用
        List<AllCouponDO> unUsedList = new ArrayList<>();
        // 已过期
        List<AllCouponDO> exportedList = new ArrayList<>();
        for (CcCoupon coupon : couponList) {
            // 判断当前日期是否在优惠券结束日期之前
            if (TimeUtil.afterDate(DateUtil.format(coupon.getEndTime(), FORMAT_TIME))) {
                // 当前日期在优惠券结束日期之后 直接跳过
                continue;
            }
            Integer count = couponHistoryMapper.selectCount(new LambdaQueryWrapper<CcCouponHistory>()
                    .eq(CcCouponHistory::getCouponId, coupon.getId())
                    .eq(CcCouponHistory::getMemberId, customerId));
            if (count < coupon.getPerLimit()) {
                AllCouponDO allCouponDO = new AllCouponDO();
                allCouponDO.setAmount(coupon.getAmount());
                allCouponDO.setEffectTime(DateUtil.format(coupon.getStartTime(), FORMAT_TIME)
                        + "~" + DateUtil.format(coupon.getEndTime(), FORMAT_TIME));
                allCouponDO.setName(coupon.getName());
                allCouponDO.setId(coupon.getId());
                unReceivedList.add(allCouponDO);
            }
        }
        // 获取用户已获得的未使用优惠券 这里面的有些优惠券可能已经过期了
        List<CcCoupon> customerCoupon =
                couponDao.getCustomerCoupon(format, CouponUseStatusEnum.UN_USED.getCode(), customerId);
        if (!customerCoupon.isEmpty() && customerCoupon.size() > 0) {
            for (CcCoupon coupon : customerCoupon) {
                // 判断当前日期是否在优惠券结束日期之前
                if (TimeUtil.afterDate(DateUtil.format(coupon.getEndTime(), FORMAT_TIME))) {
                    // 当前日期在优惠券结束日期之后 更新用户领取优惠券的信息
                    couponHistoryMapper.update(null, new LambdaUpdateWrapper<CcCouponHistory>()
                    .eq(CcCouponHistory::getCouponId, coupon.getId())
                    .eq(CcCouponHistory::getMemberId, customerId)
                    .set(CcCouponHistory::getUseStatus, CouponUseStatusEnum.EXPIRED.getCode()));
                }else {
                    AllCouponDO allCouponDO = new AllCouponDO();
                    allCouponDO.setAmount(coupon.getAmount());
                    allCouponDO.setEffectTime(DateUtil.format(coupon.getStartTime(), FORMAT_TIME)
                            + "~" + DateUtil.format(coupon.getEndTime(), FORMAT_TIME));
                    allCouponDO.setName(coupon.getName());
                    allCouponDO.setId(coupon.getId());
                    allCouponDO.setUseType(coupon.getUseType());
                    if (CouponUesTypeEnum.ASSIGNED_CATEGORY.getType().equals(coupon.getUseType())) {
                        // 获取指定分类编码
                        List<CcCouponProductCategoryRelation> relations =
                                couponProductCategoryRelationMapper
                                        .selectList(new LambdaQueryWrapper<CcCouponProductCategoryRelation>()
                                .eq(CcCouponProductCategoryRelation::getCouponId, coupon.getId()));
                        allCouponDO.setUseType(CouponUesTypeEnum.ASSIGNED_CATEGORY.getType());
                        if (relations.isEmpty() || relations.size() == 0) {
                            allCouponDO.setCategoryId(0);
                        }else {
                            allCouponDO.setCategoryId(relations.get(0).getProductCategoryId());
                        }
                    }
                    if (CouponUesTypeEnum.ASSIGNED_PRODUCT.getType().equals(coupon.getUseType())) {
                        List<CcCouponProductRelation> relations =
                                couponProductRelationMapper.selectList(new LambdaQueryWrapper<CcCouponProductRelation>()
                                .eq(CcCouponProductRelation::getCouponId, coupon.getId()));
                        allCouponDO.setUseType(CouponUesTypeEnum.ASSIGNED_PRODUCT.getType());
                        if (relations.isEmpty() || relations.size() == 0) {
                            allCouponDO.setProductId(0);
                        }else {
                            allCouponDO.setProductId(relations.get(0).getProductId());
                        }
                    }
                    unUsedList.add(allCouponDO);
                }
            }
        }
        // 获取已过期的优惠券
        List<CcCoupon> customerCoupon1 =
                couponDao.getCustomerCoupon(format, CouponUseStatusEnum.EXPIRED.getCode(), customerId);
        for (CcCoupon coupon : customerCoupon1) {
            AllCouponDO allCouponDO = new AllCouponDO();
            allCouponDO.setAmount(coupon.getAmount());
            allCouponDO.setName(coupon.getName());
            allCouponDO.setEffectTime(DateUtil.format(coupon.getStartTime(), FORMAT_TIME)
                    + "~" + DateUtil.format(coupon.getEndTime(), FORMAT_TIME));
            exportedList.add(allCouponDO);
        }
        Iterator<AllCouponDO> iterator = unReceivedList.iterator();
        Map<Integer, Integer> hashMap = new HashMap<>();
        for (AllCouponDO allCouponDO : unUsedList) {
            if (hashMap.containsKey(allCouponDO.getId())) {
                hashMap.put(allCouponDO.getId(), hashMap.get(allCouponDO.getId()) + 1);
            }else {
                hashMap.put(allCouponDO.getId(), 1);
            }
        }
        while (iterator.hasNext()) {
            Integer couponId = iterator.next().getId();
            if (hashMap.containsKey(couponId)) {
                Integer count = hashMap.get(couponId);
                for (CcCoupon coupon : couponList) {
                    if (coupon.getId().equals(couponId)) {
                        if (count >= coupon.getPerLimit()) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
        couponVO.setUnReceivedList(unReceivedList);
        couponVO.setUnUsedList(unUsedList);
        couponVO.setExportedList(exportedList);
        return CommonResult.success(couponVO);
    }

    @Override
    public CommonResult receiveCoupon(Integer id) {
        CcCoupon coupon = couponMapper.selectById(id);
        Integer customerId = getCustomerId();
        List<CcCouponHistory> couponHistories =
                couponHistoryMapper.selectList(new LambdaQueryWrapper<CcCouponHistory>()
                .eq(CcCouponHistory::getCouponId, coupon.getId())
                .eq(CcCouponHistory::getMemberId, customerId));
        // 每人限领次数
        Integer perLimit = coupon.getPerLimit();
        if (couponHistories.size() >= perLimit || coupon.getCouponCount() <= coupon.getReceiveCount()) {
            Asserts.fail("当前优惠券已达领取上限");
        }
        CcCouponHistory couponHistory = new CcCouponHistory();
        couponHistory.setCouponId(coupon.getId());
        couponHistory.setGetTime(LocalDateTime.now());
        couponHistory.setGetType(CouponGetTypeEnum.SELF.getCode());
        couponHistory.setMemberId(customerId);
        couponHistory.setUsername(getCustomerUsername());
        couponHistory.setUseStatus(CouponUseStatusEnum.UN_USED.getCode());
        couponHistoryMapper.insert(couponHistory);
        // 优惠券领取数量自增1
        couponDao.addReceivedCoupon(coupon.getId());
        return CommonResult.success();
    }

    private Integer getCustomerId() {
        String token = request.getHeader("token");
        return jwtTokenUtils.getUserId(token);
    }

    private String getCustomerUsername() {
        String token = request.getHeader("token");
        return jwtTokenUtils.getUsername(token);
    }
}
