package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 5:10
 */
public class Hero extends PointImpl implements Joystick, Tickable {

    private Direction direction;
    private int newX;

    public Hero(Point xy, Direction direction) {
        super(xy);
        this.direction = direction;
    }

    @Override
    public void down() {
        
    }

    @Override
    public void up() {
        
    }

    @Override
    public void left() {
        newX = x - 1;
    }

    @Override
    public void right() {
        
    }

    @Override
    public void act() {
        
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        x = newX;
    }
}
