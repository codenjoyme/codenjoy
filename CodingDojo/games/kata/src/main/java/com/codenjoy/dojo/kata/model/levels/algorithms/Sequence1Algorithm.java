package com.codenjoy.dojo.kata.model.levels.algorithms;

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


import com.codenjoy.dojo.kata.model.levels.AlgorithmLevelImpl;

public class Sequence1Algorithm extends AlgorithmLevelImpl {

    @Override
    public String get(int n) {
        StringBuilder b = new StringBuilder();

        for(int i = 0; i < 1000; i++) {
            b.append(String.valueOf(10 + i));
        }

        String result = b.toString();

        return result.substring((n - 1)*3, n*3);
    }

    @Override
    public String description() {
        return "Continue the sequence 101, 112, 131, 415, 161, 718... \n" +
                "i.e f(1) = 101, f(2) = 112, ...\n" +
                "Hint: Look at the whole picture";
    }

    @Override
    public int complexity() {
        return 15;
    }

    @Override
    public String author() {
        return "Alexey.Shcheglov (Alexey_Shcheglov@epam.com)\n" +
                "http://nazva.net/78";
    }
}
