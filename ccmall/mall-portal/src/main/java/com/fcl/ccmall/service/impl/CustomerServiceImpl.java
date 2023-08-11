package com.fcl.ccmall.service.impl;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.GenderEnums;
import com.fcl.ccmall.common.enums.OrderApplyStatusEnum;
import com.fcl.ccmall.common.enums.OrderStatusEnum;
import com.fcl.ccmall.common.enums.UserFlagEnum;
import com.fcl.ccmall.common.exception.Asserts;
import com.fcl.ccmall.common.utils.CodeUtil;
import com.fcl.ccmall.dao.PorCustomerDao;
import com.fcl.ccmall.entity.DTO.LoginDTO;
import com.fcl.ccmall.entity.DTO.RegisterDTO;
import com.fcl.ccmall.entity.DTO.SendMessageDTO;
import com.fcl.ccmall.entity.VO.CustomerInfoVO;
import com.fcl.ccmall.entity.VO.LoginVO;
import com.fcl.ccmall.entity.VO.MyPageContentVO;
import com.fcl.ccmall.entity.bo.CustomerDetails;
import com.fcl.ccmall.mapper.CustomerMapper;
import com.fcl.ccmall.mapper.OrderMasterMapper;
import com.fcl.ccmall.mapper.OrderReturnApplyMapper;
import com.fcl.ccmall.mapper.ProductToBeCommentMapper;
import com.fcl.ccmall.model.Customer;
import com.fcl.ccmall.model.OrderMaster;
import com.fcl.ccmall.model.OrderReturnApply;
import com.fcl.ccmall.model.ProductToBeComment;
import com.fcl.ccmall.service.CustomerService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Value("${CUSTOMER_TOKEN_REDIS_PRE}")
    private String CUSTOMER_TOKEN_REDIS_PRE;
    @Value("${CUSTOMER_PERMISSIONS_REDIS_PRE}")
    private String CUSTOMER_PERMISSIONS_REDIS_PRE;
    @Value("${SEND_MESSAGE_REDIS_PRE}")
    private String SEND_MESSAGE_REDIS_PRE;
    private final String AVATAR = "https://fclmall-oss.oss-cn-hangzhou.aliyuncs.com/salt_fish.gif";
    private final String BASE_IMG_URL = "https://fclmall-oss.oss-cn-hangzhou.aliyuncs.com/ccmall/image/";

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private PorCustomerDao porCustomerDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private HttpServletRequest request;

    @Resource
    private OrderMasterMapper orderMasterMapper;

    @Resource
    private OrderReturnApplyMapper orderReturnApplyMapper;

    @Resource
    private ProductToBeCommentMapper productToBeCommentMapper;

    @Override
    public CommonResult login(LoginDTO loginDTO) {
        LoginVO loginVO = new LoginVO();
        String s = (String) redisTemplate.opsForValue()
                .get(CUSTOMER_TOKEN_REDIS_PRE + loginDTO.getLoginAccount());
        if (!StrUtil.isEmpty(s)) {
            loginVO.setToken(s);
            return CommonResult.success(loginVO);
        }
        // 这里先暂时只支持用户名登录
        List<Customer> customers = customerMapper.selectList(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getUsername, loginDTO.getLoginAccount()));
        if (Objects.isNull(customers) || customers.size() == 0) {
            Asserts.fail("账号不存在");
        }
        if (customers.size() > 1) {
            Asserts.fail("账号不唯一，请联系客服");
        }
        Customer customer = customers.get(0);
        String password = customer.getPassword();
        if (!password.equals(DigestUtil.md5Hex(loginDTO.getPassword()))) {
            Asserts.fail("密码错误");
        }
        String token = jwtTokenUtils.createToken(customer.getId(),
                customer.getUsername(), UserFlagEnum.CUSTOMER.getDescription());
        log.info("用户名{}生成的token为{}", loginDTO.getLoginAccount(),token);
        // 将token存入redis中过期时间为7天
        redisTemplate.opsForValue().set(CUSTOMER_TOKEN_REDIS_PRE + loginDTO.getLoginAccount(),
                token, 7, TimeUnit.DAYS);
        loginVO.setToken(token);
        return CommonResult.success(loginVO);
    }

    @Override
    public CommonResult getCustomerInfo() {
        Customer customer = customerMapper.selectById(getCustomerId());
        CustomerInfoVO customerInfoVO = new CustomerInfoVO();
        customerInfoVO.setAvatar(customer.getAvatar());
        customerInfoVO.setGender(GenderEnums.getDescribeByType(customer.getGender()));
        customerInfoVO.setNickName(customer.getNickName());
        return CommonResult.success(customerInfoVO);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Customer customer = getCustomerByUsername(username);
        if (null != customer) {
            CustomerDetails customerDetails = new CustomerDetails(customer);
            redisTemplate.opsForValue().set(CUSTOMER_PERMISSIONS_REDIS_PRE + username,
                    customerDetails, 7, TimeUnit.DAYS);
            log.info("将用户权限数据存入到redis中");
            return customerDetails;
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public CommonResult register(RegisterDTO registerDTO) {
        Integer count = customerMapper.selectCount(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getPhone, registerDTO.getPhone()));
        if (count > 0) {
            log.info("该手机号=={}已注册", registerDTO.getPhone());
            Asserts.fail("该手机号已注册");
        }
        // 验证六位验证码是否相同
        if (!checkCode(registerDTO.getPhone(), registerDTO.getCaptcha())) {
            log.info("验证码错误");
            Asserts.fail("验证码错误");
        }
        Customer customer = new Customer();
        Integer maxId = porCustomerDao.getMaxId();
        customer.setUsername("cc" + maxId);
        customer.setGender(GenderEnums.UN_KNOW.getType());
        customer.setNickName("用户_" + maxId);
        customer.setAvatar(AVATAR);
        customer.setPhone(registerDTO.getPhone());
        customer.setPassword(DigestUtil.md5Hex(registerDTO.getPassword()));
        customerMapper.insert(customer);
        return CommonResult.success("注册成功");
    }

    @Override
    public CommonResult sendMessage(SendMessageDTO sendMessageDTO) {
        // 验证手机号是否正确
        if(!PhoneUtil.isMobile(sendMessageDTO.getPhone())) {
            Asserts.fail("手机号格式不正确");
        }
        // 同一个手机号一天之内不能发送注册验证码超过三次
        // 随机生成六位验证码 放入redis中发送短信注册
        String code = CodeUtil.generateCode();
        // key 自动加1
        Long count = stringRedisTemplate.boundValueOps(SEND_MESSAGE_REDIS_PRE + sendMessageDTO.getPhone()).increment(1);
        if (count == 1) {
            // 设置过期时间
            stringRedisTemplate.expire(SEND_MESSAGE_REDIS_PRE + sendMessageDTO.getPhone(), 24, TimeUnit.HOURS);
        }
        if (count > 3) {
            log.info("用户{}发送短信次数已达当日上限", sendMessageDTO.getPhone());
            Asserts.fail("您发送验证码已达当日上线");
        }
        // 发送短信
        boolean flag = sendMessage(sendMessageDTO.getPhone(), code);
        if (!flag) {
            stringRedisTemplate.boundValueOps(SEND_MESSAGE_REDIS_PRE + sendMessageDTO.getPhone()).increment(-1);
            return CommonResult.failed("短信发送失败，请稍后再试");
        }
        stringRedisTemplate.opsForValue().set(SEND_MESSAGE_REDIS_PRE + sendMessageDTO.getPhone(), code, 24, TimeUnit.HOURS);
        log.info("用户{}注册需要的验证码为{}",sendMessageDTO.getPhone(),code);
        return CommonResult.success("发送成功");
    }

    @Override
    public CommonResult getMyPageContent() {
        Integer customerId = getCustomerId();
        Customer customer = customerMapper.selectById(customerId);
        MyPageContentVO myPageContentVO = new MyPageContentVO();
        myPageContentVO.setAvatar(customer.getAvatar());
        myPageContentVO.setNickName(customer.getNickName());
        myPageContentVO.setPoint(customer.getPoints());
        // TODO 这里还有相关订单状态数量统计 等订单模块完成后再写
        Integer pendingPayment =
                orderMasterMapper.selectCount(new LambdaQueryWrapper<OrderMaster>()
                .eq(OrderMaster::getCustomerId, customerId)
                .eq(OrderMaster::getOrderStatus, OrderStatusEnum.OBLIGATION.getCode()));
        Integer waitingForDelivery =
                orderMasterMapper.selectCount(new LambdaQueryWrapper<OrderMaster>()
                        .eq(OrderMaster::getCustomerId, customerId)
                        .eq(OrderMaster::getOrderStatus, OrderStatusEnum.WAITING_FOR_DELIVERY.getCode()));
        Integer shipped = orderMasterMapper.selectCount(new LambdaQueryWrapper<OrderMaster>()
                .eq(OrderMaster::getCustomerId, customerId)
                .eq(OrderMaster::getOrderStatus, OrderStatusEnum.DELIVERED.getCode()));
        Integer waitingComments = productToBeCommentMapper.selectCount(new LambdaQueryWrapper<ProductToBeComment>()
                .eq(ProductToBeComment::getCustomerId, customerId));
        Integer afterSales = orderReturnApplyMapper.selectCount(new LambdaQueryWrapper<OrderReturnApply>()
                .eq(OrderReturnApply::getCustomerId, customerId)
                .eq(OrderReturnApply::getStatus, OrderApplyStatusEnum.AWAIT_HANDLE.getCode()));
        myPageContentVO.setPendingPayment(pendingPayment);
        myPageContentVO.setWaitingForDelivery(waitingForDelivery);
        myPageContentVO.setShipped(shipped);
        myPageContentVO.setWaitingComments(waitingComments);
        myPageContentVO.setAfterSales(afterSales);
        return CommonResult.success(myPageContentVO);
    }

    @Override
    public CommonResult updateAvatar(String avatar) {
        customerMapper.update(null, new LambdaUpdateWrapper<Customer>()
        .eq(Customer::getId, getCustomerId())
        .set(Customer::getAvatar, avatar));
        return CommonResult.success();
    }

    @Override
    public CommonResult updateGender(Integer gender) {
        customerMapper.update(null, new LambdaUpdateWrapper<Customer>()
                .eq(Customer::getId, getCustomerId())
                .set(Customer::getGender, gender));
        return CommonResult.success();
    }

    @Override
    public CommonResult updateNickName(String name) {
        customerMapper.update(null, new LambdaUpdateWrapper<Customer>()
                .eq(Customer::getId, getCustomerId())
                .set(Customer::getNickName, name));
        return CommonResult.success();
    }

    private Customer getCustomerByUsername(String username) {
        List<Customer> customers = customerMapper.selectList(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getUsername, username));
        if (Objects.isNull(customers) || customers.size() == 0) {
            Asserts.fail("账号不存在");
        }
        if (customers.size() > 1) {
            Asserts.fail("账号不唯一，请联系客服");
        }
        return customers.get(0);
    }

    private boolean sendMessage(String phone, String code) {
        //TODO 调用第三方接口发送短信
        return true;
    }

    private boolean checkCode(String phone, String code) {
        String codeRedis = stringRedisTemplate.opsForValue().get(SEND_MESSAGE_REDIS_PRE + phone);
        if (Objects.isNull(codeRedis) || !codeRedis.equals(code)) {
            return false;
        }
        return true;
    }

    private Integer getCustomerId() {
        String token = request.getHeader("token");
        return jwtTokenUtils.getUserId(token);
    }
}
