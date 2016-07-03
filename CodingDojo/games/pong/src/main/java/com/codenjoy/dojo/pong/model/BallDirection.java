package com.codenjoy.dojo.pong.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

import java.util.Objects;

public class BallDirection {

    private Direction horizontalDirection;
    private Direction verticalDirection;

    public BallDirection(Direction simpleDirection) {
        this.horizontalDirection = simpleDirection;
        this.verticalDirection = simpleDirection;
    }

    public BallDirection(Direction horizontalDirection, Direction verticalDirection) {
        this.horizontalDirection = horizontalDirection;
        this.verticalDirection = verticalDirection;
    }

    public BallDirection reflectedFrom(Barrier barier) {
        BallDirection result = this;
        if (verticalDirection == horizontalDirection) {
            return new BallDirection(horizontalDirection.inverted(), verticalDirection.inverted());
        }

        switch (barier.getOrientation()) {
            case HORISONTAL: {
                result = new BallDirection(horizontalDirection, verticalDirection.inverted());
                break;
            }
            case VERTICAL: {
                result = new BallDirection(horizontalDirection.inverted(), verticalDirection);
                break;
            }
        }
        return result;
    }


    public int changeX(int x) {
        if (verticalDirection == horizontalDirection) {
            return verticalDirection.changeX(x);
        } else {
            return horizontalDirection.changeX(verticalDirection.changeX(x));
        }
    }

    public int changeY(int y) {
        if (verticalDirection == horizontalDirection) {
            return verticalDirection.changeY(y);
        } else {
            return horizontalDirection.changeY(verticalDirection.changeY(y));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BallDirection that = (BallDirection) o;
        return Objects.equals(horizontalDirection, that.horizontalDirection) &&
                Objects.equals(verticalDirection, that.verticalDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(horizontalDirection, verticalDirection);
    }

    public Direction getHorizontalDirection() {
        return horizontalDirection;
    }

    public Direction getVerticalDirection() {
        return verticalDirection;
    }

}
