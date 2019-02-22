package com.codenjoy.dojo.pong.model;

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

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;

import java.util.Arrays;
import java.util.Objects;

public class BallDirection {

    private QDirection direction;

    public BallDirection(QDirection direction) {
        this.direction = direction;
    }

    public static BallDirection getRandom(Dice dice) {
        return new BallDirection(Arrays.asList(
                    QDirection.LEFT_DOWN, QDirection.LEFT_UP,
                    QDirection.RIGHT_DOWN, QDirection.RIGHT_UP)
                .get(dice.next(4)));
    }

    public BallDirection reflectedFrom(Barrier barrier) {
        switch (barrier.getOrientation()) {
            case HORIZONTAL:
                return new BallDirection(direction.mirrorHorizontal());
            case VERTICAL:
                return new BallDirection(direction.mirrorVertical());
            default:
                throw new IllegalArgumentException("Unknown barrier orientation: " + barrier.getOrientation());
        }
    }

    public Point change(Point pt) {
        return direction.change(pt);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BallDirection)) {
            return false;
        }
      return Objects.equals(direction, ((BallDirection) o).direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction);
    }

    public BallDirection invert() {
        return new BallDirection(direction.inverted());
    }
}
