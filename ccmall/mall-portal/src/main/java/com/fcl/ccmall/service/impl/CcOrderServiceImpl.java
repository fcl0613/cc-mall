package com.fcl.ccmall.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.CouponUesTypeEnum;
import com.fcl.ccmall.common.enums.CouponUseStatusEnum;
import com.fcl.ccmall.common.enums.OrderConfirmStatusEnum;
import com.fcl.ccmall.common.enums.OrderDeleteStatusEnum;
import com.fcl.ccmall.common.enums.OrderStatusEnum;
import com.fcl.ccmall.common.enums.OrderTypeEnum;
import com.fcl.ccmall.common.exception.Asserts;
import com.fcl.ccmall.common.utils.TimeUtil;
import com.fcl.ccmall.dao.CcOrderDao;
import com.fcl.ccmall.dao.CouponDao;
import com.fcl.ccmall.dao.CustomerDao;
import com.fcl.ccmall.entity.DO.OrderListDO;
import com.fcl.ccmall.entity.DTO.AfterSaleApplyDTO;
import com.fcl.ccmall.entity.DTO.CreateOrderDTO;
import com.fcl.ccmall.entity.DTO.GenerateConfirmOrderDTO;
import com.fcl.ccmall.entity.DTO.GetOrderListDTO;
import com.fcl.ccmall.entity.DTO.OrderDirectBuyDTO;
import com.fcl.ccmall.entity.VO.ConfirmDirectOrderVO;
import com.fcl.ccmall.entity.VO.ConfirmOrderVO;
import com.fcl.ccmall.entity.VO.CreateOrderVO;
import com.fcl.ccmall.entity.VO.OrderListVO;
import com.fcl.ccmall.mapper.CcCartMapper;
import com.fcl.ccmall.mapper.CcCouponHistoryMapper;
import com.fcl.ccmall.mapper.CcCouponMapper;
import com.fcl.ccmall.mapper.CcCouponProductCategoryRelationMapper;
import com.fcl.ccmall.mapper.CcCouponProductRelationMapper;
import com.fcl.ccmall.mapper.CcDeliveryAddressMapper;
import com.fcl.ccmall.mapper.OrderDetailMapper;
import com.fcl.ccmall.mapper.OrderMasterMapper;
import com.fcl.ccmall.mapper.ProductMapper;
import com.fcl.ccmall.model.CcCart;
import com.fcl.ccmall.model.CcCoupon;
import com.fcl.ccmall.model.CcCouponHistory;
import com.fcl.ccmall.model.CcCouponProductCategoryRelation;
import com.fcl.ccmall.model.CcCouponProductRelation;
import com.fcl.ccmall.model.CcDeliveryAddress;
import com.fcl.ccmall.model.OrderDetail;
import com.fcl.ccmall.model.OrderMaster;
import com.fcl.ccmall.model.Product;
import com.fcl.ccmall.model.ProductToBeComment;
import com.fcl.ccmall.service.CcOrderService;
import com.fcl.ccmall.service.OrderDetailService;
import com.fcl.ccmall.service.ProductService;
import com.fcl.ccmall.service.ProductToBeCommentService;
import com.fcl.ccmall.utils.IdGenerateUtil;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CcOrderServiceImpl extends ServiceImpl<OrderMasterMapper, OrderMaster>
        implements CcOrderService {

    private final String DATE_FORMAT = "yyyy-MM-dd";
    private final String GAP = ",";

    @Resource
    private CcOrderDao orderDao;

    @Resource
    private CcCartMapper cartMapper;

    @Resource
    private CcCouponMapper couponMapper;

    @Resource
    private HttpServletRequest request;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private CcCouponHistoryMapper couponHistoryMapper;

    @Resource
    private CouponDao couponDao;

    @Resource
    private CcCouponProductCategoryRelationMapper couponProductCategoryRelationMapper;

    @Resource
    private CcCouponProductRelationMapper couponProductRelationMapper;

    @Resource
    private CcDeliveryAddressMapper deliveryAddressMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private OrderMasterMapper orderMasterMapper;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private ProductToBeCommentService productToBeCommentService;

    @Resource
    private CustomerDao customerDao;

    @Resource
    private ProductService productService;

    @Override
    public CommonResult generateConfirmOrder(GenerateConfirmOrderDTO generateConfirmOrderDTO) {
        ConfirmOrderVO confirmOrderVO = new ConfirmOrderVO();
        Integer customerId = getCustomerId();
        // 获取购物车里面的内容
        List<CcCart> carts =
                cartMapper.selectList(new LambdaQueryWrapper<CcCart>()
                        .in(CcCart::getId, generateConfirmOrderDTO.getCartIds()));
        confirmOrderVO.setCartList(carts);
        // 计算商品价格  总价
        BigDecimal totalPrice = computedPrice(carts);
        confirmOrderVO.setTotalPrice(totalPrice);
        Set<Integer> productCategorySet = new HashSet<>();
        Set<Integer> productSet = new HashSet<>();
        for (CcCart cart : carts) {
            productCategorySet.add(cart.getProductCategoryId());
            productSet.add(cart.getProductId());
        }
        // 获取用户收货地址信息
//        List<CcDeliveryAddress> deliveryAddresses = deliveryAddressMapper.selectList(new LambdaQueryWrapper<CcDeliveryAddress>()
//                .eq(CcDeliveryAddress::getCustomerId, customerId));
//        confirmOrderVO.setDeliveryAddressList(deliveryAddresses);
        // 获取用户领取优惠券的信息
        List<CcCoupon> couponList = couponDao.getCouponList(customerId, CouponUseStatusEnum.UN_USED.getCode());
        if (couponList.size() == 0) {
            // 没有优惠券直接返回
            return CommonResult.success(confirmOrderVO);
        }
        // 无法使用优惠券列表
        ArrayList<CcCoupon> noUsedCoupons = new ArrayList<>();
        // 以下步骤都是couponList有数据的情况下
        // 比较优惠券的信息 主要是时间是否过期
        Iterator<CcCoupon> couponIterator = couponList.iterator();
        while (couponIterator.hasNext()) {
            CcCoupon coupon = couponIterator.next();
            if (TimeUtil.afterDate(DateUtil.format(coupon.getEndTime(), DATE_FORMAT))) {
                // 这里说明用户的优惠券是过期了
                // 更新并移除该优惠券 这里使用迭代器
                couponHistoryMapper
                        .update(null, new LambdaUpdateWrapper<CcCouponHistory>()
                                .eq(CcCouponHistory::getCouponId, coupon.getId())
                                .eq(CcCouponHistory::getMemberId, customerId)
                                .set(CcCouponHistory::getUseStatus, CouponUseStatusEnum.EXPIRED.getCode()));
                coupon.setNoUsedReason("已过期");
                noUsedCoupons.add(coupon);
                couponIterator.remove();
                continue;
            }
            if (coupon.getUseType().equals(CouponUesTypeEnum.ASSIGNED_CATEGORY.getType())) {
                // 如果是指定分类
                // 获取指定分类编号
                List<CcCouponProductCategoryRelation> couponProductCategoryRelations =
                        couponProductCategoryRelationMapper
                                .selectList(new LambdaQueryWrapper<CcCouponProductCategoryRelation>()
                                        .eq(CcCouponProductCategoryRelation::getCouponId, coupon.getId()));
                if (!couponProductCategoryRelations.isEmpty() && couponProductCategoryRelations.size() > 0) {
                    Set<Integer> temp = new HashSet<>();
                    for (CcCouponProductCategoryRelation relation : couponProductCategoryRelations) {
                        temp.add(relation.getProductCategoryId());
                    }
                    if (Collections.disjoint(productCategorySet, temp)) {
                        coupon.setNoUsedReason("无指定商品类别");
                        noUsedCoupons.add(coupon);
                        couponIterator.remove();
                    }else {
                        // 判断确认单的价格
                        if (coupon.getMinPoint().compareTo(totalPrice) > -1) {
                            // 优惠券的使用门槛比总价多
                            coupon.setNoUsedReason("未达使用门槛");
                            noUsedCoupons.add(coupon);
                            couponIterator.remove();
                        }
                    }
//                    if (!productCategorySet.contains(temp)) {
//
//                    }
                }
            }
            if (coupon.getUseType().equals(CouponUesTypeEnum.ASSIGNED_PRODUCT.getType())) {
                // 指定商品
                // 获取指定商品编号
                List<CcCouponProductRelation> couponProductRelations =
                        couponProductRelationMapper.selectList(new LambdaQueryWrapper<CcCouponProductRelation>()
                                .eq(CcCouponProductRelation::getCouponId, coupon.getId()));
                if (!couponProductRelations.isEmpty() && couponProductRelations.size() > 0) {
                    CcCouponProductRelation relation = couponProductRelations.get(0);
                    if (!productSet.contains(relation.getProductId())) {
                        coupon.setNoUsedReason("无指定商品");
                        noUsedCoupons.add(coupon);
                        couponIterator.remove();
                        continue;
                    }else {
                        // 判断购物车商品数量和价格的小计是否大于优惠券的使用门槛
                        for (CcCart cart : carts) {
                            if (cart.getProductId().equals(relation.getProductId())) {
                                // 计算小计
                                BigDecimal subtotal = cart.getProductPrice().multiply(new BigDecimal(cart.getProductAmount()));
                                if (coupon.getMinPoint().compareTo(subtotal) > -1) {
                                    // 优惠券的使用门槛比总价多
                                    coupon.setNoUsedReason("未达使用门槛");
                                    noUsedCoupons.add(coupon);
                                    couponIterator.remove();
                                }
                            }
                        }
                    }
                }
            }
        }
        confirmOrderVO.setHasCoupon(couponList);
        confirmOrderVO.setNoCoupon(noUsedCoupons);
        return CommonResult.success(confirmOrderVO);
    }

    @Override
    public CommonResult createOrder(CreateOrderDTO createOrderDTO) {
        // 获取购物车内的商品
        List<CcCart> cartList = cartMapper.selectList(new LambdaQueryWrapper<CcCart>()
                .in(CcCart::getId, createOrderDTO.getCartIds()));
        List<Integer> productIds = new ArrayList<>();
        List<Integer> cartIds = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        for (CcCart cart : cartList) {
            productIds.add(cart.getProductId());
            cartIds.add(cart.getId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductId(cart.getProductId());
            orderDetail.setProductName(cart.getProductName());
            orderDetail.setProductCnt(cart.getProductAmount());
            orderDetail.setProductPrice(cart.getProductPrice());
            orderDetail.setCreateTime(localDateTime);
            orderDetail.setProductCover(cart.getProductCover());
            orderDetails.add(orderDetail);
        }
        // 获取商品信息
        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .in(Product::getId, productIds));
        // 对比库存信息 并扣减库存
        for (Product product : productList) {
            for (CcCart cart : cartList) {
                if (product.getId().equals(cart.getProductId())) {
                    if (product.getStock() < cart.getProductAmount()) {
                        Asserts.fail("商品" + product.getProductName() + "库存不足");
                    }
                    product.setStock(product.getStock() - cart.getProductAmount());
                }
            }
        }
        productService.saveBatch(productList);
        // 获取收货地址信息
        CcDeliveryAddress deliveryAddress = deliveryAddressMapper.selectById(createOrderDTO.getAddressId());
        // 获取优惠券信息
        CcCoupon coupon = null;
        OrderMaster orderMaster = new OrderMaster();
        String snowId = IdGenerateUtil.getSnowId();
        orderMaster.setOrderId(snowId);
        Integer customerId = getCustomerId();
        orderMaster.setCustomerId(customerId);
        BigDecimal totalPrice = computedPrice(cartList);
        orderMaster.setTotalAmount(totalPrice);
        orderMaster.setPayAmount(totalPrice);
        orderMaster.setCouponAmount(BigDecimal.ZERO);
        if (!Objects.isNull(createOrderDTO.getCouponId())) {
            // 使用优惠券的情况
            coupon = couponMapper.selectById(createOrderDTO.getCouponId());
            if (!Objects.isNull(coupon)) {
                orderMaster.setCouponId(createOrderDTO.getCouponId());
                orderMaster.setPayAmount(totalPrice.subtract(coupon.getAmount()));
                orderMaster.setCouponAmount(coupon.getAmount());
            }
        }
        orderMaster.setCustomerUsername(getCustomerUsername());
        orderMaster.setPayType(createOrderDTO.getPayType());
        orderMaster.setOrderStatus(OrderStatusEnum.OBLIGATION.getCode());
        orderMaster.setIntegration(computedPoint(productList));
        if (OrderTypeEnum.DISTRIBUTION.getCode().equals(createOrderDTO.getOrderType())) {
            orderMaster.setReceiverName(deliveryAddress.getDeliveryName());
            orderMaster.setReceiverPhone(deliveryAddress.getPhone());
            orderMaster.setReceiverProvince(deliveryAddress.getProvince());
            orderMaster.setReceiverCity(deliveryAddress.getCity());
            orderMaster.setReceiverCounty(deliveryAddress.getCounty());
            orderMaster.setReceiverDetailAddress(deliveryAddress.getFullAddress());
        }
        orderMaster.setConfirmStatus(OrderConfirmStatusEnum.UN_CONFIRM.getCode());
        orderMaster.setDeleteStatus(OrderDeleteStatusEnum.UN_DELETED.getCode());
        orderMaster.setOrderTime(localDateTime);
        orderMaster.setOrderType(createOrderDTO.getOrderType());
        // 加入自己的订单表
        orderMasterMapper.insert(orderMaster);
        // 获取数据库自增的id主键
        Integer id = orderMaster.getId();
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setOrderId(id);
        }
        // 批量加入订单详情表
        orderDetailService.saveBatch(orderDetails);
        // 订单生成完毕等待买家进行付款
        // 订单生成完毕后我们需要删除购物车种对应的商品
        cartMapper.delete(new LambdaUpdateWrapper<CcCart>()
        .in(CcCart::getId, cartIds));
        // 如果使用了优惠券 将优惠券设置已使用 因为用户一种优惠券可以领取多张 因此找到列表取第一张
        if (!Objects.isNull(coupon)) {
            List<CcCouponHistory> couponHistories = couponHistoryMapper.selectList(new LambdaQueryWrapper<CcCouponHistory>()
                    .eq(CcCouponHistory::getCouponId, coupon.getId())
                    .eq(CcCouponHistory::getMemberId, customerId));
            if (!couponHistories.isEmpty() && couponHistories.size() > 0) {
                CcCouponHistory couponHistory = couponHistories.get(0);
                couponHistory.setUseStatus(CouponUseStatusEnum.USED.getCode());
                couponHistory.setOrderId(snowId);
                couponHistory.setUseTime(localDateTime);
                couponHistoryMapper.update(couponHistory, null);
            }
            couponDao.addUseCoupon(coupon.getId());
        }
        // 这里我们需要返回 订单编号 订单应付金额 （订单名称，固定配置）
        CreateOrderVO createOrderVO = new CreateOrderVO();
        createOrderVO.setOrderId(id);
        createOrderVO.setPrice(orderMaster.getPayAmount());
        createOrderVO.setName(orderMaster.getCustomerUsername());
        return CommonResult.success(createOrderVO);
    }

    @Override
    public CommonResult confirmOrder(Integer id) {
        OrderMaster orderMaster = orderMasterMapper.selectById(id);
        LocalDateTime now = LocalDateTime.now();
        if (OrderConfirmStatusEnum.UN_CONFIRM.getCode().equals(orderMaster.getConfirmStatus())) {
            orderMasterMapper.update(null, new LambdaUpdateWrapper<OrderMaster>()
            .eq(OrderMaster::getId, id)
            .set(OrderMaster::getConfirmStatus, OrderConfirmStatusEnum.CONFIRM.getCode())
            .set(OrderMaster::getOrderStatus, OrderStatusEnum.FINISHED.getCode())
            .set(OrderMaster::getReceiveTime, now));
            // 更新用户积分
            customerDao.updatePoints(orderMaster.getIntegration(), orderMaster.getCustomerId());
            // 将订单内的商品放入到待评价商品表中
            List<OrderDetail> list = orderDetailService.list(new LambdaQueryWrapper<OrderDetail>()
                    .eq(OrderDetail::getOrderId, id));
            List<ProductToBeComment> commentArrayList = new ArrayList<>();
            for (OrderDetail orderDetail : list) {
                ProductToBeComment productToBeComment = new ProductToBeComment();
                productToBeComment.setCustomerId(getCustomerId());
                productToBeComment.setProductId(orderDetail.getProductId());
                productToBeComment.setProductCount(orderDetail.getProductCnt());
                productToBeComment.setProductCover(orderDetail.getProductCover());
                productToBeComment.setProductName(orderDetail.getProductName());
                productToBeComment.setProductPrice(orderDetail.getProductPrice());
                productToBeComment.setCreateTime(now);
                productToBeComment.setOrderId(id);
                commentArrayList.add(productToBeComment);
            }
            productToBeCommentService.saveBatch(commentArrayList);
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult getOrderList(GetOrderListDTO orderListDTO) {
        Integer customerId = getCustomerId();
        Page<OrderMaster> orderMasterPage = new Page<>(orderListDTO.getPageNum(), orderListDTO.getPageSize());
        LambdaQueryWrapper<OrderMaster> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderMaster::getDeleteStatus, OrderDeleteStatusEnum.UN_DELETED.getCode());
        if (!Objects.isNull(orderListDTO.getOrderStatus())) {
            queryWrapper.eq(OrderMaster::getOrderStatus, orderListDTO.getOrderStatus());
        }
        queryWrapper.eq(OrderMaster::getCustomerId, customerId);
        Page<OrderMaster> page = orderMasterMapper.selectPage(orderMasterPage, queryWrapper);
        List<OrderMaster> records = page.getRecords();
        OrderListVO orderListVO = new OrderListVO();
        if (records.size() == 0) {
            orderListVO.setTotal(0l);
            orderListVO.setOrderList(new ArrayList<OrderListDO>());
            return CommonResult.success(orderListVO);
        }
        List<Integer> orderIds = new ArrayList<>();
        List<OrderListDO> orderListDOS = new ArrayList<>();
        for (OrderMaster record : records) {
            orderIds.add(record.getId());
            OrderListDO orderListDO = new OrderListDO();
            orderListDO.setOrderId(record.getId());
            orderListDO.setOrderSn(record.getOrderId());
            orderListDO.setOrderStatus(record.getOrderStatus());
            orderListDO.setTotalPrice(record.getPayAmount());
            orderListDO.setCouponAmount(record.getCouponAmount());
            orderListDOS.add(orderListDO);
        }
        // 查找订单下的商品
        List<OrderDetail> orderDetails = orderDetailService.list(new LambdaQueryWrapper<OrderDetail>()
                .in(OrderDetail::getOrderId, orderIds));
        for (OrderListDO orderListDO : orderListDOS) {
            Integer orderId = orderListDO.getOrderId();
            List<OrderDetail> orderDetailList = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetails) {
                if (orderId.equals(orderDetail.getOrderId())) {
                    orderDetailList.add(orderDetail);
                }
            }
            orderListDO.setOrderDetailList(orderDetailList);
        }
        orderListVO.setOrderList(orderListDOS);
        orderListVO.setTotal(page.getTotal());
        return CommonResult.success(orderListVO);
    }

    @Override
    public CommonResult deleteOrder(Integer id) {
        orderMasterMapper.update(null, new LambdaUpdateWrapper<OrderMaster>()
        .eq(OrderMaster::getId, id)
        .set(OrderMaster::getDeleteStatus, OrderDeleteStatusEnum.DELETED.getCode()));
        return CommonResult.success();
    }

    @Override
    public CommonResult cancelOrder(Integer id) {
        OrderMaster orderMaster = orderMasterMapper.selectById(id);
        Integer orderStatus = orderMaster.getOrderStatus();
        if (OrderStatusEnum.OBLIGATION.getCode().equals(orderStatus)) {
            // 如果是未付款状态 直接更新
            orderMasterMapper.update(null, new LambdaUpdateWrapper<OrderMaster>()
            .eq(OrderMaster::getId, id)
            .set(OrderMaster::getOrderStatus, OrderStatusEnum.CLOSED.getCode()));
            return CommonResult.success();
        }
        if (OrderStatusEnum.WAITING_FOR_DELIVERY.getCode().equals(orderStatus)) {
            // 如果是待发货状态 调取远程服务 退款成功后取消订单
            // TODO
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult directConfirm(Integer productId, Integer count) {
        ConfirmDirectOrderVO confirmDirectOrderVO = new ConfirmDirectOrderVO();
        // 获取商品信息
        Product product = productMapper.selectById(productId);
        // 总价
        BigDecimal totalPrice = BigDecimal.ZERO;
        totalPrice = totalPrice.add(product.getPrice().multiply(new BigDecimal(count)));
        Integer customerId = getCustomerId();
        List<Product> productList = new ArrayList<>();
        product.setProductCover(product.getProductCover().split(GAP)[0]);
        productList.add(product);
        confirmDirectOrderVO.setProductList(productList);
        confirmDirectOrderVO.setCount(count);
        confirmDirectOrderVO.setTotalPrice(totalPrice);
        // 获取优惠券信息
        // 可用优惠券
        List<CcCoupon> hasCoupon = new ArrayList<>();
        // 不可用优惠券
        List<CcCoupon> noCoupon = new ArrayList<>();
        List<CcCoupon> couponList = couponDao.getCouponList(customerId, CouponUseStatusEnum.UN_USED.getCode());
        if (couponList.size() == 0) {
            confirmDirectOrderVO.setHasCoupon(hasCoupon);
            confirmDirectOrderVO.setNoCoupon(noCoupon);
            return CommonResult.success(confirmDirectOrderVO);
        }
        for (CcCoupon coupon : couponList) {
            if (TimeUtil.afterDate(DateUtil.format(coupon.getEndTime(), DATE_FORMAT))) {
                // 这里优惠券过期
                // 更新
                couponHistoryMapper.update(null, new LambdaUpdateWrapper<CcCouponHistory>()
                .eq(CcCouponHistory::getMemberId, customerId)
                .eq(CcCouponHistory::getCouponId, coupon.getId())
                .set(CcCouponHistory::getUseStatus, CouponUseStatusEnum.EXPIRED.getCode()));
                coupon.setNoUsedReason("已过期");
                noCoupon.add(coupon);
                continue;
            }
            if (CouponUesTypeEnum.ASSIGNED_CATEGORY.getType().equals(coupon.getUseType())) {
                // 指定类型的优惠券
                // 找到对应分类的id
                List<CcCouponProductCategoryRelation> categoryRelations =
                        couponProductCategoryRelationMapper.selectList(
                                new LambdaQueryWrapper<CcCouponProductCategoryRelation>()
                        .eq(CcCouponProductCategoryRelation::getCouponId, coupon.getId()));
                if (categoryRelations.size() > 0) {
                    boolean flag = false;
                    for (CcCouponProductCategoryRelation categoryRelation : categoryRelations) {
                        if (product.getCategoryId().equals(categoryRelation.getProductCategoryId())) {
                            flag = true;
                            if (coupon.getMinPoint().compareTo(totalPrice) < 1) {
                                // 优惠券的使用门槛小于等于总价格
                                hasCoupon.add(coupon);
                            }else {
                                coupon.setNoUsedReason("未到达使用门槛");
                                noCoupon.add(coupon);
                            }
                            break;
                        }
                    }
                    if (!flag) {
                        coupon.setNoUsedReason("无指定商品类别");
                        noCoupon.add(coupon);
                    }
//                    CcCouponProductCategoryRelation productCategoryRelation = categoryRelations.get(0);
//                    if (product.getCategoryId().equals(productCategoryRelation.getProductCategoryId())) {
//                        hasCoupon.add(coupon);
//                    }else {
//                        coupon.setNoUsedReason("无指定商品类别");
//                    }
                }
                continue;
            }
            if (CouponUesTypeEnum.ASSIGNED_PRODUCT.getType().equals(coupon.getUseType())) {
                // 指定商品的优惠券
                List<CcCouponProductRelation> couponProductRelations = couponProductRelationMapper.selectList(new LambdaQueryWrapper<CcCouponProductRelation>()
                        .eq(CcCouponProductRelation::getCouponId, coupon.getId()));
                if (couponProductRelations.size() > 0) {
                    CcCouponProductRelation relation = couponProductRelations.get(0);
                    if (product.getId().equals(relation.getProductId())) {
                        if (coupon.getMinPoint().compareTo(totalPrice) < 1) {
                            // 优惠券的使用门槛小于等于总价格
                            hasCoupon.add(coupon);
                        }else {
                            coupon.setNoUsedReason("未到达使用门槛");
                            noCoupon.add(coupon);
                        }
                    }else {
                        coupon.setNoUsedReason("无指定商品");
                        noCoupon.add(coupon);
                    }
                }
                continue;
            }
        }
        confirmDirectOrderVO.setNoCoupon(noCoupon);
        confirmDirectOrderVO.setHasCoupon(hasCoupon);
        return CommonResult.success(confirmDirectOrderVO);
    }

    @Override
    public CommonResult directBuy(OrderDirectBuyDTO orderDirectBuyDTO) {
        Product product = productMapper.selectById(orderDirectBuyDTO.getProductId());
        LocalDateTime now = LocalDateTime.now();
        Integer customerId = getCustomerId();
        String customerUsername = getCustomerUsername();
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId(IdGenerateUtil.getSnowId());
        orderMaster.setOrderType(orderDirectBuyDTO.getOrderType());
        if (OrderTypeEnum.DISTRIBUTION.getCode().equals(orderDirectBuyDTO.getOrderType())) {
            CcDeliveryAddress address = deliveryAddressMapper.selectById(orderDirectBuyDTO.getAddressId());
            orderMaster.setReceiverDetailAddress(address.getFullAddress());
            orderMaster.setReceiverCounty(address.getCounty());
            orderMaster.setReceiverCity(address.getCity());
            orderMaster.setReceiverProvince(address.getProvince());
            orderMaster.setReceiverPhone(address.getPhone());
            orderMaster.setReceiverName(address.getDeliveryName());
        }
        orderMaster.setOrderTime(now);
        orderMaster.setDeleteStatus(OrderDeleteStatusEnum.UN_DELETED.getCode());
        orderMaster.setConfirmStatus(OrderConfirmStatusEnum.UN_CONFIRM.getCode());
        orderMaster.setIntegration(product.getGiftPoint());
        orderMaster.setOrderStatus(OrderStatusEnum.OBLIGATION.getCode());
        orderMaster.setPayType(orderDirectBuyDTO.getPayType());
        orderMaster.setCustomerUsername(customerUsername);
        orderMaster.setCustomerId(customerId);
        BigDecimal totalPrice = product.getPrice().multiply(new BigDecimal(orderDirectBuyDTO.getCount()));
        orderMaster.setTotalAmount(totalPrice);
        orderMaster.setPayAmount(totalPrice);
        orderMaster.setCouponAmount(BigDecimal.ZERO);
        if (!Objects.isNull(orderDirectBuyDTO.getCouponId())) {
            // 找到优惠券的信息
            CcCoupon coupon = couponMapper.selectById(orderDirectBuyDTO.getCouponId());
            orderMaster.setCouponId(coupon.getId());
            orderMaster.setPayAmount(totalPrice.subtract(coupon.getAmount()));
            orderMaster.setCouponAmount(coupon.getAmount());
        }
        this.save(orderMaster);
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderMaster.getId());
        orderDetail.setProductCover(product.getProductCover().split(GAP)[0]);
        orderDetail.setCreateTime(now);
        orderDetail.setProductPrice(product.getPrice());
        orderDetail.setProductCnt(orderDirectBuyDTO.getCount());
        orderDetail.setProductName(product.getProductName());
        orderDetail.setProductId(product.getId());
        orderDetailService.save(orderDetail);
        // 修改库存
        productMapper.update(null, new LambdaUpdateWrapper<Product>()
        .eq(Product::getId, product.getId())
        .set(Product::getStock, product.getStock() - orderDirectBuyDTO.getCount()));
        // 如果使用优惠券的情况下更新优惠券的使用信息
        if (!Objects.isNull(orderDirectBuyDTO.getCouponId())) {
            // 这里先获取优惠券的列表信息 因为一种优惠券可能领取多次
            List<CcCouponHistory> couponHistories = couponHistoryMapper.selectList(new LambdaQueryWrapper<CcCouponHistory>()
                    .eq(CcCouponHistory::getCouponId, orderDirectBuyDTO.getCouponId())
                    .eq(CcCouponHistory::getMemberId, customerId));
            if (couponHistories.size() > 0) {
                CcCouponHistory couponHistory = couponHistories.get(0);
                couponHistoryMapper.update(null, new LambdaUpdateWrapper<CcCouponHistory>()
                .eq(CcCouponHistory::getId, couponHistory.getId())
                .set(CcCouponHistory::getUseStatus, CouponUseStatusEnum.USED.getCode()));
                couponDao.addUseCoupon(couponHistory.getCouponId());
            }
        }
        // 这里我们需要返回 订单编号 订单应付金额 （订单名称，固定配置）
        CreateOrderVO createOrderVO = new CreateOrderVO();
        createOrderVO.setOrderId(orderMaster.getId());
        createOrderVO.setPrice(orderMaster.getPayAmount());
        createOrderVO.setName(orderMaster.getCustomerUsername());
        return CommonResult.success(createOrderVO);
    }

    private Integer getCustomerId() {
        return jwtTokenUtils.getUserId(request.getHeader("token"));
    }

    private String getCustomerUsername() {
        return jwtTokenUtils.getUsername(request.getHeader("token"));
    }

    private BigDecimal computedPrice(List<CcCart> list) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CcCart cart : list) {
            BigDecimal productPrice = cart.getProductPrice();
            BigDecimal multiply = productPrice.multiply(new BigDecimal(cart.getProductAmount()));
            totalPrice = totalPrice.add(multiply);
        }
        return totalPrice;
    }

    private Integer computedPoint(List<Product> list) {
        int point = 0;
        for (Product product : list) {
            point += product.getGiftPoint();
        }
        return point;
    }
}
