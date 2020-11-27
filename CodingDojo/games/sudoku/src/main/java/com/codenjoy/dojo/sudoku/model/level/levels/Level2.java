package com.codenjoy.dojo.sudoku.model.level.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.sudoku.model.level.Level;
import com.codenjoy.dojo.sudoku.model.level.LevelImpl;

public class Level2 extends LevelImpl implements Level {

    public Level2() {
        super(
                "2 9 1 6 7 5*4 3*8*\n" +
                "4*5*6 8*3 2 9*1 7*\n" +
                "8 7*3 4 1*9 6 5*2 \n" +
                "9*2 4 3 5 1 8*7*6*\n" +
                "3 1 7*9*6*8*5*2 4 \n" +
                "6 8*5 2 4 7 1*9*3*\n" +
                "5 6*8 7*9 3*2 4 1*\n" +
                "7 4 9 1*2*6 3 8 5*\n" +
                "1*3*2*5 8 4 7 6*9 \n"
        );
    }
}
