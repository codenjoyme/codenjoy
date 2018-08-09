package com.codenjoy.dojo.moebius.model;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

public class Line extends PointImpl implements State<Elements, Player> {
    enum Type {
        CROSS, LINE, CORNER;
    }

    private Direction direction;
    private Type type;

    public Line(Point pt, Elements element) {
        super(pt);

        switch (element) {
            case LEFT_RIGHT:
                type = Type.LINE;
                direction = Direction.LEFT;
                return;
            case UP_DOWN:
                type = Type.LINE;
                direction = Direction.UP;
                return;
            case DOWN_LEFT:
                type = Type.CORNER;
                direction = Direction.LEFT;
                return;
            case RIGHT_DOWN:
                type = Type.CORNER;
                direction = Direction.DOWN;
                return;
            case UP_RIGHT:
                type = Type.CORNER;
                direction = Direction.RIGHT;
                return;
            case LEFT_UP:
                type = Type.CORNER;
                direction = Direction.UP;
                return;
            case CROSS:
                type = Type.CROSS;
                direction = Direction.UP;
                return;
        }
        throw new IllegalArgumentException("Неопознанная линия: '" + element + "'");
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return state();
    }

    private Elements state() {
        switch (type) {
            case LINE:
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
                break;
            case CORNER:
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
                break;
            case CROSS:return Elements.CROSS;
        }
        throw new IllegalStateException("Чето не так с линией");
    }

    @Override
    public String toString() {
        return String.format("[%s=%s]", super.toString(), state().name());
    }

    public Point to() {
        Point result = copy();
        result.change(state().to());
        return result;
    }

    public Point from() {
        Point result = copy();
        result.change(state().from());
        return result;
    }

    public Point pipeFrom(Point start) {
        if (type == Type.CROSS) {
            int dx = start.getX() - this.getX();
            int dy = start.getY() - this.getY();

            return pt(start.getX() - dx*2, start.getY() - dy*2);
        }

        Point fromCurrent = from();
        Point toCurrent = to();
        if (fromCurrent.equals(start)) {
            return toCurrent;
        } else if (toCurrent.equals(start)) {
            return fromCurrent;
        } else {
            return null;
        }
    }

    public void rotate() {
        direction = direction.clockwise();
    }

    public Type getType() {
        return type;
    }

}
