package com.codenjoy.dojo.excitebike.model.items;

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
import com.codenjoy.dojo.excitebike.model.elements.BikeType;
import com.codenjoy.dojo.excitebike.services.Events;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.Objects;

import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_ACCELERATOR;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_INHIBITOR;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_KILLED_BIKE;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_LINE_CHANGER_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_LINE_CHANGER_UP;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_SPRINGBOARD_LEFT;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_SPRINGBOARD_LEFT_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_SPRINGBOARD_RIGHT;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_SPRINGBOARD_RIGHT_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_FALLEN;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_FALLEN_AT_ACCELERATOR;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_FALLEN_AT_FENCE;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_FALLEN_AT_INHIBITOR;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_FALLEN_AT_LINE_CHANGER_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_FALLEN_AT_LINE_CHANGER_UP;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_FALLEN_AT_OBSTACLE;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_IN_FLIGHT_FROM_SPRINGBOARD;
import static com.codenjoy.dojo.services.Direction.DOWN;
import static com.codenjoy.dojo.services.Direction.RIGHT;
import static com.codenjoy.dojo.services.Direction.UP;

public class Bike extends PlayerHero<GameField> implements State<BikeType, Player>, Shiftable {

    public static final String OTHER_BIKE_PREFIX = "OTHER";
    public static final String FALLEN_BIKE_SUFFIX = "FALLEN";
    public static final String BIKE_AT_PREFIX = "AT_";
    public static final String AT_ACCELERATOR_SUFFIX = "_AT_ACCELERATOR";
    public static final String AT_INHIBITOR_SUFFIX = "_AT_INHIBITOR";
    public static final String AT_LINE_CHANGER_UP_SUFFIX = "_AT_LINE_CHANGER_UP";
    public static final String AT_LINE_CHANGER_DOWN_SUFFIX = "_AT_LINE_CHANGER_DOWN";

    private Direction command;
    private Movement movement = new Movement();
    private BikeType type = BIKE;
    private boolean ticked;
    private boolean accelerated;
    private boolean inhibited;
    private boolean interacted;
    private boolean atSpringboard;
    private boolean adjusted;
    private boolean movementLock;

    public Bike(Point xy) {
        super(xy);
    }

    public Bike(int x, int y) {
        super(x, y);
    }

    @Override
    public void init(GameField gameField) {
        this.field = gameField;
        if (field.isSpringboardTopElement(x, y)) {
            atSpringboard = true;
        }
        if (isNextStepSpringboardRiseOrDecent()) {
            movementLock = true;
        }
    }

    @Override
    public void down() {
        if (!isAlive()) return;
        command = DOWN;
    }

    @Override
    public void up() {
        if (!isAlive()) return;
        command = UP;
    }

    @Override
    public void left() {
    }

    @Override
    public void right() {
    }

    public void crush() {
        type = type == BIKE_AT_ACCELERATOR ? BIKE_FALLEN_AT_ACCELERATOR :
                type == BIKE_AT_INHIBITOR ? BIKE_FALLEN_AT_INHIBITOR :
                        type == BIKE_AT_LINE_CHANGER_DOWN ? BIKE_FALLEN_AT_LINE_CHANGER_DOWN :
                                type == BIKE_AT_LINE_CHANGER_UP ? BIKE_FALLEN_AT_LINE_CHANGER_UP :
                                        BIKE_FALLEN;
    }

    public void crushLikeEnemy(BikeType crushedEnemyBikeType) {
        type = BikeType.valueOf(crushedEnemyBikeType.name().replace(OTHER_BIKE_PREFIX + "_", ""));
    }

    @Override
    public void act(int... p) {
        //nothing to do
    }

    @Override
    public void tick() {
        if (!ticked && isAlive()) {
            adjusted = false;
            actAccordingToState();
            if (movementLock) {
                command = null;
                accelerated = false;
            }
            executeCommand();
            tryToMove();
            adjustStateToElement();
            if(!isAlive()){
                field.getPlayerOfBike(this).event(Events.LOSE);
            }
        }
    }

    private void executeCommand() {
        interacted = false;
        if (command != null) {
            x = command.changeX(x);
            y = command.changeY(y);
            interactWithOtherBike();
            command = null;
            adjustStateToElement();
        }
    }

