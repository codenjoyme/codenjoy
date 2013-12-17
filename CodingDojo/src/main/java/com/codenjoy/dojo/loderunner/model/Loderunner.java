package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.*;

import java.util.List;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:56
 */
public class Loderunner implements Tickable, Game, Field {

    private final List<Point> borders;
    private final List<Brick> bricks;
    private final Hero hero;
    private final int size;

    public Loderunner(Level level) {
        borders = level.getBorders();
        bricks = level.getBricks();
        hero = level.getHero().iterator().next();
        hero.init(this);
        size = level.getSize();
    }

    @Override
    public void tick() {
        hero.tick();
        for (Brick brick : bricks) {
            brick.tick();
        }
    }

    public int getSize() {
        return size;
    }

    @Override
    public Joystick getJoystick() {
        return hero; 
    }

    @Override
    public int getMaxScore() {
        return 0;  
    }

    @Override
    public int getCurrentScore() {
        return 0;  
    }

    @Override
    public boolean isGameOver() {
        return false;  
    }

    @Override
    public void newGame() {
        
    }

    @Override
    public String getBoardAsString() {
        return null;  
    }

    @Override
    public void destroy() {
        
    }

    @Override
    public void clearScore() {
        
    }

    public List<Point> getBorders() {
        return borders;
    }

    public Hero getHero() {
        return hero;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        return x >= size - 1 || x <= 0;
    }

    @Override
    public void tryToDrill(int x, int y) {
        Brick brick = getBrick(x, y);
        brick.drill();
    }

    private Brick getBrick(int x, int y) {
        for (Brick brick : bricks) {
            if (brick.itsMe(x, y)) return brick;
        }
        return Brick.NULL;
    }
}
