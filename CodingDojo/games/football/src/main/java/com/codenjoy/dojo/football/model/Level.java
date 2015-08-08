package com.codenjoy.dojo.football.model;

import java.util.List;

import com.codenjoy.dojo.football.model.elements.Ball;
import com.codenjoy.dojo.football.model.elements.Goal;
import com.codenjoy.dojo.football.model.elements.Hero;
import com.codenjoy.dojo.football.model.elements.Wall;

/**
 * Я вот для простоты и удобства хочу указывать борду в тестовом виде, а реализация этого интерфейса позволяет мне это сделать
 */
public interface Level {

    /**
     * @return Размер поля (обязательно квадратное)
     */
    int getSize();

    List<Wall> getWalls();

    List<Hero> getHero();

	List<Ball> getBalls();

	List<Goal> getTopGoals();

	List<Goal> getBottomGoals();

}
