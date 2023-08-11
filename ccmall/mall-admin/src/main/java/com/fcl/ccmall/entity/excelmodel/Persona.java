package com.fcl.ccmall.entity.excelmodel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Persona {
    @ExcelProperty("手机号")
    private String phone;
    @ExcelProperty("性别")
    private String gender;
    @ExcelProperty("年龄")
    private Integer age;
    @ExcelProperty("职业")
    private String career;
    @ExcelProperty("收入")
    private BigDecimal income;
    @ExcelProperty("是否有车")
    private String hasCar;
    @ExcelProperty("是否有房")
    private String hasHome;
    @ExcelProperty("教育水平")
    private String educationalLevel;
    @ExcelProperty("来源地")
    private String countryOfOrigin;
}
