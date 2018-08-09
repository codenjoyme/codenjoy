package com.codenjoy.dojo.snake.model.artifacts;

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
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.snake.model.BodyDirection;
import com.codenjoy.dojo.snake.model.Elements;
import com.codenjoy.dojo.snake.model.Hero;

import static com.codenjoy.dojo.snake.model.Elements.*;

public class Tail extends PointImpl implements State<Elements, Object> {

    private Hero snake;

    public Tail(int x, int y, Hero snake) {
        super(x, y);
        this.snake = snake;
    }

    private Elements getTailColor(Direction direction) {
        switch (direction) {
            case DOWN : return TAIL_END_DOWN;
            case UP : return TAIL_END_UP;
            case LEFT : return TAIL_END_LEFT;
            case RIGHT : return TAIL_END_RIGHT;
            default : return NONE;
        }
    }

    private Elements getHead(Direction direction) {
        switch (direction) {
            case DOWN : return HEAD_DOWN;
            case UP : return HEAD_UP;
            case LEFT : return HEAD_LEFT;
            case RIGHT : return HEAD_RIGHT;
            default : return NONE;
        }
    }

    private Elements getBody(BodyDirection bodyDirection) {
        switch (bodyDirection) {
            case HORIZONTAL : return TAIL_HORIZONTAL;
            case VERTICAL : return TAIL_VERTICAL;
            case TURNED_LEFT_DOWN : return TAIL_LEFT_DOWN;
            case TURNED_LEFT_UP : return TAIL_LEFT_UP;
            case TURNED_RIGHT_DOWN : return TAIL_RIGHT_DOWN;
            case TURNED_RIGHT_UP : return TAIL_RIGHT_UP;
            default : return NONE;
        }
    }

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        if (snake.itsMyHead(this)) {
            return getHead(snake.getDirection());
        }

        if (snake.itsMyTail(this)) {
            return getTailColor(snake.getTailDirection());
        }

        return getBody(snake.getBodyDirection(this));
    }
}
