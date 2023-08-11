package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.exception.Asserts;
import com.fcl.ccmall.entity.VO.VantAddressInfoVO;
import com.fcl.ccmall.entity.VO.VantAddressVO;
import com.fcl.ccmall.enums.DefaultAddressFlagEnum;
import com.fcl.ccmall.mapper.CcDeliveryAddressMapper;
import com.fcl.ccmall.model.CcDeliveryAddress;
import com.fcl.ccmall.service.CcDeliveryAddressService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-03-27
 */
@Service
public class CcDeliveryAddressServiceImpl extends ServiceImpl<CcDeliveryAddressMapper, CcDeliveryAddress> implements CcDeliveryAddressService {

    @Resource
    private CcDeliveryAddressMapper deliveryAddressMapper;

    @Resource
    private HttpServletRequest request;

    @Resource
    private JwtTokenUtils jwtTokenUtils;


    @Override
    public CommonResult create(CcDeliveryAddress deliveryAddress) {
        Integer customerId = getCustomerId();
        Integer count = deliveryAddressMapper.selectCount(new LambdaQueryWrapper<CcDeliveryAddress>()
                .eq(CcDeliveryAddress::getCustomerId, customerId));
        if (count > 5) {
            Asserts.fail("当前账号的收货地址数量已达上限");
        }
        deliveryAddress.setCustomerId(customerId);
        if (DefaultAddressFlagEnum.DEFAULT.getCode().equals(deliveryAddress.getDefaultFlag())) {
            deliveryAddressMapper.update(null, new LambdaUpdateWrapper<CcDeliveryAddress>()
            .eq(CcDeliveryAddress::getCustomerId, customerId)
            .set(CcDeliveryAddress::getDefaultFlag, DefaultAddressFlagEnum.COMMON.getCode()));
        }
        deliveryAddressMapper.insert(deliveryAddress);
        return CommonResult.success();
    }

    @Override
    public CommonResult getAddressList() {
        List<CcDeliveryAddress> ccDeliveryAddresses = deliveryAddressMapper.selectList(new LambdaQueryWrapper<CcDeliveryAddress>()
                .eq(CcDeliveryAddress::getCustomerId, getCustomerId())
                .orderByAsc(CcDeliveryAddress::getDefaultFlag));
        ArrayList<VantAddressVO> vantAddressVOS = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (CcDeliveryAddress ccDeliveryAddress : ccDeliveryAddresses) {
            VantAddressVO addressVo = new VantAddressVO();
            addressVo.setId(ccDeliveryAddress.getId());
            addressVo.setName(ccDeliveryAddress.getDeliveryName());
            addressVo.setTel(ccDeliveryAddress.getPhone());
            sb.setLength(0);
            sb.append(ccDeliveryAddress.getProvince());
            sb.append(ccDeliveryAddress.getCity());
            sb.append(ccDeliveryAddress.getCounty());
            sb.append(ccDeliveryAddress.getFullAddress());
            addressVo.setAddress(sb.toString());
            if (DefaultAddressFlagEnum.DEFAULT.getCode().equals(ccDeliveryAddress.getDefaultFlag())) {
                addressVo.setIsDefault(true);
            }
            vantAddressVOS.add(addressVo);
        }
        return CommonResult.success(vantAddressVOS);
    }

    @Override
    public CommonResult deleteAddress(Integer id) {
        this.removeById(id);
        return CommonResult.success();
    }

    @Override
    public CommonResult setDefault(Integer id) {
        deliveryAddressMapper.update(null, new LambdaUpdateWrapper<CcDeliveryAddress>()
        .eq(CcDeliveryAddress::getCustomerId, getCustomerId())
        .set(CcDeliveryAddress::getDefaultFlag, DefaultAddressFlagEnum.COMMON.getCode()));
        deliveryAddressMapper.update(null, new LambdaUpdateWrapper<CcDeliveryAddress>()
        .eq(CcDeliveryAddress::getId, id)
        .set(CcDeliveryAddress::getDefaultFlag, DefaultAddressFlagEnum.DEFAULT.getCode()));
        return CommonResult.success();
    }

    @Override
    public CommonResult updateAddress(CcDeliveryAddress deliveryAddress) {
        deliveryAddressMapper.updateById(deliveryAddress);
        if (DefaultAddressFlagEnum.DEFAULT.getCode().equals(deliveryAddress.getDefaultFlag())) {
            this.setDefault(deliveryAddress.getId());
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult getAddressInfo(Integer id) {
        CcDeliveryAddress address = deliveryAddressMapper.selectById(id);
        VantAddressInfoVO addressInfoVO = new VantAddressInfoVO();
        addressInfoVO.setAddressDetail(address.getFullAddress());
        addressInfoVO.setAreaCode(address.getAreaCode());
        addressInfoVO.setCity(address.getCity());
        addressInfoVO.setCounty(address.getCounty());
        addressInfoVO.setId(address.getId());
        addressInfoVO.setIsDefault(DefaultAddressFlagEnum.DEFAULT.getCode().equals(address.getDefaultFlag()));
        addressInfoVO.setName(address.getDeliveryName());
        addressInfoVO.setTel(address.getPhone());
        addressInfoVO.setProvince(address.getProvince());
        return CommonResult.success(addressInfoVO);
    }

    private Integer getCustomerId() {
       return jwtTokenUtils.getUserId(request.getHeader("token"));
    }
}
