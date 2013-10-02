package com.codenjoy.dojo.services.settings;

/**
 * User: sanja
 * Date: 26.09.13
 * Time: 8:48
 */
public interface Parameter<T> {
    T getValue();

    String getName();

    void update(T value);

    Parameter<T> def(T value);

    boolean itsMe(String name);

    <V> Parameter<V> type(Class<V> integerClass);

    void select(int index);
}
