package com.codenjoy.dojo.services.settings;

public class SimpleParameter<T> implements Parameter<T> {

    private T value;

    public SimpleParameter(T value) {
        this.value = value;
    }

    public static Parameter<Integer> v(int value) {
        return new SimpleParameter<Integer>(value);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Object value) {
        this.value = (T)value;
    }

    @Override
    public Parameter def(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean itsMe(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void select(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Parameter type(Class integerClass) {
        throw new UnsupportedOperationException();
    }
}
