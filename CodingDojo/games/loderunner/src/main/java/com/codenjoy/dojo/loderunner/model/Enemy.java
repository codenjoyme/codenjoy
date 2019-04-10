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

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Enemy extends PointImpl implements Tickable, Fieldable, State<Elements, Player> {

    private Direction direction;
    private EnemyAI ai;
    private Field field;
    private boolean withGold;
    private Hero huntHim;
    private Hero oldHurt;

    public Enemy(Point pt, Direction direction, EnemyAI ai) {
        super(pt);
        withGold = false;
        this.direction = direction;
        this.ai = ai;
    }

    @Override
    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void tick() {
        if (huntHim == null || !huntHim.isAlive()) {
            List<Hero> heroes = new LinkedList<>(field.getHeroes());
            if (oldHurt != null) { // если я бегал за героем, который спрятался
                heroes.remove(oldHurt); // исключаю его из поиска // TODO подумать, тут может быть кейс, когда герой один и он появился уже а я за ним бегать не могу
            }
            if (heroes.size() != 0) { // если осталось за кем гоняться
                huntHim = heroes.get(new Random().nextInt(heroes.size())); // попробуем
                oldHurt = null;
            }
        }

        if (isFall()) {
            if (field.isBrick(x, y - 1) && withGold) {
                withGold = false;
                // TODO герой не может оставить золото, если он залез в ямку под лестницу, золото должно появиться сбоку
                field.leaveGold(x, y);
            }
            move(x, y - 1);
        } else if (field.isBrick(x, y)) {
            if (field.isFullBrick(x, y)) {
                move(Direction.UP.change(this));
            }
        } else {
            Direction direction = ai.getDirection(field, huntHim, this);
            if (direction == null) {
                oldHurt = huntHim;
                huntHim = null;
                return;
            }

            if (direction == Direction.UP && !field.isLadder(x, y)) return;

            if (direction != Direction.DOWN) {
                this.direction = direction;
            }
            Point pt = direction.change(this);

//            if (field.isEnemyAt(pt.getX(), pt.getY())) return; // чертик чертику не помеха - пусть проходять друг сквозь друга

            if (!field.isHeroAt(pt.getX(), pt.getY())
                    && field.isBarrier(pt.getX(), pt.getY())) return;

            move(pt);
        }
    }

    public boolean isFall() {
        return !field.isBrick(x, y)
                && (field.isHeroAt(x, y - 1) || field.isPit(x, y)) && !field.isPipe(x, y) && !field.isLadder(x, y);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (field.isBrick(x, y)) {
            return Elements.ENEMY_PIT;
        }

        if (field.isLadder(x, y)) {
            return Elements.ENEMY_LADDER;
        }

        if (field.isPipe(x, y)) {
            if (direction.equals(Direction.LEFT)) {
                return Elements.ENEMY_PIPE_LEFT;
            } else {
                return Elements.ENEMY_PIPE_RIGHT;
            }
        }

        if (direction.equals(Direction.LEFT)) {
            return Elements.ENEMY_LEFT;
        }
        return Elements.ENEMY_RIGHT;
    }

    public void getGold() {
        withGold = true;
    }

    public boolean withGold() {
        return withGold;
    }

}
