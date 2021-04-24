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
import com.codenjoy.dojo.services.round.RoundGamePlayer;
import com.codenjoy.dojo.snakebattle.model.board.Field;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.services.GameSettings;

public class Player extends RoundGamePlayer<Hero, Field> {

    public Player(EventListener listener, GameSettings settings) {
        super(listener, settings);
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
        hero.setPlayer(this);
    }

    public void start(int round, Object startEvent) {
        super.start(round, startEvent);
        // hero.clear(); TODO test me
    }

    @Override
    public void initHero(Field field) {
        hero = new Hero(field.getFreeStart());
        hero.setPlayer(this);
        hero.init(field);
    }
}
