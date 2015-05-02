package com.codenjoy.dojo.services;

import org.springframework.util.DigestUtils;

public class Hash {
    public static String md5(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
