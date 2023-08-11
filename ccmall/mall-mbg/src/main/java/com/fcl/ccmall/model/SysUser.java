package com.fcl.ccmall.model;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author fcl
 * @since 2023-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 后台会员id
     */
//    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 部门编号
     */
    private Long deptId;

    /**
     * 岗位编号
     */
    private Long postId;

    /**
     * 描述
     */
    private String description;

    /**
     * 账号状态1：正常0停用
     */
    private Integer status;

    /**
     * 账号创建时间
     */
    private LocalDateTime creatTime;

    /**
     * 账号更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标记0 可用 1已删除
     */
    private Integer deleted;


}
