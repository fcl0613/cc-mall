package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.entity.dto.AddCustomerDTO;
import com.fcl.ccmall.entity.dto.DeleteCustomerDTO;
import com.fcl.ccmall.entity.dto.UpdateCustomerDTO;
import com.fcl.ccmall.model.Customer;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-02-26
 */
public interface CustomerService extends IService<Customer> {
    CommonResult getCustomerList(PageParam pageParam, String keyword);
    CommonResult create(AddCustomerDTO addCustomerDTO);
    CommonResult updateCustomer(UpdateCustomerDTO updateCustomerDTO);
    CommonResult deleteCustomer(DeleteCustomerDTO deleteCustomerDTO);
    CommonResult customerDetail(Integer id);
}
