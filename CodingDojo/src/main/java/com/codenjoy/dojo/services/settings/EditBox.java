package com.codenjoy.dojo.services.settings;

/**
 * User: sanja
 * Date: 26.09.13
 * Time: 9:10
 */
public class EditBox implements Parameter {

    private String name;
    private Object def;
    private Object value;

    public EditBox(String name) {
        this.name = name;
    }

    @Override
    public Object getValue() {
        return (value == null)?def:value;
    }

    @Override
    public void update(Object value) {
        this.value = value;
    }

    @Override
    public Parameter def(Object value) {
        def = value;
        return this;
    }

    @Override
    public boolean itsMe(String name) {
        return this.name.equals(name);
    }
}
