package com.fcl.ccmall.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.entity.PageParam;
import com.fcl.ccmall.common.enums.GenderEnums;
import com.fcl.ccmall.common.exception.Asserts;
import com.fcl.ccmall.dao.CustomerDao;
import com.fcl.ccmall.entity.DO.GetCustomerListDO;
import com.fcl.ccmall.entity.dto.AddCustomerDTO;
import com.fcl.ccmall.entity.dto.DeleteCustomerDTO;
import com.fcl.ccmall.entity.dto.UpdateCustomerDTO;
import com.fcl.ccmall.entity.vo.GetCustomerListVO;
import com.fcl.ccmall.mapper.CustomerMapper;
import com.fcl.ccmall.model.Customer;
import com.fcl.ccmall.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-02-26
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    private final String DEFAULT_PASSWORD = "123456";
    private final String DEFAULT_AVATAR = "https://fclmall-oss.oss-cn-hangzhou.aliyuncs.com/salt_fish.gif";

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private CustomerDao customerDao;

    @Override
    public CommonResult getCustomerList(PageParam pageParam, String keyword) {
        Page<GetCustomerListDO> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        Map<Integer, String> genderMap = GenderEnums.getGenderMap();
        Page<GetCustomerListDO> customerDO = customerDao.getCustomerDO(page, keyword, genderMap);
        GetCustomerListVO getCustomerListVO = new GetCustomerListVO();
        getCustomerListVO.setList(customerDO.getRecords());
        getCustomerListVO.setTotal(customerDO.getTotal());
        return CommonResult.success(getCustomerListVO);
    }

    @Override
    public CommonResult create(AddCustomerDTO addCustomerDTO) {
        String username = addCustomerDTO.getUsername();
        List<Customer> customers = customerMapper.selectList(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getUsername, username));
        if (customers.size() > 0) Asserts.fail("当前用户名已注册，请更换后重试");
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(DigestUtil.md5Hex(DEFAULT_PASSWORD));
        customer.setPhone(addCustomerDTO.getPhone());
        customer.setAvatar(DEFAULT_AVATAR);
        customer.setGender(addCustomerDTO.getGender());
        customer.setNickName(addCustomerDTO.getNickName());
        customerMapper.insert(customer);
        return CommonResult.success();
    }

    @Override
    public CommonResult updateCustomer(UpdateCustomerDTO updateCustomerDTO) {
        Customer customer = new Customer();
        customer.setId(updateCustomerDTO.getId());
        customer.setNickName(updateCustomerDTO.getNickName());
        customer.setGender(updateCustomerDTO.getGender());
        customer.setPhone(updateCustomerDTO.getPhone());
        customerMapper.updateById(customer);
        return CommonResult.success();
    }

    @Override
    public CommonResult deleteCustomer(DeleteCustomerDTO deleteCustomerDTO) {
        customerMapper.deleteBatchIds(deleteCustomerDTO.getList());
        return CommonResult.success();
    }

    @Override
    public CommonResult customerDetail(Integer id) {
        Customer customer = customerMapper.selectById(id);
        return CommonResult.success(customer);
    }
}
