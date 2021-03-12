package com.codenjoy.dojo.icancode.model;

public class Act {

    private int[] data;

    public Act(int... data) {
        this.data = data;
    }

    boolean act() {
        return data.length == 0;
    }

    boolean act(int input) {
        return !act() && data[0] == input;
    }
}