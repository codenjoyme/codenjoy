package com.codenjoy.dojo.hex.model;

import java.util.List;

public interface Level {

    int getSize();

    List<Wall> getWalls();

    List<Hero> getHeroes();
}
