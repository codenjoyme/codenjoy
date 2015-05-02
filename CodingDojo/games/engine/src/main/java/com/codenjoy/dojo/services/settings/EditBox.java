package com.codenjoy.dojo.services.settings;

public class EditBox<T> implements Parameter<T> {

    private String name;
    private T def;
    private T value;
    private Class<?> type;

    public EditBox(String name) {
        this.name = name;
    }

    @Override
    public T getValue() {
        return (value == null)?def:value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(T value) {
        if (value instanceof String) {
            if (Integer.class.equals(type)) {
                this.value = (T)Integer.valueOf((String)value);  // TODO потестить это
            }
        } else {
            this.value = value;
        }
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
    public <V> Parameter<V> type(Class<V> type) {
        this.type = type; // TODO сделать это же с другими элементами
        return (Parameter<V>)this;
    }

    @Override
    public void select(int index) {
        throw new UnsupportedOperationException();
    }
}
