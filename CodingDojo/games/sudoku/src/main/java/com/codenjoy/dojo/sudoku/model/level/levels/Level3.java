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

public class Level3 extends LevelImpl implements Level {

    public Level3() {
        super(
                "6*5*7 2*4*8 9*1*3 \n" +
                "1*8 4 7 3*9*6 5*2*\n" +
                "3*9*2 5 1*6*4 7 8*\n" +
                "4*7 6*3*9 2 1 8 5*\n" +
                "2*1*5 8*7 4 3*6*9 \n" +
                "8*3*9*1 6*5*2*4 7 \n" +
                "7*2*1*4 8*3*5*9 6 \n" +
                "9*4 3*6 5 7 8*2 1*\n" +
                "5*6 8 9*2 1*7*3*4*\n"
        );
    }
}
