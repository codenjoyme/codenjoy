package com.codenjoy.dojo.excitebike.model.items.bike;

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


import com.codenjoy.dojo.excitebike.model.GameField;
import com.codenjoy.dojo.excitebike.model.Player;
import com.codenjoy.dojo.excitebike.model.items.Shiftable;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

public class Bike extends PlayerHero<GameField> implements State<BikeElementType, Player>, Shiftable {

    private Direction direction;

    private BikeElementType type = BikeElementType.BIKE;

    public Bike(Point xy) {
        super(xy);
    }

    @Override
    public void init(GameField gameField) {
        this.field = gameField;
    }

    @Override
    public void down() {
        if (!isAlive()) return;

        setDirection(Direction.DOWN);
    }

    @Override
    public void up() {
        if (!isAlive()) return;

        setDirection(Direction.UP);
    }

    @Override
    public void left() {
        if (!isAlive()) return;

        changeIncline(BikeElementType.BIKE_INCLINE_LEFT, BikeElementType.BIKE_INCLINE_RIGHT);
    }

    @Override
    public void right() {
        if (!isAlive()) return;

        changeIncline(BikeElementType.BIKE_INCLINE_RIGHT, BikeElementType.BIKE_INCLINE_LEFT);
    }

    private void changeIncline(BikeElementType toIncline, BikeElementType inclinedTo) {
        if (getType() == BikeElementType.BIKE) {
            setType(toIncline);
        } else if (getType() == inclinedTo) {
            setType(BikeElementType.BIKE);
        }
    }

    public void crush() {
        setType(BikeElementType.BIKE_FALLEN);
    }

    public void jump() {
        //TODO add new BikeElementType.JUMP
        //setType(BikeElementType.BIKE_JUMP);
    }

    @Override
    public void act(int... p) {
        //nothing to do
    }

    @Override
    public void tick() {
        if (isAlive()) {
            if (direction != null) {
                int newX = direction.changeX(x);
                int newY = direction.changeY(y);
                move(newX, newY);
                direction = null;
            }
            interactWithOtherElements(x, y);
        } else {
            shift();
        }
    }

    private void interactWithOtherElements(final int x, final int y) {
        int acceleratorStep = 2;
        int inhibitorStep = -2;

        if (field.isAccelerator(x, y)) {
            if (getX() + acceleratorStep < field.size()) {
                move(getX() + acceleratorStep, getY());
            } else {
                move(field.size() - 1, getY());
            }
//            interactWithAcceleratorOrInhibitor(field.size(), -1, acceleratorStep);
        }

        if (field.isInhibitor(x, y)) {
            if (getX() + inhibitorStep >= 0) {
                move(getX() + inhibitorStep, getY());
            } else {
                move(0, getY());
            }

//            interactWithAcceleratorOrInhibitor(0, 0, inhibitorStep);
        }

        if (field.isObstacle(x, y)) {
            crush();
            shift();
        }
    }

//    private void interactWithAcceleratorOrInhibitor(int bound, int dxIfOutOfBound, int step) {
//        if (getX() + step < bound) {
//            move(getX() + step, getY());
//        } else {
//            move(bound + dxIfOutOfBound, getY());
//        }
//    }

    @Override
    public BikeElementType state(Player player, Object... alsoAtPoint) {
        Bike bike = player.getHero();

        return this == bike ? getType() : bike.getType();
    }

    public boolean isAlive() {
        return type != BikeElementType.BIKE_FALLEN;
    }

    private BikeElementType getType() {
        return type;
    }

    private void setType(BikeElementType type) {
        this.type = type;
    }

    private Direction getDirection() {
        return direction;
    }

    private void setDirection(Direction direction) {
        this.direction = direction;
    }

    private void resetDirection() {
        setDirection(null);
    }

}
