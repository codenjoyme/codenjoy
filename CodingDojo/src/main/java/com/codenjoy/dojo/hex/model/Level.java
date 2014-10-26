package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.Point;

import java.util.List;

public interface Level {

    int getSize();

    List<Wall> getWalls();

    List<Hero> getHeroes();
}
