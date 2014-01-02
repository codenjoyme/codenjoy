package com.codenjoy.dojo.services.settings;

import org.apache.commons.lang.StringUtils;

/**
 * User: sanja
 * Date: 28.12.13
 * Time: 15:03
 */
public class NullParameter<T> implements Parameter<T> {

    NullParameter() {
        // do nothing
    }

    @Override
    public T getValue() {
        return (T)new Object();
    }

    @Override
    public String getName() {
        return StringUtils.EMPTY;
    }

    @Override
    public void update(T value) {
        // do nothing
    }

    @Override
    public Parameter<T> def(T value) {
        return Parameter.NULL;
    }

    @Override
    public boolean itsMe(String name) {
        return false;
    }

    @Override
    public <V> Parameter<V> type(Class<V> integerClass) {
        return Parameter.NULL;
    }

    @Override
    public void select(int index) {
        // do nothing
    }
}
