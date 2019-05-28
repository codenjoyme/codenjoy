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

public class FibonacciNumbersAlgorithm extends AlgorithmLevelImpl {

    @Override
    public String get(int number) {
        int previous = 0;
        int current = 0;
        int next = 1;
        for (int index = 0; index < number; index++) {
            previous = current;
            current = next;
            next = previous + current;
        }
        return String.valueOf(current);
    }

    @Override
    public String description() {
        return "Сделай метод, возвращающий числ Фибоначчи из ряда по его номеру:\n" +
                "f(0) = 0\n" +
                "f(1) = 1\n" +
                "f(2) = 0 + 1 = 1\n" +
                "f(3) = 1 + 1 = 2\n" +
                "f(4) = 1 + 2 = 3\n" +
                "f(5) = 2 + 3 = 5\n" +
                "f(6) = 3 + 5 = 8\n" +
                "f(7) = 5 + 8 = 13\n" +
                "f(8) = 8 + 13 = 21\n" +
                "f(9) = 13 + 21 = 34\n" +
                "и так далее...";
    }

    @Override
    public int complexity() {
        return 20;
    }

    @Override
    public String author() {
        return "Александр Баглай (apofig@gmail.com)";
    }
}
