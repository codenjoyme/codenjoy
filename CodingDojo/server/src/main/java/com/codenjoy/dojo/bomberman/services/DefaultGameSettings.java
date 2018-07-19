package com.codenjoy.dojo.bomberman.services;

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


import static com.codenjoy.dojo.services.settings.SimpleParameter.s;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

import com.codenjoy.dojo.bomberman.model.GameSettings;
import com.codenjoy.dojo.bomberman.model.Hero;
import com.codenjoy.dojo.bomberman.model.HeroImpl;
import com.codenjoy.dojo.bomberman.model.Level;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.settings.Parameter;

/**
 * User: oleksandr.baglai
 * Date: 3/11/13
 * Time: 6:41 PM
 */
public class DefaultGameSettings implements GameSettings {

    public static final int MEAT_CHOPPERS_COUNT = 10;
    public static final int BOMB_POWER = 3;
    public static final int BOMBS_COUNT = 1;
    public static final int DESTROY_WALL_COUNT = 10;
    public static final String MAP_FILE = "level1";

    @Override
    public Parameter<String> getMapFile() {
        return s("level1");
    }

    @Override
    public Hero getBomberman(Level level) {
        return new HeroImpl(level, new RandomDice());
    }

    @Override
    public Parameter<Integer> getBombsCount() {
        return v(BOMBS_COUNT);
    }

    @Override
    public Parameter<Integer> getBombsPower() {
        return v(BOMB_POWER);
    }

    @Override
    public Parameter<Integer> getDestroyableWalls() {
        return v(DESTROY_WALL_COUNT);
    }

    @Override
    public Parameter<Integer> getMeatChoppersCount() {
        return v(MEAT_CHOPPERS_COUNT);
    }

}
