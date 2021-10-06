package com.codenjoy.dojo.sample.model.check;

public class Caller {

    private String name;
    private Object wrapper;
    private Object[] args;

    public Caller(String name, Object wrapper, Object... args) {
        this.name = name;
        this.wrapper = wrapper;
        this.args = args;
    }

    public Object wrapper() {
        return wrapper;
    }

    public Object[] args() {
        return args;
    }

    public String name() {
        return name;
    }
}
