package com.codenjoy.dojo.battlecity.model;

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


import com.codenjoy.dojo.battlecity.model.items.Bullet;
import com.codenjoy.dojo.battlecity.model.items.Tree;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class Tank extends PlayerHero<Field> implements State<Elements, Player> {

    public static final int MAX = 100;
    protected Dice dice;
    private List<Bullet> bullets;
    private boolean alive;
    private Gun gun;

    protected Direction direction;
    protected boolean moving;
    private boolean fire;

    private Sliding sliding;

    public Tank(Point pt, Direction direction, Dice dice, int ticksPerBullets) {
        super(pt);
        this.direction = direction;
        this.dice = dice;
        gun = new Gun(ticksPerBullets);
        reset();
    }

    void turn(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void up() {
        if (alive) {
            direction = Direction.UP;
            moving = true;
        }
    }

    @Override
    public void down() {
        if (alive) {
            direction = Direction.DOWN;
            moving = true;
        }
    }

    @Override
    public void right() {
        if (alive) {
            direction = Direction.RIGHT;
            moving = true;
        }
    }

    @Override
    public void left() {
        if (alive) {
            direction = Direction.LEFT;
            moving = true;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    // TODO подумать как устранить дублирование с MovingObject
    public void move() {
        if (!moving) {
            return;
        }
        direction = sliding.act(this);
        moving(direction.change(this));
    }

    public void moving(Point pt) {
        if (field.isBarrier(pt)) {
            // do nothing
        } else {
            move(pt);
        }
        moving = false;
    }

    @Override
    public void act(int... p) {
        if (alive) {
            fire = true;
        }
    }

    public Collection<Bullet> getBullets() {
        return new LinkedList<>(bullets);
    }

    public void init(Field field) {
        super.init(field);

        sliding = new Sliding(field);

        int c = 0;
        Point pt = this;
        while (field.isBarrier(pt) && c++ < MAX) {
            pt = PointImpl.random(dice, field.size());
        }
        if (c >= MAX) {
            alive = false;
            return;
        }
        move(pt);
        alive = true;
    }

    public void kill(Bullet bullet) {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void removeBullets() {
        bullets.clear();
    }

    @Override
    public void tick() {
        gun.tick();
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Tree tree = filterOne(alsoAtPoint, Tree.class);
        if (tree != null) {
            return Elements.TREE;
        }

        if (isAlive()) {
            if (player.getHero() == this) {
                switch (direction) {
                    case LEFT:  return Elements.TANK_LEFT;
                    case RIGHT: return Elements.TANK_RIGHT;
                    case UP:    return Elements.TANK_UP;
                    case DOWN:  return Elements.TANK_DOWN;
                    default:    throw new RuntimeException("Неправильное состояние танка!");
                }
            } else {
                switch (direction) {
                    case LEFT:  return Elements.OTHER_TANK_LEFT;
                    case RIGHT: return Elements.OTHER_TANK_RIGHT;
                    case UP:    return Elements.OTHER_TANK_UP;
                    case DOWN:  return Elements.OTHER_TANK_DOWN;
                    default:    throw new RuntimeException("Неправильное состояние танка!");
                }
            }
        } else {
            return Elements.BANG;
        }
    }

    public void reset() {
        moving = false;
        fire = false;
        alive = true;
        gun.reset();
        bullets = new LinkedList<>();
    }

    public void tryFire() {
        if (!fire) return;
        fire = false;

        if (!gun.tryToFire()) return;

        Bullet bullet = new Bullet(field, direction, copy(), this,
                b -> Tank.this.bullets.remove(b));

        if (!bullets.contains(bullet)) {
            bullets.add(bullet);
        }
    }

    protected boolean isTankPrize() {
        return false;
    }
}
