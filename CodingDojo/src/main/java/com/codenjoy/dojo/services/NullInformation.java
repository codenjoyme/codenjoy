package com.codenjoy.dojo.services;

import org.apache.commons.lang.StringUtils;

/**
 * User: sanja
 * Date: 27.12.13
 * Time: 22:32
 */
public class NullInformation implements Information {

    NullInformation() {
        // do nothing
    }

    @Override
    public String getMessage() {
        return StringUtils.EMPTY;
    }
}
