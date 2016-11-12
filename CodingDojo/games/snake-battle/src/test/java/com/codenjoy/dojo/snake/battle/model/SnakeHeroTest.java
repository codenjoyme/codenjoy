package com.codenjoy.dojo.snake.battle.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kors
 */
public class SnakeHeroTest {

    private SnakeBoard game;
    private Hero hero;

    @Before
    public void setup() {
        hero = new Hero(new PointImpl(0, 0));
        game = mock(SnakeBoard.class);
        hero.init(game);
    }

    // Проверка что змейка увеличивается
    @Test
    public void snakeEncreasing() {
        assertTrue("Змейка мертва!", hero.isAlive());
        int before = hero.size();
        applesAtAllPoints(true);// впереди яблоко -> увеличиваем змейку
        hero.tick();
        applesAtAllPoints(false);
        assertTrue("Змейка не увеличилась!", before + 1 == hero.size());
    }

    // тест что короткая змейка погибает от камня
    @Test
    public void diedByStone() {
        assertTrue("Змейка мертва!", hero.isAlive());
        for (int i = 0; i < 3; i++)
            snakeEncreasing();
        stonesAtAllPoints(true);// впереди камень
        hero.tick();
        stonesAtAllPoints(false);
        assertTrue("Маленькая змейка не погибла от камня!", !hero.isAlive());
    }

    // тест что большая змейка уменьшается от камня, но не погибает
    @Test
    public void reduceByStone() {
        assertTrue("Змейка мертва!", hero.isAlive());
        // для длинной змейки
        for (int i = 0; i < 4; i++)
            snakeEncreasing();
        int before = hero.size();
        stonesAtAllPoints(true);// впереди камень
        hero.tick();
        stonesAtAllPoints(false);
        assertTrue("Большая змейка погибла от камня!", hero.isAlive());
        assertEquals("Змейка не укоротилась на предполагаемую длину!", before - 4, hero.size());
    }

    private void applesAtAllPoints(boolean enable) {
        when(game.isApple(any(Point.class))).thenReturn(enable);// впереди камень
    }

    private void stonesAtAllPoints(boolean enable) {
        when(game.isStone(any(Point.class))).thenReturn(enable);// впереди камень
    }

}
