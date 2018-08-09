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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.LinkedList;
import java.util.List;

public class Tank extends PlayerHero<Field> implements State<Elements, Player> {

    protected Dice dice;
    private List<Bullet> bullets;
    private boolean alive;
    private Gun gun;

    protected Direction direction;
    protected int speed;
    protected boolean moving;

    public Tank(int x, int y, Direction direction, Dice dice, int ticksPerBullets) {
        super(x, y);
        speed = 1;
        moving = false;
        this.direction = direction;
        gun = new Gun(ticksPerBullets);
        bullets = new LinkedList<Bullet>();
        alive = true;
        this.dice = dice;
    }

    void turn(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void up() {
        direction = Direction.UP;
        moving = true;
    }

    @Override
    public void down() {
        direction = Direction.DOWN;
        moving = true;
    }

    @Override
    public void right() {
        direction = Direction.RIGHT;
        moving = true;
    }

    @Override
    public void left() {
        direction = Direction.LEFT;
        moving = true;
    }

    public Direction getDirection() {
        return direction;
    }

    public void move() {
        for (int i = 0; i < speed; i++) {
            if (!moving) {
                return;
            }

            int newX = direction.changeX(x);
            int newY = direction.changeY(y);
            moving(newX, newY);
        }
    }

    public void moving(int newX, int newY) {
        if (field.isBarrier(newX, newY)) {
            // do nothing
        } else {
            x = newX;
            y = newY;
        }
        moving = false;
    }

    @Override
    public void act(int... p) {
        if (gun.tryToFire()) {
            Bullet bullet = new Bullet(field, direction, copy(), this, new OnDestroy() {
                @Override
                public void destroy(Object bullet) {
                    Tank.this.bullets.remove(bullet);
                }
            });
            if (!bullets.contains(bullet)) {
                bullets.add(bullet);
            }
        }
    }

    public Iterable<Bullet> getBullets() {
        return new LinkedList<Bullet>(bullets);
    }

    public void init(Field field) {
        super.init(field);

        int xx = x;
        int yy = y;
        while (field.isBarrier(xx, yy)) {
            xx = dice.next(field.size());
            yy = dice.next(field.size());
        }
        x = xx;
        y = yy;
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
}
