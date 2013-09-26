package com.codenjoy.dojo.services.settings;

/**
 * User: sanja
 * Date: 26.09.13
 * Time: 8:48
 */
public interface Parameter {
    Object getValue();

    void update(Object value);

    Parameter def(Object value);

    boolean itsMe(String name);
}
