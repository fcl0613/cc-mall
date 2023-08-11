package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.entity.dto.AddCustomerDTO;
import com.fcl.ccmall.entity.dto.DeleteCustomerDTO;
import com.fcl.ccmall.entity.dto.UpdateCustomerDTO;
import com.fcl.ccmall.service.CustomerService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-02-26
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @GetMapping("/list")
    public CommonResult getCustomer(PageParam pageParam, String keyword) {
       return customerService.getCustomerList(pageParam, keyword);
    }

    @PostMapping("/create")
    public CommonResult createCustomer(@RequestBody AddCustomerDTO addCustomerDTO) {
        return customerService.create(addCustomerDTO);
    }

    @PostMapping("/update")
    public CommonResult updateCustomer(@RequestBody UpdateCustomerDTO updateCustomerDTO) {
        return customerService.updateCustomer(updateCustomerDTO);
    }

    @GetMapping("/detail/{id}")
    public CommonResult getCustomerDetail(@PathVariable Integer id) {
        return customerService.customerDetail(id);
    }

    @PostMapping("/remove")
    public CommonResult removeCustomer(@RequestBody DeleteCustomerDTO deleteCustomerDTO) {
        return customerService.deleteCustomer(deleteCustomerDTO);
    }
}

