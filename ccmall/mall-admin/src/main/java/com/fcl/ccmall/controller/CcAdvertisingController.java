package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.BatchRemoveDTO;
import com.fcl.ccmall.entity.dto.CommonStatusDTO;
import com.fcl.ccmall.entity.dto.GetADListDTO;
import com.fcl.ccmall.model.CcAdvertising;
import com.fcl.ccmall.service.CcAdvertisingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/advertising")
public class CcAdvertisingController {

    @Resource
    private CcAdvertisingService ccAdvertisingService;

    @PostMapping("/list")
    public CommonResult getADList(@RequestBody GetADListDTO getADListDTO) {
        return ccAdvertisingService.getADList(getADListDTO);
    }

    @PostMapping("/create")
    public CommonResult create(@RequestBody CcAdvertising ccAdvertising) {
        return ccAdvertisingService.create(ccAdvertising);
    }

    @PostMapping("/update")
    public CommonResult update(@RequestBody CcAdvertising ccAdvertising) {
        return ccAdvertisingService.updateAD(ccAdvertising);
    }

    @GetMapping("/detail/{id}")
    public CommonResult detail(@PathVariable Integer id) {
        return ccAdvertisingService.detail(id);
    }

    @PostMapping("/update/publish")
    public CommonResult updatePublishStatus(@RequestBody CommonStatusDTO commonStatusDTO) {
        return ccAdvertisingService.updatePublish(commonStatusDTO);
    }

    @PostMapping("/remove")
    public CommonResult remove(@RequestBody BatchRemoveDTO batchRemoveDTO) {
        return ccAdvertisingService.deleteAD(batchRemoveDTO);
    }

}

