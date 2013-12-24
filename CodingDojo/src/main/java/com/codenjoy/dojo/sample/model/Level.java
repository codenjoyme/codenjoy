package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * Я вот для простоты и удобства хочу указывать борду в тестовом виде, а реализация этого интерфейса позволяет мне это сделать
 */
public interface Level {

    /**
     * @return Размер поля (обязательно квадратное)
     */
    int getSize();

    List<Point> getWalls();

    List<Hero> getHero();

    List<Point> getGold();
}
