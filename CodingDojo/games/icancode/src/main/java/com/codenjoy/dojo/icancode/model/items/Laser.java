package com.codenjoy.dojo.icancode.model.items;

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


import com.codenjoy.dojo.icancode.model.*;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

public class Laser extends FieldItem implements Tickable {

    private Direction direction;
    private State owner;
    private boolean skip;

    public Laser(Elements element) {
        super(element);
        this.direction = getDirection(element);
    }

    public Laser(State owner, Direction direction) {
        super(getElement(direction));
        this.owner = owner;
        skip = (owner instanceof Hero);
        this.direction = direction;
    }

    private static Elements getElement(Direction direction) {
        switch (direction) {
            case LEFT:
                return Elements.LASER_LEFT;
            case RIGHT:
                return Elements.LASER_RIGHT;
            case DOWN:
                return Elements.LASER_DOWN;
            case UP:
                return Elements.LASER_UP;
        }
        throw new IllegalStateException("Unexpected direction: " + direction);
    }

    private static Direction getDirection(Elements element) {
        switch (element) {
            case LASER_LEFT: return Direction.LEFT;
            case LASER_RIGHT: return Direction.RIGHT;
            case LASER_DOWN: return Direction.DOWN;
            case LASER_UP: return Direction.UP;
        }
        throw new IllegalStateException("Unexpected element: " + element);
    }

    @Override
    public void action(Item item) {
        HeroItem heroItem = getIf(item, HeroItem.class);
        if (heroItem != null) {
            Hero hero = heroItem.getHero();
            if (!hero.isFlying()) {
                die();
                hero.dieOnLaser();
                addOwnerKillHeroScore();
            }
        }

        Zombie zombie = getIf(item, Zombie.class);
        if (zombie != null) {
            die();
            zombie.die();
            addOwnerKillZombieScore();
        }
    }

    private void addOwnerKillZombieScore() {
        if (owner != null && owner instanceof Hero) {
            ((Hero) owner).addZombieKill();
        }
    }

    private void addOwnerKillHeroScore() {
        if (owner != null && owner instanceof Hero) {
            ((Hero) owner).addHeroKill();
        }
    }

    @Override
    public void tick() {
        if (getCell() == null) return; // TODO почему-то тут был NPE
        int newX = direction.changeX(getCell().getX());
        int newY = direction.changeY(getCell().getY());

        if (!field.isBarrier(newX, newY)) {
            field.move(this, newX, newY);
        } else {
            removeFromCell();
        }
    }

    public State owner() {
        return owner;
    }

    public boolean skipFirstTick() {
        if (skip) {
            skip = false;
            return true;
        }
        return false;
    }

    public void die() {
        removeFromCell();
    }
}
