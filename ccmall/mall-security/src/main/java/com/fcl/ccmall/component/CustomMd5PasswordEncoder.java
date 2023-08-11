package com.fcl.ccmall.component;

import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 我们自定义选择加密密码的方式
 * 这里就选择hutool工具自带的md5加密方式，如果想要其他的加密方式 自行替换即可
 */
public class CustomMd5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return DigestUtil.md5Hex(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(DigestUtil.md5Hex(charSequence.toString()));
    }
}
