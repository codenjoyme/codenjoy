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

import java.util.Arrays;
import java.util.List;

public class MakeBricksAlgorithm extends AlgorithmLevelImpl {

    @Override
    public String get(int... input) {
        int small = input[0];
        int big = input[1];
        int goal = input[2];

        boolean result = (small >= (goal % 5) && small >= goal - big * 5);

        return String.valueOf(result);
    }

    @Override
    public List<String> getQuestions() {
        return Arrays.asList(
                "0, 1, 5",
                "1, 0, 1",
                "1, 0, 2",
                "3, 1, 7",
                "4, 2, 14",
                "3, 2, 14",
                "0, 2, 5",
                "3, 1, 6",
                "3, 1, 9",
                "3, 2, 9",
                "10, 10, 0",
                "0, 0, 1",
                "0, 1, 1",
                "0, 0, 2",
                "2, 0, 2",
                "0, 1, 2",
                "1, 1, 2",
                "2, 1, 3",
                "3, 1, 4",
                "2, 1, 1",
                "2, 2, 6",
                "6, 1, 11",
                "5, 0, 1",
                "3, 1, 8",
                "3, 2, 10",
                "3, 2, 8",
                "6, 0, 11",
                "1, 4, 11",
                "0, 3, 10",
                "1, 4, 12",
                "1, 1, 7",
                "2, 1, 7",
                "7, 1, 11",
                "7, 1, 8",
                "7, 1, 13",
                "43, 1, 46",
                "40, 1, 46",
                "40, 2, 47",
                "40, 2, 50",
                "40, 2, 52",
                "22, 2, 33",
                "0, 2, 10",
                "1000000, 1000, 1000100",
                "2, 1000000, 100003",
                "20, 0, 19",
                "20, 0, 21",
                "20, 4, 51",
                "20, 4, 39");
    }

    @Override
    public String description() {
        return "У тебя есть кирпичики размером 5 (bigCount) и 1 (smallCount). " +
                "Напиши метод, который смог бы определить получится из заданного " +
                "набора кирпичиков  построить стенку заданной длинны (length). " +
                "Например: \n" +
                "method(3, 1, 8) → true\n" +
                "method(3, 1, 9) → false\n" +
                "method(3, 2, 10) → true";
    }

    @Override
    public String author() {
        return "Взято из CodingBat по рекомендации коллеги " +
                "(https://codingbatsolutions.wordpress.com/2013/03/28/makebricks/)";
    }

    @Override
    public int complexity() {
        return 35;
    }
}
