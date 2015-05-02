package com.codenjoy.dojo.services.settings;

public class CheckBox implements Parameter<Boolean> {
    private Boolean def;
    private Boolean value;
    private String name;

    public CheckBox(String name) {
        this.name = name;
    }

    @Override
    public Boolean getValue() {
        return (value == null) ? def : value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(Boolean value) {
        this.value = value;
    }

    @Override
    public Parameter<Boolean> def(Boolean value) {
        this.def = value;
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
        value = index == 1;
    }
}
