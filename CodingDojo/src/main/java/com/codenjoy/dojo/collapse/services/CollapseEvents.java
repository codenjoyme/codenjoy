package com.codenjoy.dojo.collapse.services;

public enum CollapseEvents {
    SUCCESS, NEW_GAME;

    private int count;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }


}
