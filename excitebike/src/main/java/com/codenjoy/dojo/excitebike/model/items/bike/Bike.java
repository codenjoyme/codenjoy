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

public class Bike extends PlayerHero<GameField> implements State<BikeElementType, Player>, Shiftable {

    private Direction direction;

    private BikeElementType type = BikeElementType.BIKE;

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

        changeIncline(BikeElementType.BIKE_INCLINE_LEFT, BikeElementType.BIKE_INCLINE_RIGHT);
    }

    @Override
    public void right() {
        if (!isAlive()) return;

        changeIncline(BikeElementType.BIKE_INCLINE_RIGHT, BikeElementType.BIKE_INCLINE_LEFT);
    }

    private void changeIncline(BikeElementType toIncline, BikeElementType inclinedTo) {
        if (type == BikeElementType.BIKE) {
            type = toIncline;
        } else if (type == inclinedTo) {
            type = BikeElementType.BIKE;
        }
    }

    public void crush() {
        type = BikeElementType.BIKE_FALLEN;
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
                move1(newX, newY);
            }
            interactWithOtherElements(x, y);
        } else {
            shift();
        }
    }

    private void move1(final int x, final int y){
        Optional<Bike> enemyBike = field.getEnemyBike(x, y);
        enemyBike.ifPresent(this::interactWithOtherBike);

        if (!field.isBorder(x, y) && !enemyBike.isPresent()) {
            move(x, y);
        }
        direction = null;
    }

    private void interactWithOtherBike(Bike enemyBike) {
        if (enemyBike.direction == null) {
            enemyBike.crush();
        } else {
            enemyBike.direction = null;
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
        }

        if (field.isInhibitor(x, y)) {
            if (x + inhibitorStep >= 0) {
                move(x + inhibitorStep, y);
            } else {
                move(0, y);
            }
        }

        if (field.isObstacle(x, y)) {
            crush();
            shift();
        }

        if (field.isUpLineChanger(x, y)) {

        }

        if (field.isUpLineChanger(x, y)) {

        }

    }

    @Override
    public BikeElementType state(Player player, Object... alsoAtPoint) {
        Bike bike = player.getHero();

        return this == bike ? type : bike.type;
    }

    public boolean isAlive() {
        return type != BikeElementType.BIKE_FALLEN;
    }

    private BikeElementType getType() {
        return type;
    }

    private Direction getDirection() {
        return direction;
    }
}
