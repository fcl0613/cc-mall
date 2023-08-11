package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.exception.Asserts;
import com.fcl.ccmall.dao.CartDao;
import com.fcl.ccmall.entity.DO.CartListDO;
import com.fcl.ccmall.entity.DTO.CartItemDTO;
import com.fcl.ccmall.entity.VO.CartListVO;
import com.fcl.ccmall.mapper.CcCartMapper;
import com.fcl.ccmall.mapper.ProductMapper;
import com.fcl.ccmall.model.CcCart;
import com.fcl.ccmall.model.Product;
import com.fcl.ccmall.service.CcCartService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-03-24
 */
@Service
public class CcCartServiceImpl extends ServiceImpl<CcCartMapper, CcCart> implements CcCartService {

    private final String GAP = ",";

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CcCartMapper cartMapper;

    @Resource
    private CartDao cartDao;

    @Resource
    private HttpServletRequest request;

    @Resource
    private JwtTokenUtils jwtTokenUtils;


    @Override
    public CommonResult addCart(CartItemDTO cartItemDTO) {
        Integer customerId = getCustomerId();
        Integer count = cartMapper.selectCount(new LambdaQueryWrapper<CcCart>()
                .eq(CcCart::getProductId, cartItemDTO.getProductId())
                .eq(CcCart::getCustomerId, customerId));
        if (count > 0) {
            Asserts.fail("当前商品已在购物车中");
        }
        Product product = productMapper.selectById(cartItemDTO.getProductId());
        CcCart cart = new CcCart();
        cart.setCustomerId(customerId);
        cart.setProductAmount(cartItemDTO.getProductCount());
        cart.setProductCover(product.getProductCover().split(GAP)[0]);
        cart.setProductId(cartItemDTO.getProductId());
        cart.setProductName(product.getProductName());
        cart.setProductPrice(product.getPrice());
        cart.setProductCategoryId(product.getCategoryId());
        cartMapper.insert(cart);
        return CommonResult.success();
    }

    @Override
    public CommonResult delete(Integer id) {
        cartMapper.deleteById(id);
        return CommonResult.success();
    }

    @Override
    public CommonResult plusCount(Integer id) {
        cartDao.plusCount(id);
        return CommonResult.success();
    }

    @Override
    public CommonResult minusCount(Integer id) {
        cartDao.minusCount(id);
        return CommonResult.success();
    }

    @Override
    public CommonResult getCartList(Long pageNum, Long pageSize) {
        Page<CartListDO> listDOPage = new Page<>(pageNum, pageSize);
        Page<CartListDO> page = cartDao.getCartList(listDOPage, getCustomerId());
        List<CartListDO> list = page.getRecords();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartListDO cartListDO : list) {
            BigDecimal price = cartListDO.getPrice();
            Integer productAmount = cartListDO.getProductAmount();
            totalPrice = totalPrice.add(price.multiply(new BigDecimal(productAmount)));
        }
        CartListVO cartListVO = new CartListVO();
        cartListVO.setList(list);
        cartListVO.setTotal(page.getTotal());
        cartListVO.setTotalPrice(totalPrice);
        return CommonResult.success(cartListVO);
    }

    private Integer getCustomerId() {
        String token = request.getHeader("token");
        return jwtTokenUtils.getUserId(token);
    }
}
