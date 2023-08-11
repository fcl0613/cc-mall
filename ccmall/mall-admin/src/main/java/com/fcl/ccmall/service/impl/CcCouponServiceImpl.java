package com.fcl.ccmall.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.CouponTypeEnum;
import com.fcl.ccmall.common.enums.CouponUesTypeEnum;
import com.fcl.ccmall.common.utils.TimeUtil;
import com.fcl.ccmall.entity.DO.GetCouponListDO;
import com.fcl.ccmall.entity.dto.BatchRemoveDTO;
import com.fcl.ccmall.entity.dto.CreateCouponDTO;
import com.fcl.ccmall.entity.dto.GetCouponListDTO;
import com.fcl.ccmall.entity.dto.UpdateCouponDTO;
import com.fcl.ccmall.entity.vo.CouponInfoVO;
import com.fcl.ccmall.entity.vo.GetCouponDetailVO;
import com.fcl.ccmall.entity.vo.GetCouponListVO;
import com.fcl.ccmall.mapper.CcCouponMapper;
import com.fcl.ccmall.model.CcCoupon;
import com.fcl.ccmall.model.CcCouponProductCategoryRelation;
import com.fcl.ccmall.model.CcCouponProductRelation;
import com.fcl.ccmall.service.CcCouponProductCategoryRelationService;
import com.fcl.ccmall.service.CcCouponProductRelationService;
import com.fcl.ccmall.service.CcCouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-03-28
 */
@Service
public class CcCouponServiceImpl extends ServiceImpl<CcCouponMapper, CcCoupon> implements CcCouponService {

    private final String FORMAT_TIME = "yyyy-MM-dd";

    @Resource
    private CcCouponProductCategoryRelationService couponProductCategoryRelationService;

    @Resource
    private CcCouponProductRelationService couponProductRelationService;

