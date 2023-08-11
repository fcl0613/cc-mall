package com.fcl.ccmall.common.utils;

import cn.hutool.core.util.RandomUtil;

/**
 * 生成随机验证码
 */

public class CodeUtil {
    public static String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
           sb.append(RandomUtil.randomInt(10));
        }
        return sb.toString();
    }
}
