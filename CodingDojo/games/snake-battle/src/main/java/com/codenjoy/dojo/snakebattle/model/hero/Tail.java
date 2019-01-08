package com.codenjoy.dojo.snakebattle.model.hero;

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
import com.codenjoy.dojo.snakebattle.model.Elements;
import com.codenjoy.dojo.snakebattle.model.Player;

import static com.codenjoy.dojo.snakebattle.model.Elements.*;

public class Tail extends PointImpl implements State<Elements, Object> {

    private Hero snake;

    Tail(Point xy, Hero snake) {
        super(xy.getX(), xy.getY());
        this.snake = snake;
    }

    Tail(int x, int y, Hero snake) {
        super(x, y);
        this.snake = snake;
    }

    private Elements getTail(TailDirection direction, boolean itIsMyHero) {
        if (itIsMyHero)
            return getMyTail(direction);
        else
            return getEnemyTail(direction);
    }

    private Elements getMyTail(TailDirection direction) {
        switch (direction) {
            case VERTICAL_DOWN:
                return TAIL_END_DOWN;
            case VERTICAL_UP:
                return TAIL_END_UP;
            case HORIZONTAL_LEFT:
                return TAIL_END_LEFT;
            case HORIZONTAL_RIGHT:
                return TAIL_END_RIGHT;
            default:
                return OTHER;
        }
    }

    private Elements getEnemyTail(TailDirection direction) {
        switch (direction) {
            case VERTICAL_DOWN:
                return ENEMY_TAIL_END_DOWN;
            case VERTICAL_UP:
                return ENEMY_TAIL_END_UP;
            case HORIZONTAL_LEFT:
                return ENEMY_TAIL_END_LEFT;
            case HORIZONTAL_RIGHT:
                return ENEMY_TAIL_END_RIGHT;
            default:
                return OTHER;
        }
    }

    private Elements getHead(Direction direction, boolean itIsMyHero) {
        if (itIsMyHero)
            return getMyHead(direction);
        else
            return getEnemyHead(direction);
    }

    private Elements getMyHead(Direction direction) {
        switch (direction) {
            case DOWN:
                return HEAD_DOWN;
            case UP:
                return HEAD_UP;
            case LEFT:
                return HEAD_LEFT;
            case RIGHT:
                return HEAD_RIGHT;
            default:
                return OTHER;
        }
    }

    private Elements getEnemyHead(Direction direction) {
        switch (direction) {
            case DOWN:
                return ENEMY_HEAD_DOWN;
            case UP:
                return ENEMY_HEAD_UP;
            case LEFT:
                return ENEMY_HEAD_LEFT;
            case RIGHT:
                return ENEMY_HEAD_RIGHT;
            default:
                return OTHER;
        }
    }

    private Elements getBody(BodyDirection bodyDirection, boolean itIsMyHero) {
        if (itIsMyHero)
            return getMyBody(bodyDirection);
        else
            return getEnemyBody(bodyDirection);
    }

    private Elements getMyBody(BodyDirection bodyDirection) {
        switch (bodyDirection) {
            case HORIZONTAL:
                return BODY_HORIZONTAL;
            case VERTICAL:
                return BODY_VERTICAL;
            case TURNED_LEFT_DOWN:
                return BODY_LEFT_DOWN;
            case TURNED_LEFT_UP:
                return BODY_LEFT_UP;
            case TURNED_RIGHT_DOWN:
                return BODY_RIGHT_DOWN;
            case TURNED_RIGHT_UP:
                return BODY_RIGHT_UP;
            default:
                return OTHER;
        }
    }

    private Elements getEnemyBody(BodyDirection bodyDirection) {
        switch (bodyDirection) {
            case HORIZONTAL:
                return ENEMY_BODY_HORIZONTAL;
            case VERTICAL:
                return ENEMY_BODY_VERTICAL;
            case TURNED_LEFT_DOWN:
                return ENEMY_BODY_LEFT_DOWN;
            case TURNED_LEFT_UP:
                return ENEMY_BODY_LEFT_UP;
            case TURNED_RIGHT_DOWN:
                return ENEMY_BODY_RIGHT_DOWN;
            case TURNED_RIGHT_UP:
                return ENEMY_BODY_RIGHT_UP;
            default:
                return OTHER;
        }
    }

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        if (!(player instanceof Player) ||
                ((Player) player).getHero() == null ||
                snake == null)
            return OTHER;
        return snakePart(((Player) player).getHero().equals(snake));
    }

    private Elements snakePart(boolean itIsMyHero) {
        if (snake.itsMyHead(this)) {
            if (snake.isAlive()) {
                if (!snake.isActive())
                    return itIsMyHero ? HEAD_SLEEP : ENEMY_HEAD_SLEEP;
                else if (snake.isFlying())
                    return itIsMyHero ? HEAD_FLY : ENEMY_HEAD_FLY;
                else if (snake.isFury())
                    return itIsMyHero ? HEAD_EVIL : ENEMY_HEAD_EVIL;
                else
                    return getHead(snake.getDirection(), itIsMyHero);
            } else
                return itIsMyHero ? HEAD_DEAD : ENEMY_HEAD_DEAD;
        }
        if (snake.itsMyTail(this))
            if (snake.isActive())
                return getTail(snake.getTailDirection(), itIsMyHero);
            else
                return itIsMyHero ? TAIL_INACTIVE : ENEMY_TAIL_INACTIVE;
        return getBody(snake.getBodyDirection(this), itIsMyHero);
    }
}
