package com.codenjoy.dojo.services.settings;

import java.util.List;

/**
 * User: sanja
 * Date: 26.09.13
 * Time: 9:15
 */
public class SelectBox implements Parameter {

    private String name;
    private List<Object> options;
    private Integer selected;
    private Integer def;

    public SelectBox(String name, List<Object> options) {
        this.name = name;
        this.options = options;
    }

    @Override
    public Object getValue() {
        return (selected == null)?(def == null)?null:options.get(def):options.get(selected);
    }

    @Override
    public void update(Object index) {
        this.selected = (Integer)index;

    }

    @Override
    public Parameter def(Object index) {
        this.def = (Integer)index;
        return this;
    }

    @Override
    public boolean itsMe(String name) {
        return this.name.equals(name);
    }
}
