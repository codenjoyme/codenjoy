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
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class Hero extends PlayerHero<Field> implements State<Elements, Player> {

    private Direction direction;
    private Supplier<Integer> shadowPillTicks;
    private Map<PillType, Integer> activePills = new HashMap<>();
    private boolean moving;
    private boolean drill;
    private boolean drilled;
    private boolean alive;
    private boolean jump;
    private boolean suicide;

    public Hero(Point xy, Direction direction, Supplier<Integer> shadowPillTicks) {
        super(xy);
        this.direction = direction;
        this.shadowPillTicks = shadowPillTicks;
        moving = false;
        drilled = false;
        drill = false;
        alive = true;
        jump = false;
        suicide = false;
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

        if (p.length == 1 && p[0] == 0) {
            alive = false;
            suicide = true;
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

            boolean noPhysicalBarrier = !field.isBarrier(newX, newY);
            boolean victim = isRegularPlayerAt(newX, newY) && iAmTheShadow();
            if (noPhysicalBarrier || victim) {
                move(newX, newY);
            }
        }
        drill = false;
        moving = false;
        jump = false;
        dissolvePills();
    }

    private boolean iAmTheShadow() {
        return this.isUnderThePill(PillType.SHADOW_PILL);
    }

    private boolean isRegularPlayerAt(int x, int y) {
        return field.isHeroAt(x, y)
                && !field.isUnderThePillAt(x, y, PillType.SHADOW_PILL);
    }

    private void dissolvePills() {
        Set<PillType> activePillTypes = activePills.keySet();
        for (PillType activePill : activePillTypes) {
            int ticksLeft = activePills.get(activePill);
            ticksLeft--;
            if (ticksLeft < 0) {
                activePills.remove(activePill);
            } else {
                activePills.put(activePill, ticksLeft);
            }
        }
    }

    public boolean isAlive() {
        if (alive) {
            checkAlive();
        }
        return alive;
    }

    public boolean isSuicide() {
        return suicide;
    }

    public boolean isUnderThePill(PillType pillType) {
        return activePills.containsKey(pillType);
    }

    public void swallowThePill(PillType pillType) {
        activePills.put(pillType, shadowPillTicks.get());
    }

    private void checkAlive() {
        // TODO: перепроверить. Кажется, где-то проскакивает ArrayIndexOutOfBoundsException
        boolean killedByEnemy = field.isEnemyAt(x, y) && !iAmTheShadow();
        if (field.isFullBrick(x, y) || killedByEnemy) {
            alive = false;
        }
    }

    public boolean isFall() {
        return (field.isPit(x, y) && !field.isPipe(x, y) && !field.isLadder(x, y))
            || (iAmTheShadow() && isRegularPlayerAt(x, y - 1));
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
        boolean underKillerPill = iAmTheShadow();
        if (el != null) {
            if (el instanceof Ladder) {
                ladder = (Ladder) el;
            } else if (el instanceof Pipe) {
                pipe = (Pipe) el;
            }
        }

        if (!alive) {
            return Elements.HERO_DIE;
        }

        if (ladder != null) {
            return underKillerPill ? Elements.HERO_SHADOW_LADDER : Elements.HERO_LADDER;
        }

        if (pipe != null) {
            if (direction.equals(Direction.LEFT)) {
                return underKillerPill ? Elements.HERO_SHADOW_PIPE_LEFT : Elements.HERO_PIPE_LEFT;
            } else {
                return underKillerPill ? Elements.HERO_SHADOW_PIPE_RIGHT : Elements.HERO_PIPE_RIGHT;
            }
        }

        if (drilled) {
            if (direction.equals(Direction.LEFT)) {
                return underKillerPill ? Elements.HERO_SHADOW_DRILL_LEFT : Elements.HERO_DRILL_LEFT;
            } else {
                return underKillerPill ? Elements.HERO_SHADOW_DRILL_RIGHT : Elements.HERO_DRILL_RIGHT;
            }
        }

        if (isFall()) {
            if (direction.equals(Direction.LEFT)) {
                return underKillerPill ? Elements.HERO_SHADOW_FALL_LEFT : Elements.HERO_FALL_LEFT;
            } else {
                return underKillerPill ? Elements.HERO_SHADOW_FALL_RIGHT : Elements.HERO_FALL_RIGHT;
            }
        }

        if (direction.equals(Direction.LEFT)) {
            return underKillerPill ? Elements.HERO_SHADOW_LEFT : Elements.HERO_LEFT;
        } else {
            return underKillerPill ? Elements.HERO_SHADOW_RIGHT : Elements.HERO_RIGHT;
        }
    }
}
