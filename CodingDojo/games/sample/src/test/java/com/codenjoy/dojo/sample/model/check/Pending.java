package com.codenjoy.dojo.sample.model.check;

public class Pending {

    private boolean enabled = false;
    private String value = null;

    public void value(String value) {
        this.value = value;
    }

    public boolean enabled() {
        return enabled;
    }

    public boolean hasValue() {
        return value != null;
    }

    public String value() {
        return value;
    }

    private Pending copy() {
        Pending result = new Pending();
        result.value = value;
        result.enabled = enabled;
        return result;
    }

    public Pending disable() {
        Pending result = copy();
        enabled = false;
        value = null;
        return result;
    }

    public void enabled(boolean enabled) {
        this.enabled = enabled;
    }
}
