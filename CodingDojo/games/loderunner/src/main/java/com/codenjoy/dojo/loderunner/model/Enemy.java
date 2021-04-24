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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class Enemy extends PointImpl implements Tickable, Fieldable, State<Elements, Player> {

    private Direction direction;
    private EnemyAI ai;
    private Field field;
    private Class<? extends Point> withGold;
    private Hero prey;

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

    private Point under(Point pt) {
        return Direction.DOWN.change(pt);
    }

    @Override
    public void tick() {
        if (isFall()) {
            // при падении в ямку оставляем золото
            if (field.isBrick(under(this)) && withGold != null) {
                // TODO герой не может оставить золото, если он залез в ямку под лестницу, золото должно появиться сбоку
                field.leaveGold(this, withGold);
                withGold = null;
            }
            move(x, y - 1);
            return;
        }

        if (field.isBrick(this)) {
            // если ямка заросла, выбираемся
            if (field.isFullBrick(this)) {
                move(Direction.UP.change(this));
            }
            return;
        }

        List<Hero> heroes = field.visibleHeroes();

        // если тот, за км охотились уже ушел с поля или умер, или стал невидимым - будем искать нового
        if (prey == null || !prey.isActiveAndAlive() || prey.isVisible()) {
            prey = null;
        }

        List<Hero> free = getFreePreys(heroes);

        Direction direction = ai.getDirection(field, this, (List)free);
        if (direction == null) {
            prey = null;
            return;
        }
        Point reached = ai.getReached();
        prey = findHero(heroes, reached);

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

    private List<Hero> getFreePreys(List<Hero> all) {
        // у нас уже есть за кем охотиться
        if (prey != null) {
            return Arrays.asList(prey);
        }

        // ищем за кем охотиться
        List<Enemy> enemies = field.enemies();
        // выбираем только тех, за кем еще никто не охотится
        List<Hero> free = all.stream()
                .filter(prey -> enemies.stream()
                        .map(enemy -> enemy.prey())
                        .filter(Objects::nonNull)
                        .noneMatch(escaping -> prey.equals(escaping)))
                .collect(toList());
        // если все заняты, будем бежать за ближайшим
        if (free.isEmpty()) {
            return all;
        }

        return free;
    }

    private Hero findHero(List<Hero> preys, Point reached) {
        return preys.stream()
                .filter(it -> it.equals(reached))
                .findFirst()
                .orElse(null);
    }

    private Hero prey() {
        return prey;
    }

    public boolean isFall() {
        return !field.isBrick(this)
                && (field.isHeroAt(under(this))
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
