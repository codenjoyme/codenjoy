package com.codenjoy.dojo.moebius.model;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.Direction;

public class Line extends PointImpl implements State<Elements, Player> {

    private Direction direction;
    private boolean type;

    public Line(Point pt, Elements element) {
        super(pt);

        switch (element) {
            case LEFT_RIGHT:
                type = true;
                direction = Direction.LEFT;
                return;
            case UP_DOWN:
                type = true;
                direction = Direction.UP;
                return;
            case DOWN_LEFT:
                type = false;
                direction = Direction.LEFT;
                return;
            case RIGHT_DOWN:
                type = false;
                direction = Direction.DOWN;
                return;
            case UP_RIGHT:
                type = false;
                direction = Direction.RIGHT;
                return;
            case LEFT_UP:
                type = false;
                direction = Direction.UP;
                return;
        }
        throw new IllegalArgumentException("Неопознанная линия: '" + element + "'");
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (type) {
            switch (direction) {
                case LEFT:
                    return Elements.LEFT_RIGHT;
                case RIGHT:
                    return Elements.LEFT_RIGHT;
                case UP:
                    return Elements.UP_DOWN;
                case DOWN:
                    return Elements.UP_DOWN;
            }
        } else {
            switch (direction) {
                case UP:
                    return Elements.LEFT_UP;
                case RIGHT:
                    return Elements.UP_RIGHT;
                case DOWN:
                    return Elements.RIGHT_DOWN;
                case LEFT:
                    return Elements.DOWN_LEFT;
            }
        }
        throw new IllegalStateException("Чето не так с линией");
    }

    public void rotate() {
        direction = direction.clockwise();
    }
}
