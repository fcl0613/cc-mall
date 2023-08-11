package com.fcl.ccmall.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解用户操作日志
 * 暂时先作用在方法上
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebLog {
    public String description() default "";
}
