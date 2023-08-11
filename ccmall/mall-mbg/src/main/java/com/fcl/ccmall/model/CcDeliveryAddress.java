package com.fcl.ccmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author fcl
 * @since 2023-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CcDeliveryAddress implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer customerId;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 县
     */
    private String county;

    /**
     * 详细地址
     */
    private String fullAddress;

    /**
     * 是否为默认地址0是1不是
     */
    private Integer defaultFlag;

    /**
     * 收货人
     */
    private String deliveryName;

    /**
     * 联系方式
     */
    private String phone;

    /**
     *  配合vant地址选择器的字段
     */
    private String areaCode;
}
