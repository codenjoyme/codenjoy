package com.codenjoy.dojo.services.settings;

/**
 * User: sanja
 * Date: 26.09.13
 * Time: 9:24
 */
public class CheckBox implements Parameter {
    private Boolean def;
    private Boolean value;
    private String name;

    public CheckBox(String name) {
        this.name = name;
    }

    @Override
    public Object getValue() {
        return (value == null) ? def : value;
    }

    @Override
    public void update(Object value) {
        this.value = (Boolean) value;
    }

    @Override
    public Parameter def(Object value) {
        this.def = (Boolean) value;
        return this;
    }

    @Override
    public boolean itsMe(String name) {
        return this.name.equals(name);
    }
}
