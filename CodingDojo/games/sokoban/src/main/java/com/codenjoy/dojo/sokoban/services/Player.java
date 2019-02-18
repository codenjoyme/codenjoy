package com.codenjoy.dojo.sokoban.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.sokoban.model.items.Field;
import com.codenjoy.dojo.sokoban.model.itemsImpl.Hero;
import com.codenjoy.dojo.sokoban.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * Класс игрока. Тут кроме героя может подсчитываться очки.
 * Тут же ивенты передабтся лиснеру фреймворка.
 */
public class Player extends GamePlayer<Hero, Field> {
    Logger log = getLogger(Player.class.getName());

    public Hero hero;
    private String name;

    public Player(EventListener listener, String name) {
        super(listener);
        this.name = name;
        if (!Storage.levels.containsKey(name)){
            Storage.levels.put(name,1);}
    }

    public void event(Events event) {
        switch (event) {
            case LOOSE:
                break;
            case WIN:
                increaseLevel(1);
                break;
        }

        super.event(event);
    }

    public Hero getHero() {
        return hero;
    }

    @Override
    public void newHero(Field field) {
        hero = new Hero(field.getFreeRandom());
        hero.init(field);
    }

    @Override
    public boolean isAlive() {
        return hero != null && hero.isAlive();
    }



    private void increaseLevel(int value) {
        int lvl = Storage.levels.get(this.name);
        if (lvl<=(Storage.MAX_VALUE-value)){
        Storage.levels.put(this.name,lvl+value);
        log.log(Level.INFO, String.format("reached lvl:%d\t max limit:%d", lvl + value, Storage.MAX_VALUE));
        }
        else {
            log.log(Level.WARNING,"reached max limit: {}",Storage.MAX_VALUE);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}