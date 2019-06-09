package com.codenjoy.dojo.excitebike.model.items;

import com.codenjoy.dojo.services.Point;

public interface Shiftable extends Point {
    default void shift(){
        move(getX()-1, getY());
    }
}
