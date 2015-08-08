package com.codenjoy.dojo.puzzlebox.model;

import com.codenjoy.dojo.services.*;

/**
 * Created by indigo on 01.08.2015.
 */
public class Box extends PointImpl implements Joystick, Tickable, State<Elements, Player> {

    private Field field;
    private boolean alive;
    private boolean done;
    private Direction direction;
    public boolean isMoving;
    public boolean isCurrent;

    public Box(Point xy) {
        super(xy);
        direction = null;
        isMoving = false;
        alive = true;
        isCurrent = false;
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        if (!alive || isMoving || done) return;

        isMoving = true;
        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive || isMoving || done) return;


        isMoving = true;
        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive || isMoving || done) return;

        isMoving = true;
        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        if (!alive || isMoving || done) return;

        isMoving = true;
        direction = Direction.RIGHT;
    }

    @Override
    public void act(int... p) {
        if (!alive) return;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (isMoving) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);

            if(field.isBarrier(newX,newY)){
                stopMoving();
                return;
            }

            if(field.isTarget(newX, newY)) {
                stopMoving();
                done = true;
            }

            move(newX, newY);
        }
    }

    private void stopMoving() {
        isMoving = false;
        direction = null;
    }

    public boolean getDone() {
        return done;
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if(done) {
            return Elements.FILEDBOX;
        }
        else if(isCurrent){
            return Elements.CURBOX;
        }
        else {
            return Elements.BOX;
        }
    }


}
