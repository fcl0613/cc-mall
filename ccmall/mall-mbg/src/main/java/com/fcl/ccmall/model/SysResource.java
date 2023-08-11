package com.fcl.ccmall.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysResource implements Serializable {

    private static final long serialVersionUID = 991625010736888921L;

    private Long id;

    private Date createTime;

    private String name;

    private String url;

    private String description;

    private Long categoryId;
}