    private void actAccordingToState() {
        if (type == BIKE_AT_INHIBITOR) {
            if (!inhibited) {
                movement.setLeft();
                inhibited = true;
            }
            type = atNothingType();
        } else {
            inhibited = false;
        }

        if (type == BIKE_AT_LINE_CHANGER_UP) {
            movement.setUp();
            type = atNothingType();
        }

        if (type == BIKE_AT_LINE_CHANGER_DOWN) {
            movement.setDown();
            type = atNothingType();
        }

        if (type == BIKE_AT_ACCELERATOR || accelerated) {
            movement.setRight();
            type = atNothingType();
            accelerated = false;
            adjustStateToElement();
        }

        if (type == BIKE_AT_SPRINGBOARD_LEFT
                || type == BIKE_AT_SPRINGBOARD_LEFT_DOWN
                || type == BIKE_AT_SPRINGBOARD_RIGHT
                || type == BIKE_AT_SPRINGBOARD_RIGHT_DOWN) {
            movementLock = field.isSpringboardLightElement(x, y) || field.isSpringboardRightDownElement(x, y);
            type = atNothingType();
            return;
        }

        if (type == BIKE_IN_FLIGHT_FROM_SPRINGBOARD) {
            movementLock = true;
            movement.setDown();
        }

    }

    private BikeType atNothingType() {
        return type.name().contains(BIKE_AT_PREFIX)
                ? BikeType.valueOf(type.name().substring(0, type.name().indexOf(BIKE_AT_PREFIX) - 1))
                : type;
    }

    private void tryToMove() {
        int xBefore = x;
        int yBefore = y;
        if (!isAlive()) {
            return;
        }
        if (movement.isUp()) {
            y = UP.changeY(y);
        }
        if (movement.isDown()) {
            y = DOWN.changeY(y);
        }
        if (movement.isLeft()) {
            x = Direction.LEFT.changeX(x);
            if (isAlive() && x < 0) {
                x = 0;
            }
        }
        if (movement.isRight()) {
            x = RIGHT.changeX(x);
            if (x >= field.size()) {
                x = field.size() - 1;
            }
        }
        interactWithOtherBike();
        movement.clear();
        if (xBefore != x || yBefore != y) {
            adjusted = false;
        }
    }

    private void interactWithOtherBike() {
        if (interacted) {
            return;
        }
        field.getEnemyBike(x, y, field.getPlayerOfBike(this)).ifPresent(enemy -> {
            if (enemy != this) {
                if (!enemy.isAlive() ||
                        (movement.isRight()
                                && !enemy.movement.isRight()
                                && !enemy.movement.isUp()
                                && !enemy.movement.isDown()
                                && enemy.command == null)) {
                    if (enemy.isAlive()) {
                        enemy.type = BIKE_AT_KILLED_BIKE;
                        crush();
                    } else {
                        crushLikeEnemy(enemy.getEnemyBikeType());
                    }
                    return;
                }
                if (!enemy.movement.isUp() && !enemy.movement.isDown() && enemy.command == null) {
                    enemy.crush();
                    type = BIKE_AT_KILLED_BIKE;
                    field.getPlayerOfBike(this).event(Events.WIN);
                    move(enemy);
                    enemy.ticked = true;
                    field.getPlayerOfBike(enemy).event(Events.LOSE);
                    movement.clear();
                    command = null;
                } else if (((movement.isDown() || command == DOWN) && (enemy.movement.isUp() || enemy.command == UP))
                        || ((movement.isUp() || command == UP) && (enemy.movement.isDown() || enemy.command == DOWN))) {
                    enemy.clearY();
                    if (movement.isUp() || command == UP) {
                        move(x, DOWN.changeY(y));
                    } else if (movement.isDown() || command == DOWN) {
                        move(x, UP.changeY(y));
                    }
                    clearY();
                } else {
                    enemy.tick();
                    enemy.ticked = true;
                }
            }
        });
        interacted = true;
    }

    private void clearY() {
        if (movement.isUp()) {
            movement.setDown();
        } else if (movement.isDown()) {
            movement.setUp();
        }
        command = null;
    }

