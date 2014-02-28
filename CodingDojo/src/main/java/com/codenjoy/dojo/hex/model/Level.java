package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.Point;

import java.util.List;

public interface Level {

    int getSize();

    List<Point> getWalls();

    List<Hero> getHero();

    List<Point> getGold();
}
