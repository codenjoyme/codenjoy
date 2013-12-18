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
    private boolean drill;
    private boolean drilled;
    private boolean alive;

    public Hero(Point xy, Direction direction) {
        super(xy);
        this.direction = direction;
        moving = false;
        drilled = false;
        drill = false;
        alive = true;
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        
    }

    @Override
    public void up() {
        if (field.isLadder(x, y)) {
            direction = Direction.UP;
            moving = true;
        }
    }

    @Override
    public void left() {
        drilled = false;
        direction = Direction.LEFT;
        moving = true;
    }

    @Override
    public void right() {
        drilled = false;
        direction = Direction.RIGHT;
        moving = true;
    }

    @Override
    public void act() {
        drill = true;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (!alive) {
            alive = true; // TODO fire event to Player
            Point pt = field.getFreeRandom();
            move(pt.getX(), pt.getY());
        } else if (field.isPit(x, y)) {
            move(x, y - 1);
        } else if (drill) {
            int dx = direction.changeX(x);
            int dy = y - 1;
            drilled = field.tryToDrill(dx, dy);
        } else if (moving) {
            int newX = direction.changeX(x);
            int newY = direction.inverted().changeY(y);

            if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
            }
        }
        drill = false;
        moving = false;
    }

    public boolean isDrilled() {
        return drilled;
    }

    public boolean isAlive() {
        return alive;
    }

    public void checkAlive() {
        if (field.isBarrier(x, y)) {
            alive = false;
        }
    }
}
