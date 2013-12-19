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
    private boolean jump;

    public Hero(Point xy, Direction direction) {
        super(xy);
        this.direction = direction;
        moving = false;
        drilled = false;
        drill = false;
        alive = true;
        jump = false;
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        if (!alive) return;

        if (field.isLadder(x, y) || field.isLadder(x, y - 1)) {
            direction = Direction.DOWN;
            moving = true;
        } else if (field.isPipe(x, y)) {
            jump = true;
        }
    }

    @Override
    public void up() {
        if (!alive) return;

        if (field.isLadder(x, y)) {
            direction = Direction.UP;
            moving = true;
        }
    }

    @Override
    public void left() {
        if (!alive) return;

        drilled = false;
        direction = Direction.LEFT;
        moving = true;
    }

    @Override
    public void right() {
        if (!alive) return;

        drilled = false;
        direction = Direction.RIGHT;
        moving = true;
    }

    @Override
    public void act() {
        if (!alive) return;
        drill = true;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (isFall()) {
            move(x, y - 1);
        } else if (drill) {
            int dx = direction.changeX(x);
            int dy = y - 1;
            drilled = field.tryToDrill(this, dx, dy);
        } else if (moving || jump) {
            int newX = direction.changeX(x);
            int newY = direction.inverted().changeY(y);

            if (jump) {
                newX = x;
                newY = y - 1;
            }

            if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
            }
        }
        drill = false;
        moving = false;
        jump = false;
    }

    public boolean isDrilled() {
        return drilled;
    }

    public boolean isAlive() {
        if (alive) {
            checkAlive();
        }
        return alive;
    }

    private void checkAlive() {
        if (field.isBarrier(x, y)) {
            alive = false;
        }
    }

    public boolean isFall() {
        return field.isPit(x, y) && !field.isPipe(x, y);
    }
}
