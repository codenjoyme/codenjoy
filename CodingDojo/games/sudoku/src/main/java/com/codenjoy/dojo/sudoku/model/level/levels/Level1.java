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

public class Level1 extends LevelImpl implements Level {

    public Level1() {
        super(
                "8 3*6 2*1 5*7 9*4*\n" +
                "2 1 9*7 4*6 8*5 3 \n" +
                "4 7*5 3 9*8 6 2*1*\n" +
                "6 9 7*5*2*4 1 3 8*\n" +
                "1*8*4*9 7 3 5*6 2 \n" +
                "3 5*2 6 8 1*9 4 7*\n" +
                "9*4 1 8*5 2 3 7*6*\n" +
                "5*6*8 4*3 7 2*1 9 \n" +
                "7 2*3 1 6 9*4 8 5*\n"
        );
    }
}
