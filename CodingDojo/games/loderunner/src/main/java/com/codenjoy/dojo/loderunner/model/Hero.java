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


import com.codenjoy.dojo.loderunner.model.Pill.PillType;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.StateUtils;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.codenjoy.dojo.loderunner.model.Elements.*;
import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.SHADOW_TICKS;
import static com.codenjoy.dojo.services.Direction.DOWN;
import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class Hero extends PlayerHero<Field> implements State<Elements, Player> {

    private Direction direction;
    private Map<PillType, Integer> pills = new HashMap<>();
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

        if (field.isLadder(this) || field.isLadder(underHero())) {
            direction = DOWN;
            moving = true;
        } else if (field.isPipe(this)) {
            jump = true;
        }
    }

    @Override
    public void up() {
        if (!alive) return;

        if (field.isLadder(this)) {
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

        if (p.length == 1 && p[0] == 0) {
            alive = false;
            field.suicide(this);
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
            move(DOWN);
        } else if (drill) {
            Point hole = DOWN.change(direction.change(this));
            drilled = field.tryToDrill(this, hole);
        } else if (moving || jump) {
            Point dest;
            if (jump) {
                dest = DOWN.change(this);
            } else {
                dest = direction.change(this);
            }

            boolean noPhysicalBarrier = !field.isBarrier(dest);
            boolean victim = isRegularPlayerAt(dest) && isShadow();
            if (noPhysicalBarrier || victim) {
                move(dest);
            }
        }
        drill = false;
        moving = false;
        jump = false;
        dissolvePills();
    }

    private boolean isShadow() {
        return under(PillType.SHADOW_PILL);
    }

    private boolean isRegularPlayerAt(Point pt) {
        return field.isHeroAt(pt)
                && !field.under(pt, PillType.SHADOW_PILL);
    }

    private void dissolvePills() {
        Set<PillType> active = pills.keySet();
        for (PillType pill : active) {
            int ticksLeft = pills.get(pill);
            ticksLeft--;
            if (ticksLeft < 0) {
                pills.remove(pill);
            } else {
                pills.put(pill, ticksLeft);
            }
        }
    }

    public boolean isAlive() {
        if (alive) {
            checkAlive();
        }
        return alive;
    }

    public boolean under(PillType pill) {
        return pills.containsKey(pill);
    }

    public void pick(PillType pill) {
        pills.put(pill, settings().integer(SHADOW_TICKS));
    }

    private void checkAlive() {
        // TODO: перепроверить. Кажется, где-то проскакивает ArrayIndexOutOfBoundsException
        boolean killedByEnemy = field.isEnemyAt(this) && !isShadow();
        if (field.isFullBrick(this) || killedByEnemy) {
            alive = false;
        }
    }

    public boolean isFall() {
        return (field.isPit(this) && !field.isPipe(this) && !field.isLadder(this))
            || (isShadow() && isRegularPlayerAt(underHero()));
    }

    private Point underHero() {
        return DOWN.change(this);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (StateUtils.itsMe(player, this, alsoAtPoint, player.getHero())) {
            Hero hero = player.getHero();
            Elements state = hero.state(alsoAtPoint);
            return hero.isShadow()
                    ? state.shadow()
                    : state;
        } else {
            Elements state = state(alsoAtPoint);
            state = state.otherHero();
            return isShadow()
                    ? state.shadow()
                    : state;
        }
    }

    private Elements state(Object[] alsoAtPoint) {
        Ladder ladder = filterOne(alsoAtPoint, Ladder.class);
        Pipe pipe = filterOne(alsoAtPoint, Pipe.class);

        if (!alive) {
            return HERO_DIE;
        }

        if (ladder != null) {
            return HERO_LADDER;
        }

        if (pipe != null) {
            return isLeftTurn()
                    ? HERO_PIPE_LEFT
                    : HERO_PIPE_RIGHT;
        }

        if (drilled) {
            return isLeftTurn()
                    ? HERO_DRILL_LEFT
                    : HERO_DRILL_RIGHT;
        }

        if (isFall()) {
            return isLeftTurn()
                    ? HERO_FALL_LEFT
                    : HERO_FALL_RIGHT;
        }

        return isLeftTurn()
                ? HERO_LEFT
                : HERO_RIGHT;
    }

    private boolean isLeftTurn() {
        return direction.equals(Direction.LEFT);
    }
}
