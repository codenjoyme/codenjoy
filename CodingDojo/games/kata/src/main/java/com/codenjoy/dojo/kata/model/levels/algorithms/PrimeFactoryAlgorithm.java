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

public class PrimeFactoryAlgorithm extends AlgorithmLevelImpl {

    @Override
    public String get(int number) {
        String result = "";
        if (number == 1) {
            return "[1]";
        }
        for (int i = 2; i < number / (i - 1); i++) {
            while (number % i == 0) {
                number /= i;
                result += String.valueOf(i) + ",";
            }
        }
        if (number > 1) {
            result += String.valueOf(number) + ",";
        }
        result = result.substring(0, result.length() - 1);
        return "[" + result + "]";
    }

    @Override
    public String description() {
        return "Разложи целое число на множители из простых чисел:\n" +
                "1 -> [1]\n" +
                "2 -> [2]\n" +
                "3 -> [3]\n" +
                "4 -> [2,2]\n" +
                "6 -> [2,3]\n" +
                "9 -> [3,3]\n" +
                "12 -> [2,2,3]\n" +
                "15 -> [3,5]\n" +
                "и так далее...";
    }

    @Override
    public int complexity() {
        return 25;
    }

    @Override
    public String author() {
        return "Алгоритм взят из доклада Johannes Brodwall о TDD" +
                "(http://szelenin.blogspot.com/2011_12_01_archive.html)";
    }
}
