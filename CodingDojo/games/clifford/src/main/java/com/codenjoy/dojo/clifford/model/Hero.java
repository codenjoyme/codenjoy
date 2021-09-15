package com.codenjoy.dojo.clifford.model;

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


import com.codenjoy.dojo.games.clifford.Element;
import com.codenjoy.dojo.clifford.model.items.Ladder;
import com.codenjoy.dojo.clifford.model.items.Potion.PotionType;
import com.codenjoy.dojo.clifford.model.items.Pipe;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.StateUtils;
import com.codenjoy.dojo.services.round.RoundPlayerHero;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.codenjoy.dojo.games.clifford.Element.*;
import static com.codenjoy.dojo.clifford.services.GameSettings.Keys.MASK_TICKS;
import static com.codenjoy.dojo.services.Direction.DOWN;
import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class Hero extends RoundPlayerHero<Field> implements State<Element, Player> {

    protected Direction direction;
    private Map<PotionType, Integer> potions = new HashMap<>();
    private boolean moving;
    private boolean crack;
    private boolean cracked;
    private boolean jump;
    private int score;

    public Hero(Point xy, Direction direction) {
        super(xy);
        this.direction = direction;
        score = 0;
        moving = false;
        cracked = false;
        crack = false;
        jump = false;
    }

    @Override
    public void init(Field field) {
        super.init(field);

        field.heroes().add(this);
    }

    @Override
    public void down() {
        if (!isActiveAndAlive()) return;

        if (field.isLadder(this) || field.isLadder(underHero())) {
            direction = DOWN;
            moving = true;
        } else if (field.isPipe(this)) {
            jump = true;
        }
    }

    @Override
    public void up() {
        if (!isActiveAndAlive()) return;

        if (field.isLadder(this)) {
            direction = Direction.UP;
            moving = true;
        }
    }

    @Override
    public void left() {
        if (!isActiveAndAlive()) return;

        cracked = false;
        direction = Direction.LEFT;
        moving = true;
    }

    @Override
    public void right() {
        if (!isActiveAndAlive()) return;

        cracked = false;
        direction = Direction.RIGHT;
        moving = true;
    }

    @Override
    public void act(int... p) {
        if (!isActiveAndAlive()) return;

        if (p.length == 1 && p[0] == 0) {
            die();
            field.suicide(this);
            return;
        }

        crack = true;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void tick() {
        if (!isActiveAndAlive()) return;

        if (isFall()) {
            move(DOWN);
        } else if (crack) {
            Point hole = DOWN.change(direction.change(this));
            cracked = field.tryToCrack(this, hole);
        } else if (moving || jump) {
            Point dest;
            if (jump) {
                dest = DOWN.change(this);
            } else {
                dest = direction.change(this);
            }

            boolean noPhysicalBarrier = !field.isBarrier(dest);
            boolean victim = isRegularPlayerAt(dest) && isMask();
            if (noPhysicalBarrier || victim) {
                move(dest);
            }
        }
        crack = false;
        moving = false;
        jump = false;
        dissolvePotions();
    }

    private boolean isMask() {
        return under(PotionType.MASK_POTION);
    }

    private boolean isRegularPlayerAt(Point pt) {
        return field.isHeroAt(pt)
                && !field.under(pt, PotionType.MASK_POTION);
    }

    private void dissolvePotions() {
        Set<PotionType> active = potions.keySet();
        for (PotionType potion : active) {
            int ticksLeft = potions.get(potion);
            ticksLeft--;
            if (ticksLeft < 0) {
                potions.remove(potion);
            } else {
                potions.put(potion, ticksLeft);
            }
        }
    }

    @Override
    public boolean isAlive() {
        if (super.isAlive()) {
            checkAlive(); // TODO точно это надо делать в геттере?
        }
        return super.isAlive();
    }

    public void increaseScore() {
        score++;
    }

    public void clearScores() {
        score = 0;
        potions.clear();
    }

    @Override
    public int scores() {
        return score;
    }

    public boolean isVisible() {
        return !under(PotionType.MASK_POTION);
    }

    public boolean under(PotionType potion) {
        return potions.containsKey(potion);
    }

    public void pick(PotionType potion) {
        potions.put(potion, settings().integer(MASK_TICKS));
    }

    private void checkAlive() {
        // TODO: перепроверить. Кажется, где-то проскакивает ArrayIndexOutOfBoundsException
        boolean killedByRobber = field.isRobberAt(this) && !isMask() && super.isActive(); // TODO test me
        if (field.isFullBrick(this) || killedByRobber) {
            die();
        }
    }

    public boolean isFall() {
        return (field.isPit(this) && !field.isPipe(this) && !field.isLadder(this))
            || (isMask() && isRegularPlayerAt(underHero()));
    }

    private Point underHero() {
        return DOWN.change(this);
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (StateUtils.itsMe(player, this, alsoAtPoint, player.getHero())) {
            Hero hero = player.getHero();
            Element state = hero.state(alsoAtPoint);
            return hero.isMask()
                    ? state.mask()
                    : state;
        } else {
            Element state = state(alsoAtPoint);
            state = state.otherHero();
            return isMask()
                    ? state.mask()
                    : state;
        }
    }

    private Element state(Object[] alsoAtPoint) {
        Ladder ladder = filterOne(alsoAtPoint, Ladder.class);
        Pipe pipe = filterOne(alsoAtPoint, Pipe.class);

        if (!isAlive() || !isActive()) {
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

        if (cracked) {
            return isLeftTurn()
                    ? HERO_CRACK_LEFT
                    : HERO_CRACK_RIGHT;
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