    @Override
    public CommonResult createCoupon(CreateCouponDTO createCouponDTO) {
        CcCoupon ccCoupon = generateCoupon(createCouponDTO);
        this.save(ccCoupon);
        if(CouponUesTypeEnum.ASSIGNED_CATEGORY.getType().equals(createCouponDTO.getUseType())) {
            for (CcCouponProductCategoryRelation relation : createCouponDTO.getCategoryRelationList()) {
                relation.setCouponId(ccCoupon.getId());
            }
            couponProductCategoryRelationService.saveBatch(createCouponDTO.getCategoryRelationList());
        }
        if (CouponUesTypeEnum.ASSIGNED_PRODUCT.getType().equals(createCouponDTO.getUseType())) {
            for (CcCouponProductRelation productRelation : createCouponDTO.getProductRelationList()) {
                productRelation.setCouponId(ccCoupon.getId());
            }
            couponProductRelationService.saveBatch(createCouponDTO.getProductRelationList());
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult updateCoupon(UpdateCouponDTO updateCouponDTO) {
        CcCoupon ccCoupon = generateCoupon(updateCouponDTO);
        ccCoupon.setId(updateCouponDTO.getId());
        this.updateById(ccCoupon);
        if(CouponUesTypeEnum.ASSIGNED_CATEGORY.getType().equals(updateCouponDTO.getUseType())) {
            // 这里需要删除之前的关系
            couponProductCategoryRelationService.remove(new LambdaUpdateWrapper<CcCouponProductCategoryRelation>()
            .eq(CcCouponProductCategoryRelation::getCouponId, updateCouponDTO.getId()));
            for (CcCouponProductCategoryRelation relation : updateCouponDTO.getCategoryRelationList()) {
                relation.setCouponId(ccCoupon.getId());
            }
            couponProductCategoryRelationService.saveBatch(updateCouponDTO.getCategoryRelationList());
        }
        if (CouponUesTypeEnum.ASSIGNED_PRODUCT.getType().equals(updateCouponDTO.getUseType())) {
            // 这里需要删除之前的关系
            couponProductRelationService.remove(new LambdaUpdateWrapper<CcCouponProductRelation>()
            .eq(CcCouponProductRelation::getCouponId, updateCouponDTO.getId()));
            for (CcCouponProductRelation productRelation : updateCouponDTO.getProductRelationList()) {
                productRelation.setCouponId(ccCoupon.getId());
            }
            couponProductRelationService.saveBatch(updateCouponDTO.getProductRelationList());
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult deleteCoupon(BatchRemoveDTO batchRemoveDTO) {
        this.removeByIds(batchRemoveDTO.getList());
        // 同时删除和分类或者商品的关系
        couponProductCategoryRelationService.remove(new LambdaUpdateWrapper<CcCouponProductCategoryRelation>()
        .in(CcCouponProductCategoryRelation::getCouponId, batchRemoveDTO.getList()));
        couponProductRelationService.remove(new LambdaUpdateWrapper<CcCouponProductRelation>()
        .in(CcCouponProductRelation::getCouponId, batchRemoveDTO.getList()));
        return CommonResult.success();
    }

    @Override
    public CommonResult getCouponList(GetCouponListDTO getCouponListDTO) {
        LambdaQueryWrapper<CcCoupon> queryWrapper = new LambdaQueryWrapper<>();
        Page<CcCoupon> couponPage = new Page<>(getCouponListDTO.getPageNum(), getCouponListDTO.getPageSize());
        if (!StrUtil.isBlank(getCouponListDTO.getName())) {
            queryWrapper.like(CcCoupon::getName, getCouponListDTO.getName());
        }
        if (!Objects.isNull(getCouponListDTO.getType())) {
            queryWrapper.eq(CcCoupon::getType, getCouponListDTO.getType());
        }
        queryWrapper.orderByDesc(CcCoupon::getId);
        Page<CcCoupon> page = this.page(couponPage, queryWrapper);
        List<CcCoupon> records = page.getRecords();
        ArrayList<GetCouponListDO> couponListDOS = new ArrayList<>();
        for (CcCoupon record : records) {
            GetCouponListDO couponListDO = new GetCouponListDO();
            couponListDO.setAmount(record.getAmount());
            couponListDO.setEndTime(DateUtil.format(record.getEndTime(), FORMAT_TIME));
            couponListDO.setId(record.getId());
            couponListDO.setMinPoint(record.getMinPoint());
            couponListDO.setName(record.getName());
            couponListDO.setStartTime(DateUtil.format(record.getStartTime(), FORMAT_TIME));
            couponListDO.setType(CouponTypeEnum.getDes(record.getType()));
            couponListDO.setUseType(CouponUesTypeEnum.getDes(record.getUseType()));
            if (TimeUtil.afterDate(DateUtil.format(record.getEndTime(), FORMAT_TIME))) {
                couponListDO.setStatus("已过期");
            }else {
                couponListDO.setStatus("未过期");
            }
            couponListDOS.add(couponListDO);
        }
        GetCouponListVO couponListVO = new GetCouponListVO();
        couponListVO.setList(couponListDOS);
        couponListVO.setTotal(page.getTotal());
        return CommonResult.success(couponListVO);
    }

    @Override
    public CommonResult getCouponInfo(Integer id) {
        CcCoupon coupon = this.getById(id);
        CouponInfoVO couponInfoVO = new CouponInfoVO();
        couponInfoVO.setAmount(coupon.getAmount());
        couponInfoVO.setCouponCount(coupon.getCouponCount());
        couponInfoVO.setEndTime(coupon.getEndTime());
        couponInfoVO.setId(coupon.getId());
        couponInfoVO.setMinPoint(coupon.getMinPoint());
        couponInfoVO.setName(coupon.getName());
        couponInfoVO.setNote(coupon.getNote());
        couponInfoVO.setPerLimit(coupon.getPerLimit());
        couponInfoVO.setStartTime(coupon.getStartTime());
        couponInfoVO.setType(coupon.getType());
        couponInfoVO.setUseType(coupon.getUseType());
        if (CouponUesTypeEnum.ASSIGNED_CATEGORY.getType().equals(coupon.getUseType())) {
            // 找到对应的分类数据
            List<CcCouponProductCategoryRelation> list = couponProductCategoryRelationService.list(new LambdaQueryWrapper<CcCouponProductCategoryRelation>()
                    .eq(CcCouponProductCategoryRelation::getCouponId, coupon.getId()));
            couponInfoVO.setProductCategoryRelationList(list);
        }
        if (CouponUesTypeEnum.ASSIGNED_PRODUCT.getType().equals(coupon.getUseType())) {
            // 找到对应的商品数据
            List<CcCouponProductRelation> list = couponProductRelationService.list(new LambdaQueryWrapper<CcCouponProductRelation>()
                    .eq(CcCouponProductRelation::getCouponId, coupon.getId()));
            couponInfoVO.setProductRelationList(list);
        }
        return CommonResult.success(couponInfoVO);
    }

    @Override
    public CommonResult getCouponDetail(Integer id) {
        CcCoupon coupon = this.getById(id);
        GetCouponDetailVO couponDetailVO = new GetCouponDetailVO();
        couponDetailVO.setAmount(coupon.getAmount());
        couponDetailVO.setCouponCount(coupon.getCouponCount());
        String startTime = DateUtil.format(coupon.getStartTime(), FORMAT_TIME);
        String endTime = DateUtil.format(coupon.getEndTime(), FORMAT_TIME);
        couponDetailVO.setEffectiveTime(startTime + "至" + endTime);
        couponDetailVO.setMinPoint(coupon.getMinPoint());
        couponDetailVO.setName(coupon.getName());
        couponDetailVO.setReceiveCount(coupon.getReceiveCount());
        if (TimeUtil.afterDate(DateUtil.format(coupon.getEndTime(), FORMAT_TIME))) {
            couponDetailVO.setStatus("已过期");
        }else {
            couponDetailVO.setStatus("未过期");
        }
        couponDetailVO.setType(CouponTypeEnum.getDes(coupon.getType()));
        couponDetailVO.setUnReceiveCount(coupon.getCouponCount() - coupon.getReceiveCount());
        couponDetailVO.setUseCount(coupon.getUseCount());
        couponDetailVO.setUnUseCount(coupon.getReceiveCount() - coupon.getUseCount());
        couponDetailVO.setUseType(CouponUesTypeEnum.getDes(coupon.getUseType()));
        return CommonResult.success(couponDetailVO);
    }

    private CcCoupon generateCoupon(CreateCouponDTO createCouponDTO) {
        CcCoupon ccCoupon = new CcCoupon();
        ccCoupon.setAmount(createCouponDTO.getAmount());
        ccCoupon.setCouponCount(createCouponDTO.getCouponCount());
        ccCoupon.setEndTime(createCouponDTO.getEndTime());
        ccCoupon.setMinPoint(createCouponDTO.getMinPoint());
        ccCoupon.setName(createCouponDTO.getName());
        ccCoupon.setNote(createCouponDTO.getNote());
        ccCoupon.setPerLimit(createCouponDTO.getPerLimit());
        ccCoupon.setReceiveCount(0);
        ccCoupon.setStartTime(createCouponDTO.getStartTime());
        ccCoupon.setType(createCouponDTO.getType());
        ccCoupon.setUseType(createCouponDTO.getUseType());
        return ccCoupon;
    }
}
