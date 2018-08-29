package com.codenjoy.dojo.spacerace.model;

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

    private boolean alive;
    private Direction direction;
    private boolean fire;
    private int deadCount = 0;
    private BulletCharger charger;

    public Hero(Point xy, BulletCharger charger) {
        super(xy);
        direction = null;
        fire = false;
        alive = true;
        this.charger = charger;
    }

    @Override
    public void down() {
        if (!alive) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive) return;

        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        if (!alive) return;

        direction = Direction.RIGHT;
    }

    @Override
    public void act(int... p) {
        if (p.length == 1 && p[0] == 0) {
            die();
        }

        if (!alive) return;

        fire = true;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        charger.tick();

        if (!alive) {
            if(deadCount > 1){
                alive = true;
                deadCount = 0;
                return;
            }
            deadCount++;
            return;
        }

        if (fire){
            if (charger.canShoot()) {
                field.addBullet(this.getX(), this.getY(), this);
            }
            fire = false;
        }

        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);

            if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
            }
        }
        direction = null;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die(){
        deadCount++;
        alive = false;
    }
    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (!isAlive()) {
            return Elements.DEAD_HERO;
        }

        if (this == player.getHero()) {
            return Elements.HERO;
        } else {
            return Elements.OTHER_HERO;
        }
    }

    // TODO потестить кейз, когда игрок уже погиб, а ему за его снаряды начисляются очки - не должны начисляться
    public void renew(Point pt, BulletCharger charger) {
        this.charger = charger;
        alive = true;
        move(pt);
    }

    public void recharge() {
        charger.setToRecharge(true);
    }
}
