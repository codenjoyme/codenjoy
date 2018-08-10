package com.codenjoy.dojo.puzzlebox.model;

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


import com.codenjoy.dojo.puzzlebox.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.joystick.DirectionActJoystick;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;

import java.util.LinkedList;
import java.util.List;

public class Player extends GamePlayer<Hero, Field> {

    Hero hero;

    public Player(EventListener listener) {
        super(listener);
    }

    @Override
    public Hero getHero() {
        return hero;
    }

    @Override
    public void newHero(Field field) {
        hero = new Hero();
        hero.init(field);
        hero.init(this);
    }

    @Override
    public boolean isAlive() {
        return true;
    }

}
