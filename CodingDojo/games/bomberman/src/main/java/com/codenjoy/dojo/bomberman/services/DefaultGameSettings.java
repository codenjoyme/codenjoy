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


import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.settings.Parameter;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * User: oleksandr.baglai
 * Date: 3/11/13
 * Time: 6:41 PM
 */
public class DefaultGameSettings implements GameSettings {

    public static final int MEAT_CHOPPERS_COUNT = 10;
    public static final int BOMB_POWER = 3;
    public static final int BOMBS_COUNT = 1;
    public static final int BOARD_SIZE = 33;
    public static final int DESTROY_WALL_COUNT = BOARD_SIZE * BOARD_SIZE / 10;
/*
    @Override
    public Level getLevel() {
        return null;
    }

    @Override
    public Walls getWalls(Bomberman board) {
        OriginalWalls originalWalls = new OriginalWalls(v(BOARD_SIZE));
        MeatChoppers meatChoppers = new MeatChoppers(originalWalls, board, v(MEAT_CHOPPERS_COUNT), new RandomDice());
        EatSpaceWalls eatWalls = new EatSpaceWalls(meatChoppers, board, v(DESTROY_WALL_COUNT), new RandomDice());
        return eatWalls;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(BOARD_SIZE);
    }

*/

    @Override
    public Hero getBomberman(Level level) {
        return new HeroImpl(level, new RandomDice());
    }

}