    private void adjustStateToElement() {
        if (!isAlive() || adjusted) {
            return;
        }
        adjusted = true;

        if (isNextStepSpringboardRiseOrDecent()) {
            movementLock = true;
        }

        if (field.isSpringboardDarkElement(x, y)) {
            if (y == 1 && !movement.isUp()) {
                type = BIKE_IN_FLIGHT_FROM_SPRINGBOARD;
                atSpringboard = false;
            } else {
                type = BIKE_AT_SPRINGBOARD_LEFT;
                atSpringboard = true;
            }
            return;
        }

        if (field.isSpringboardLightElement(x, y)) {
            type = BIKE_AT_SPRINGBOARD_RIGHT;
            atSpringboard = false;
            return;
        }

        if (field.isSpringboardLeftDownElement(x, y)) {
            type = BIKE_AT_SPRINGBOARD_LEFT_DOWN;
            atSpringboard = true;
            return;
        }

        if (field.isSpringboardRightDownElement(x, y)) {
            type = BIKE_AT_SPRINGBOARD_RIGHT_DOWN;
            atSpringboard = false;
            return;
        }

        if (field.isAccelerator(x, y)) {
            changeStateToAt(AT_ACCELERATOR_SUFFIX);
            accelerated = true;
            return;
        }

        if (field.isInhibitor(x, y)) {
            if (type.name().contains(AT_ACCELERATOR_SUFFIX)) {
                type = BikeType.valueOf(type.name().replace(AT_ACCELERATOR_SUFFIX, ""));
            } else {
                changeStateToAt(AT_INHIBITOR_SUFFIX);
            }
            if (movement.isRight()) {
                movement.setLeft();
                inhibited = true;
            }
            return;
        }

        if (field.isObstacle(x, y)) {
            if (movement.isRight()) {
                movement.setLeft();
            }
            type = BIKE_FALLEN_AT_OBSTACLE;
            return;
        }

        if (field.isUpLineChanger(x, y)) {
            if (movement.isRight()) {
                movement.setUp();
            } else {
                changeStateToAt(AT_LINE_CHANGER_UP_SUFFIX);
            }
            return;
        }

        if (field.isDownLineChanger(x, y)) {
            if (movement.isRight()) {
                movement.setDown();
            } else {
                changeStateToAt(AT_LINE_CHANGER_DOWN_SUFFIX);
            }
            return;
        }

        if (field.isFence(x, y) && !atSpringboard) {
            type = BIKE_FALLEN_AT_FENCE;
            return;
        }

        if (atSpringboard && y >= field.size()) {
            crush();
            return;
        }

        if (!field.getEnemyBike(x, y, field.getPlayerOfBike(this)).isPresent()) {
            type = atNothingType();
        }
    }

    private void changeStateToAt(String atSuffix) {
        if (type.name().contains(BIKE_AT_PREFIX)) {
            String substringBikeAtSomething = type.name().substring(type.name().indexOf(BIKE_AT_PREFIX) - 1);
            type = BikeType.valueOf(type.name().replace(substringBikeAtSomething, atSuffix));
        } else {
            type = BikeType.valueOf(type.name() + atSuffix);
        }
    }

    @Override
    public BikeType state(Player player, Object... alsoAtPoint) {
        Bike bike = player.getHero();
        return this == bike ? bike.type : this.getEnemyBikeType();
    }

    private BikeType getEnemyBikeType() {
        return BikeType.valueOf(OTHER_BIKE_PREFIX + "_" + type.name());
    }

    public boolean isAlive() {
        return type != null && !type.name().contains(FALLEN_BIKE_SUFFIX);
    }

    public void setTicked(boolean ticked) {
        this.ticked = ticked;
    }

    public void changeYDependsOnSpringboard() {
        if (type == BIKE_AT_SPRINGBOARD_LEFT
                || type == BIKE_AT_SPRINGBOARD_LEFT_DOWN) {
            y++;
        }
        if (field.isSpringboardLightElement(x, y - 1) || field.isSpringboardRightDownElement(x, y - 1)) {
            y--;
        }
    }

    private boolean isNextStepSpringboardRiseOrDecent() {
        return field.isSpringboardLightElement(x + 1, y - 1)
                || field.isSpringboardRightDownElement(x + 1, y - 1)
                || field.isSpringboardDarkElement(x + 1, y)
                || field.isSpringboardLeftDownElement(x + 1, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bike bike = (Bike) o;
        return ticked == bike.ticked &&
                accelerated == bike.accelerated &&
                inhibited == bike.inhibited &&
                Objects.equals(movement, bike.movement) &&
                type == bike.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), movement, type, ticked, inhibited, accelerated);
    }

    @Override
    public String toString() {
        return "Bike{" +
                "movement=" + movement +
                ", type=" + type +
                ", ticked=" + ticked +
                ", accelerated=" + accelerated +
                ", inhibited=" + inhibited +
                ", atSpringboard=" + atSpringboard +
                ", movementLock=" + movementLock +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    class Movement {

        private boolean up;
        private boolean down;
        private boolean left;
        private boolean right;

        public Movement() {
        }

        public boolean isUp() {
            return up;
        }

        public void setUp() {
            if (down) {
                down = false;
            } else {
                up = true;
            }
        }

        public boolean isDown() {
            return down;
        }

        public void setDown() {
            if (up) {
                up = false;
            } else {
                down = true;
            }
        }

        public boolean isLeft() {
            return left;
        }

        public void setLeft() {
            if (right) {
                right = false;
            } else {
                left = true;
            }
        }

        public boolean isRight() {
            return right;
        }

        public void setRight() {
            if (left) {
                left = false;
            } else {
                right = true;
            }
        }

        public void clear() {
            up = false;
            down = false;
            left = false;
            right = false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Movement movement = (Movement) o;
            return up == movement.up &&
                    down == movement.down &&
                    left == movement.left &&
                    right == movement.right;
        }

        @Override
        public int hashCode() {
            return Objects.hash(up, down, left, right);
        }

        @Override
        public String toString() {
            return "Movement{" +
                    "up=" + up +
                    ", down=" + down +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
}
