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

import java.util.List;

public class Enemy extends PointImpl implements Tickable, Fieldable, State<Elements, Player> {

    private Direction direction;
    private EnemyAI ai;
    private Field field;
    private Class<? extends Point> withGold;
    private List<Point> preys;

    public Enemy(Point pt, Direction direction, EnemyAI ai) {
        super(pt);
        withGold = null;
        this.direction = direction;
        this.ai = ai;
    }

    @Override
    public void init(Field field) {
        this.field = field;
    }

    private Point underEnemy() {
        return Direction.DOWN.change(this);
    }

    @Override
    public void tick() {
        preys = field.visibleHeroes();
        
        if (isFall()) {
            if (field.isBrick(underEnemy()) && withGold != null) {
                // TODO герой не может оставить золото, если он залез в ямку под лестницу, золото должно появиться сбоку
                field.leaveGold(this, withGold);
                withGold = null;
            }
            move(x, y - 1);
        } else if (field.isBrick(this)) {
            if (field.isFullBrick(this)) {
                move(Direction.UP.change(this));
            }
        } else {
            Direction direction = ai.getDirection(field, this, preys);
            if (direction == null) {
                return;
            }

            if (direction == Direction.UP && !field.isLadder(this)) return;

            if (direction != Direction.DOWN) {
                this.direction = direction;
            }
            Point pt = direction.change(this);

            // чертик чертику не помеха - пусть проходят друг сквозь друга
            // if (field.isEnemyAt(pt.getX(), pt.getY())) return;

            if (!field.isHeroAt(pt)
                    && field.isBarrier(pt)) return;

            move(pt);
        }
    }

    public boolean isFall() {
        return !field.isBrick(this)
                && (field.isHeroAt(underEnemy())
                    || field.isPit(this))
                && !field.isPipe(this)
                && !field.isLadder(this);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (field.isBrick(this)) {
            return Elements.ENEMY_PIT;
        }

        if (field.isLadder(this)) {
            return Elements.ENEMY_LADDER;
        }

        if (field.isPipe(this)) {
            return isLeftTurn()
                    ? Elements.ENEMY_PIPE_LEFT
                    : Elements.ENEMY_PIPE_RIGHT;
        }

        return isLeftTurn()
                ? Elements.ENEMY_LEFT
                : Elements.ENEMY_RIGHT;
    }

    public boolean isLeftTurn() {
        return direction.equals(Direction.LEFT);
    }

    public void getGold(Class<? extends Point> clazz) {
        withGold = clazz;
    }

    public boolean withGold() {
        return withGold != null;
    }

}
