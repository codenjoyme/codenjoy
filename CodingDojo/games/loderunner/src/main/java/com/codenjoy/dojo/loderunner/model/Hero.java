package com.codenjoy.dojo.loderunner.model;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

public class Hero extends PlayerHero<Field> implements State<Elements, Player> {

    private Direction direction;
    private boolean moving;
    private boolean drill;
    private boolean drilled;
    private boolean alive;
    private boolean jump;

    public Hero(Point xy, Direction direction) {
        super(xy);
        this.direction = direction;
        moving = false;
        drilled = false;
        drill = false;
        alive = true;
        jump = false;
    }

    @Override
    public void down() {
        if (!alive) return;

        if (field.isLadder(x, y) || field.isLadder(x, y - 1)) {
            direction = Direction.DOWN;
            moving = true;
        } else if (field.isPipe(x, y)) {
            jump = true;
        }
    }

    @Override
    public void up() {
        if (!alive) return;

        if (field.isLadder(x, y)) {
            direction = Direction.UP;
            moving = true;
        }
    }

    @Override
    public void left() {
        if (!alive) return;

        drilled = false;
        direction = Direction.LEFT;
        moving = true;
    }

    @Override
    public void right() {
        if (!alive) return;

        drilled = false;
        direction = Direction.RIGHT;
        moving = true;
    }

    @Override
    public void act(int... p) {
        if (!alive) return;

        if (p.length == 1 && p[0] == 0) { // TODO test me
            alive = false;
            return;
        }

        drill = true;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (isFall()) {
            move(x, y - 1);
        } else if (drill) {
            int dx = direction.changeX(x);
            int dy = y - 1;
            drilled = field.tryToDrill(this, dx, dy);
        } else if (moving || jump) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);

            if (jump) {
                newX = x;
                newY = y - 1;
            }

            if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
            }
        }
        drill = false;
        moving = false;
        jump = false;
    }

    public boolean isAlive() {
        if (alive) {
            checkAlive();
        }
        return alive;
    }

    private void checkAlive() {
        if (field.isFullBrick(x, y) || field.isEnemyAt(x, y)) {
            alive = false;
        }
    }

    public boolean isFall() {
        return field.isPit(x, y) && !field.isPipe(x, y) && !field.isLadder(x, y);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Elements state = state(alsoAtPoint);
        if (player.getHero() == this) {
            return state;
        } else {
            return Elements.forOtherHero(state);
        }
    }

    private Elements state(Object[] alsoAtPoint) {
        Ladder ladder = null;
        Pipe pipe = null;
        Object el = alsoAtPoint[1];
        if (el != null) {
            if (el instanceof Ladder) {
                ladder = (Ladder) el;
            } else if (el instanceof Pipe) {
                pipe = (Pipe)el;
            }
        }

        if (!alive) {
            return Elements.HERO_DIE;
        }

        if (ladder != null) {
            return Elements.HERO_LADDER;
        }

        if (pipe != null) {
            if (direction.equals(Direction.LEFT)) {
                return Elements.HERO_PIPE_LEFT;
            } else {
                return Elements.HERO_PIPE_RIGHT;
            }
        }

        if (drilled) {
            if (direction.equals(Direction.LEFT)) {
                return Elements.HERO_DRILL_LEFT;
            } else {
                return Elements.HERO_DRILL_RIGHT;
            }
        }

        if (isFall()) {
            if (direction.equals(Direction.LEFT)) {
                return Elements.HERO_FALL_LEFT;
            } else {
                return Elements.HERO_FALL_RIGHT;
            }
        }

        if (direction.equals(Direction.LEFT)) {
            return Elements.HERO_LEFT;
        } else {
            return Elements.HERO_RIGHT;
        }
    }
}
