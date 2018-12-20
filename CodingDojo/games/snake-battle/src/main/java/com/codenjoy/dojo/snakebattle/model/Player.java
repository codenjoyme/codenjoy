package com.codenjoy.dojo.snakebattle.model;

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


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.snakebattle.model.board.Field;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.services.Events;

public class Player extends GamePlayer<Hero, Field> {

    private Hero hero;

    public Player(EventListener listener) {
        super(listener);
    }

    public void event(Events event) {
        switch (event) {
            case START:
                start();
                break;
        }

        super.event(event);
    }

    private void start() {
        hero.setActive(true);
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void newHero(Field field) {
        hero = new Hero(field.getFreeStart());
        hero.init(field);
    }

    public boolean isAlive() {
        return hero != null && isActive() && hero.isAlive();
    }

    public boolean isActive() {
        return hero != null && hero.isActive();
    }
}
