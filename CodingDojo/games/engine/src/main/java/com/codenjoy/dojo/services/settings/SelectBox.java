package com.codenjoy.dojo.services.settings;

import java.util.List;

public class SelectBox<T> implements Parameter<T> {

    private String name;
    private List<T> options;
    private Integer selected;
    private Integer def;

    public SelectBox(String name, List<T> options) {
        this.name = name;
        this.options = options;
    }

    @Override
    public T getValue() {
        return (selected == null)?(def == null)?null:options.get(def):options.get(selected);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(T value) {
        checkIsPresent(value);
        this.selected = options.indexOf(value);

    }

    private void checkIsPresent(T value) {
        if (!options.contains(value)) {
            throw new IllegalArgumentException(String.format("No option '%s' in set %s", value, options));
        }
    }

    @Override
    public Parameter<T> def(T value) {
        checkIsPresent(value);
        this.def = options.indexOf(value);
        return this;
    }

    @Override
    public boolean itsMe(String name) {
        return this.name.equals(name);
    }

    public <V> Parameter<V> type(Class<V> integerClass) {
        return (Parameter<V>)this;
    }

    @Override
    public void select(int index) {
        this.selected = index;
    }
}
