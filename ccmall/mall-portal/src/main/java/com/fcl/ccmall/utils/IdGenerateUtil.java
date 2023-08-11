package com.fcl.ccmall.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class IdGenerateUtil {

    public static String getSnowId() {
        Snowflake snowflake = IdUtil.createSnowflake(1, 8);
        return snowflake.nextIdStr();
    }
}
