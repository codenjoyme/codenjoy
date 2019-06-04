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
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

public class Bike extends PlayerHero<GameField> implements State<BikeElementType, Player> {

    private boolean front;

    private Direction direction;
    private BikeState state;

    private BikeElementType type;

    public Bike(Point xy, boolean front, BikeElementType type) {
        super(xy);
        direction = null;
        this.front = front;
        this.type = type;
        state = BikeState.HORIZONTAL;
    }

    @Override
    public void init(GameField gameField) {
        this.field = gameField;
    }

    @Override
    public void down() {
//        if (!alive) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
//        if (!alive) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        //nothing to do
    }

    @Override
    public void right() {
        //nothing to do
    }

    @Override
    public void act(int... p) {
//        if (!alive) return;
//
//        field.setBomb(x, y);
    }

    @Override
    public void tick() {
//        if (!alive) return;
//
//        if (direction != null) {
//            int newX = direction.changeX(x);
//            int newY = direction.changeY(y);
//
//            if (field.isBomb(newX, newY)) {
//                alive = false;
//                field.removeBomb(newX, newY);
//            }
//
//            if (!field.isBarrier(newX, newY)) {
//                move(newX, newY);
//            }
//        }
        direction = null;
    }

    public boolean isFront() {
        return front;
    }

    public boolean isAlive() {
        return state != BikeState.FALLEN;
    }

    public BikeState getState() {
        return state;
    }

    @Override
    public BikeElementType state(Player player, Object... alsoAtPoint) {
        Bike bike = player.getHero();

        return this == bike
                ? getTypeDependsOnState(state, isFront())
                : getTypeDependsOnState(bike.getState(), bike.isFront());
    }

    private static BikeElementType getTypeDependsOnState(BikeState state, boolean front) {
        switch (state) {
            case HORIZONTAL: {
                return front ? BikeElementType.BIKE_FRONT : BikeElementType.BIKE_BACK;
            }
            case INCLINE_LEFT: {
                return front ? BikeElementType.BIKE_INCLINE_LEFT_FRONT : BikeElementType.BIKE_INCLINE_LEFT_BACK;
            }
            case INCLINE_RIGHT: {
                return front ? BikeElementType.BIKE_INCLINE_RIGHT_FRONT : BikeElementType.BIKE_INCLINE_RIGHT_BACK;
            }
            case FALLEN: {
                return front ? BikeElementType.BIKE_FALLEN_FRONT : BikeElementType.BIKE_FALLEN_BACK;
            }
            default:
                return null;    //TODO refactor this null
        }
    }
}
