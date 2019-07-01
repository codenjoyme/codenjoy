package com.codenjoy.dojo.excitebike.model.items.bike;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import java.util.Objects;

/**
 * Created by Pavel Bobylev 6/27/2019
 */
public class Movement {

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
