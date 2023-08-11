package com.fcl.ccmall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.dao.AdvertiseDao;
import com.fcl.ccmall.entity.DO.GetADListDO;
import com.fcl.ccmall.entity.dto.BatchRemoveDTO;
import com.fcl.ccmall.entity.dto.CommonStatusDTO;
import com.fcl.ccmall.entity.dto.GetADListDTO;
import com.fcl.ccmall.entity.vo.GetADListVO;
import com.fcl.ccmall.mapper.CcAdvertisingMapper;
import com.fcl.ccmall.model.CcAdvertising;
import com.fcl.ccmall.service.CcAdvertisingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-03-14
 */
@Service
public class CcAdvertisingServiceImpl extends ServiceImpl<CcAdvertisingMapper, CcAdvertising> implements CcAdvertisingService {

    @Resource
    private CcAdvertisingMapper ccAdvertisingMapper;

    @Resource
    private AdvertiseDao advertiseDao;

    @Override
    public CommonResult getADList(GetADListDTO getADListDTO) {
        Page<GetADListDO> getADListDOPage = new Page<>(getADListDTO.getPageNum(), getADListDTO.getPageSize());
        Page<GetADListDO> page = advertiseDao.getAdList(getADListDOPage, getADListDTO);
        GetADListVO getADListVO = new GetADListVO();
        getADListVO.setTotal(page.getTotal());
        getADListVO.setList(page.getRecords());
        return CommonResult.success(getADListVO);
    }

    @Override
    public CommonResult create(CcAdvertising ccAdvertising) {
        ccAdvertisingMapper.insert(ccAdvertising);
        return CommonResult.success();
    }

    @Override
    public CommonResult updateAD(CcAdvertising ccAdvertising) {
        ccAdvertisingMapper.updateById(ccAdvertising);
        return CommonResult.success();
    }

    @Override
    public CommonResult updatePublish(CommonStatusDTO commonStatusDTO) {
        ccAdvertisingMapper.update(null, new LambdaUpdateWrapper<CcAdvertising>()
        .eq(CcAdvertising::getId, commonStatusDTO.getId())
        .set(CcAdvertising::getPublishStatus, commonStatusDTO.getStatus()));
        return CommonResult.success();
    }

    @Override
    public CommonResult detail(Integer id) {
        CcAdvertising ccAdvertising = ccAdvertisingMapper.selectById(id);
        return CommonResult.success(ccAdvertising);
    }

    @Override
    public CommonResult deleteAD(BatchRemoveDTO batchRemoveDTO) {
        ccAdvertisingMapper.deleteBatchIds(batchRemoveDTO.getList());
        return CommonResult.success();
    }
}
