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


import com.codenjoy.dojo.bomberman.model.GameSettings;
import com.codenjoy.dojo.bomberman.model.Hero;
import com.codenjoy.dojo.bomberman.model.HeroImpl;
import com.codenjoy.dojo.bomberman.model.Level;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * User: sanja
 * Date: 26.09.13
 * Time: 9:38
 */
public class OptionGameSettings implements GameSettings {

    private final Parameter<Integer> bombPower;
    private final Parameter<Integer> bombsCount;
    private final Parameter<Integer> destroyWallCount;
    private final Parameter<Integer> meatChoppersCount;
    private final Parameter<String> mapFile;

    public OptionGameSettings(Settings settings) {
        bombsCount = settings.addEditBox("Bombs count").type(Integer.class).def(DefaultGameSettings.BOMBS_COUNT);
        bombPower = settings.addEditBox("Bomb power").type(Integer.class).def(DefaultGameSettings.BOMB_POWER);
        destroyWallCount = settings.addEditBox("Destroy wall count").type(Integer.class).def(DefaultGameSettings.DESTROY_WALL_COUNT);
        meatChoppersCount = settings.addEditBox("Meat choppers count").type(Integer.class).def(DefaultGameSettings.MEAT_CHOPPERS_COUNT);
        mapFile = settings.addEditBox("Map file").type(String.class).def(DefaultGameSettings.MAP_FILE);
    }

    @Override
    public Parameter<String> getMapFile() {
        return mapFile;
    }

    @Override
    public Hero getBomberman(Level level) {
        return new HeroImpl(level, new RandomDice());
    }

    @Override
    public Parameter<Integer> getBombsCount() {
        return bombsCount;
    }

    @Override
    public Parameter<Integer> getBombsPower() {
        return bombPower;
    }

    @Override
    public Parameter<Integer> getDestroyableWalls() {
        return destroyWallCount;
    }

    @Override
    public Parameter<Integer> getMeatChoppersCount() {
        return meatChoppersCount;
    }

    @Override
    public Parameter<Integer> getBombPower() {
        return bombPower;
    }

    @Override
    public Parameter<Integer> getBombCountPerPlayer() {
        return bombsCount;
    }

}
