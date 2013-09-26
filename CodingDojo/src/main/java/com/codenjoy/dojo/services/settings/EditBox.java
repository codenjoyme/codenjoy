package com.codenjoy.dojo.services.settings;

/**
 * User: sanja
 * Date: 26.09.13
 * Time: 9:10
 */
public class EditBox<T> implements Parameter<T> {

    private String name;
    private T def;
    private T value;

    public EditBox(String name) {
        this.name = name;
    }

    @Override
    public T getValue() {
        return (value == null)?def:value;
    }

    @Override
    public void update(T value) {
        this.value = value;
    }

    @Override
    public Parameter<T> def(T value) {
        def = value;
        return this;
    }

    @Override
    public boolean itsMe(String name) {
        return this.name.equals(name);
    }

    @Override
    public <V> Parameter<V> type(Class<V> integerClass) {
        return (Parameter<V>)this;
    }

    @Override
    public void select(int index) {
        throw new UnsupportedOperationException();
    }
}
