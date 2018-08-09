package com.codenjoy.dojo.bomberman.model;

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


import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

public class Bomb extends PointImpl implements Tickable, State<Elements, Player> {
    protected int timer = 5;
    protected int power;
    private Hero owner;
    private Field field;

    public Bomb(Hero owner, int x, int y, int power, Field field) {
        super(x, y);
        this.power = power;
        this.owner = owner;
        this.field = field;
    }

    public void tick() {
        timer--;
        if (timer == 0) {
            boom();
        }
    }

    private void boom() {
        field.removeBomb(this);
    }

    public int getTimer() {
        return timer;
    }

    public int getPower() {
        return power;
    }

    public boolean isExploded() {
        return timer == 0;
    }

    public boolean itsMine(Hero bomberman) {
        return this.owner == bomberman;
    }

    public Hero getOwner() {
        return owner;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        switch (timer) {
            case 1 : return Elements.BOMB_TIMER_1;
            case 2 : return Elements.BOMB_TIMER_2;
            case 3 : return Elements.BOMB_TIMER_3;
            case 4 : return Elements.BOMB_TIMER_4;
            case 5 : return Elements.BOMB_TIMER_5;
            default : return Elements.BOOM;
        }
    }
}
