package com.codenjoy.dojo.pong.model;

public interface Field {

    boolean isBarrier(int x, int y);

    Barrier getBarrier(int x, int y);

}
