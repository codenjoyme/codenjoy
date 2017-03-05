package com.codenjoy.dojo.kata.model.levels.algorithms;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 Codenjoy
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

/**
 * User: oleksandr.baglai
 * Date: 2/13/13
 * Time: 7:10 PM
 */
public class FizzBuzzAlgorithm extends AlgorithmLevelImpl {

    @Override
    public String get(int i) {
        String result = "";
        if (i % 3 == 0) {
            result += "Fizz";
        }
        if (i % 5 == 0) {
            result += "Buzz";
        }
        if (result.length() == 0) {
            result = String.valueOf(i);
        }
        return result;
    }

    @Override
    public String description() {
        return "Напиши метод, принимающий один int аргумент и \n" +
                "возвращающий String. Для тех чисел, которые делятся \n" +
                "нацело на 3 метод должен вернуть “Fizz”, для тех, \n" +
                "что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 \n" +
                "и на 5 - “FizzBuzz”, ну а для всех остальных - само число.";
    }

    @Override
    public int complexity() {
        return 100;
    }
}
