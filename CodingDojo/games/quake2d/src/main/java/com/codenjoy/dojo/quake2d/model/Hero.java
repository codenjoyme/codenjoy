package com.codenjoy.dojo.quake2d.model;

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

import com.codenjoy.dojo.quake2d.client.ai.AISolver;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

public class Hero extends PlayerHero<Field> implements State<Elements, Player> {

    public static final int ABILITY_LIFE_TIME = 10;
    public static final int START_HEALTH = 100;
    public static final int DEFFENCE_MULTIPLICATOR = 2;
    public static final int FREAQEUNCY_SHUT = 7;

    private boolean alive;
    private Direction direction;
    private Direction previousDirection;
    private int deathTimeCounter;
    private Ability ability;
    private int health;
    private int counterBeetwenShut = 0;
    private int abilityCounter;

    public Hero(Point xy) {
        super(xy);
        direction = null;
        alive = true;
        this.deathTimeCounter = 0;
        this.abilityCounter = 0;
        this.health = START_HEALTH;
    }

    public void setAlive(boolean pAlive) {
        alive = pAlive;
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        if (!alive) return;

        direction = Direction.UP; // TODO continue flip up/down
    }

    @Override
    public void up() {
        if (!alive) return;

        direction = Direction.DOWN; // TODO continue flip up/down
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

    public Direction getPreviousDirection() {
        return previousDirection;
    }

    @Override
    public void act(int... p) {
        if (!alive) return;
        if (p.length != 0 && p[0] >= AISolver.SHIFT_COMMAND){
            direction = Direction.getValues().get(p[0] - AISolver.SHIFT_COMMAND);
            tick();
        }
        if (counterBeetwenShut == 0){
            field.fireBullet(x, y, previousDirection, field, this);
            counterBeetwenShut = FREAQEUNCY_SHUT;
        }
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.inverted().changeY(y);

//            if (field.isBomb(newX, newY)) {
//                alive = false;
//                field.removeBomb(newX, newY);
//            }

            if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
            }
        }
        previousDirection = direction;
        direction = null;
        if (counterBeetwenShut > 0) {
            counterBeetwenShut--;
        }
        if (abilityCounter != 0 && --abilityCounter == 0){
            ability = null;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (!isAlive()) {
            return Elements.DEAD_HERO;
        }

        if (this == player.getHero()) {
            return Elements.HERO;
        } else if (this.getAbility() != null){
            return Elements.SUPER_OTHER_HERO;
        } else {
            return Elements.OTHER_HERO;
        }
    }

    public int getDeathTimeCounter() {
        return deathTimeCounter;
    }

    public void setDeathTimeCounter(int pDeathTimeCounter) {
        deathTimeCounter = pDeathTimeCounter;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability pAbility) {
        if (pAbility.getAbilityType() == Ability.Type.HEALTH){
            health = Math.min(START_HEALTH, health+Ability.HEALTH_BONUS);
        } else {
            ability = pAbility;
            abilityCounter = ABILITY_LIFE_TIME;
        }
    }

    public int getAbilityCounter() {
        return abilityCounter;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int pHealth) {
        health += pHealth;
    }

    public void setDamage(int pDamage) {
        health -= ((ability != null && ability.getAbilityType() == Ability.Type.DEFENCE) ? pDamage/DEFFENCE_MULTIPLICATOR : pDamage);
        health = Math.max(0, health);
    }
}
