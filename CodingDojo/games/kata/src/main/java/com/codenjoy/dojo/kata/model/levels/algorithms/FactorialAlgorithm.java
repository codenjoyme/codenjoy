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

import java.math.BigInteger;
import java.util.Map;

public class FactorialAlgorithm extends AlgorithmLevelImpl {

    // TODO подумать над проблемами многопоточности
    private static Map<Integer, BigInteger> cache = new java.util.HashMap<>();

    @Override
    public String get(int n) {
        BigInteger result;
        if (n == 0) {
            return "1";
        }
        if (null != (result = cache.get(n))) {
            return result.toString();
        }
        result = BigInteger.valueOf(n).multiply(new BigInteger(get(String.valueOf(n - 1))));
        cache.put(n, result);
        return result.toString();
    }

    @Override
    public String description() {
        return "Напиши метод, принимающий один int аргумент и " +
                "возвращающий факториал этого числа в виде String. \n" +
                "Внимание! Возможно переполнение int/long.";
    }

    @Override
    public int complexity() {
        return 40;
    }

    @Override
    public String author() {
        return "Александр Баглай (apofig@gmail.com)";
    }
}
