package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.BatchRemoveDTO;
import com.fcl.ccmall.entity.dto.CommonStatusDTO;
import com.fcl.ccmall.entity.dto.GetADListDTO;
import com.fcl.ccmall.model.CcAdvertising;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-03-14
 */
public interface CcAdvertisingService extends IService<CcAdvertising> {

    CommonResult getADList(GetADListDTO getADListDTO);

    CommonResult create(CcAdvertising ccAdvertising);

    CommonResult updateAD(CcAdvertising ccAdvertising);

    CommonResult updatePublish(CommonStatusDTO commonStatusDTO);

    CommonResult detail(Integer id);

    CommonResult deleteAD(BatchRemoveDTO batchRemoveDTO);
}
