package com.codenjoy.dojo.services;

import org.apache.commons.lang.StringUtils;

public class NullInformation implements Information {

    public static final Information INSTANCE = new NullInformation();

    private NullInformation() {
        // do nothing
    }

    @Override
    public String getMessage() {
        return StringUtils.EMPTY;
    }
}
