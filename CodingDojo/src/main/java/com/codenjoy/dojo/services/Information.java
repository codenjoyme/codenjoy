package com.codenjoy.dojo.services;

/**
 * User: oleksandr.baglai
 * Date: 11/13/12
 * Time: 5:26 AM
 */
public interface Information {
    public static final Information NULL = new NullInformation();

    String getMessage();
}
