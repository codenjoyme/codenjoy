package com.codenjoy.dojo.puzzlebox.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.services.*;

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

    @Override
    public void message(String command) {
        // do nothing, this should never happen
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
