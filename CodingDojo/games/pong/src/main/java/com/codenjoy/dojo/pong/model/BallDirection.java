package com.codenjoy.dojo.pong.model;

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
