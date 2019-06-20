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
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.Optional;

public class Bike extends PlayerHero<GameField> implements State<BikeType, Player>, Shiftable {

    private Direction direction;

    private BikeType type = BikeType.BIKE;

    public Bike(Point xy) {
        super(xy);
    }

    public Bike(int x, int y) {
        super(x, y);
    }

    @Override
    public void init(GameField gameField) {
        this.field = gameField;
    }

    @Override
    public void down() {
        if (!isAlive()) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!isAlive()) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!isAlive()) return;

        changeIncline(BikeType.BIKE_INCLINE_LEFT, BikeType.BIKE_INCLINE_RIGHT);
    }

    @Override
    public void right() {
        if (!isAlive()) return;

        changeIncline(BikeType.BIKE_INCLINE_RIGHT, BikeType.BIKE_INCLINE_LEFT);
    }

    private void changeIncline(BikeType toIncline, BikeType inclinedTo) {
        if (type == BikeType.BIKE) {
            type = toIncline;
        } else if (type == inclinedTo) {
            type = BikeType.BIKE;
        }
    }

    public void crush() {
        type = BikeType.BIKE_FALLEN;
    }

    public void jump() {
        //TODO add new BikeType.JUMP for springboard TASK - 26-springboard-item
        //type = BikeType.BIKE_JUMP;
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
                tryToMove(newX, newY);
            }
            interactWithOtherElements(x, y);
        } else if (x >= 0) {
            shift();
        }
    }

    private void tryToMove(final int x, final int y) {
        Optional<Bike> enemyBike = field.getEnemyBike(x, y);
        enemyBike.ifPresent(this::interactWithOtherBike);

        if (canMoveTo(x, y) && direction != null) {
            move(x, y);
            direction = null;
        }
    }

    private boolean canMoveTo(final int x, final int y) {
        return !field.isBorder(x, y);
    }

    private void interactWithOtherBike(Bike enemyBike) {
        if (enemyBike.direction == null) {
            enemyBike.crush();
            direction = null;
        } else if (direction != enemyBike.direction) {
            enemyBike.direction = null;
            direction = null;
        }
    }

    private void interactWithOtherElements(final int x, final int y) {
        int acceleratorStep = 2;
        int inhibitorStep = -2;

        if (field.isAccelerator(x, y)) {
            if (x + acceleratorStep < field.size()) {
                move(x + acceleratorStep, y);
            } else {
                move(field.size() - 1, y);
            }
            return;
        }

        if (field.isInhibitor(x, y)) {
            if (x + inhibitorStep >= 0) {
                move(x + inhibitorStep, y);
            } else {
                move(0, y);
            }
            return;
        }

        if (field.isObstacle(x, y)) {
            crush();
            shift();
            return;
        }

        if (field.isUpLineChanger(x, y)) {
            direction = Direction.UP;
            tryToMove(x, direction.changeY(y));
            return;
        }

        if (field.isDownLineChanger(x, y)) {
            direction = Direction.DOWN;
            tryToMove(x, direction.changeY(y));
        }
    }

    @Override
    public BikeType state(Player player, Object... alsoAtPoint) {
        Bike bike = player.getHero();

        return this == bike ? bike.type : this.getEnemyBikeType();
    }

    private BikeType getEnemyBikeType() {
        switch (type) {
            case BIKE:
                return BikeType.OTHER_BIKE;
            case BIKE_FALLEN:
                return BikeType.OTHER_BIKE_FALLEN;
            case BIKE_INCLINE_LEFT:
                return BikeType.OTHER_BIKE_INCLINE_LEFT;
            case BIKE_INCLINE_RIGHT:
                return BikeType.OTHER_BIKE_INCLINE_RIGHT;
            default:
                throw new IllegalArgumentException("No such element for " + type);
        }
    }

    public boolean isAlive() {
        return type != BikeType.BIKE_FALLEN;
    }

    @Override
    public String toString() {
        return "Bike{" +
                "direction=" + direction +
                ", type=" + type +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
