package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 5:10
 */
public class Hero extends PointImpl implements Joystick, Tickable {

    private Direction direction;
    private boolean moving;
    private Field field;

    public Hero(Point xy, Direction direction) {
        super(xy);
        this.direction = direction;
        moving = false;
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        
    }

    @Override
    public void up() {
        
    }

    @Override
    public void left() {
        direction = Direction.LEFT;
        moving = true;
    }

    @Override
    public void right() {
        direction = Direction.RIGHT;
        moving = true;
    }

    @Override
    public void act() {
        
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (moving) {
            int newX = direction.changeX(x);

            if (!field.isBarrier(newX, y)) {
                move(newX, y);
            }
            moving = false;
        }
    }
}
