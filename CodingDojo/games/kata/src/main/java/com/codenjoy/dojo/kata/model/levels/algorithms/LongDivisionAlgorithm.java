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

public class LongDivisionAlgorithm extends AlgorithmLevelImpl {

    @Override
    public String get(int... input) {
        int a = input[0];
        int b = input[1];

        if (b == 0) {
            return "Div by zero error!";
        }
        if (a == 0) {
            return "0";
        }
        boolean isMinus = (a < 0 && b > 0 || a > 0 && b < 0);
        if (a < 0) a = -a;
        if (b < 0) b = -b;

        final int length = 100;
        final int DOT = -1;
        final int TEN = 10;
        final int NOT_FOUND = -1;

        StringBuilder result = new StringBuilder();

        int[] aa = new int[length * 2];
        int ia = 0;
        int ceilpos = 0;

        boolean isCalcCeil = a >= b;
        if (!isCalcCeil) {
            result.append("0.");
            aa[ia++] = 0;
            aa[ia++] = DOT;
            ceilpos = ia - 1;
        }
        while (a != 0 && (result.length() - ceilpos) <= length) {
            if (a < b) {
                if (isCalcCeil) {
                    result.append(".");
                    aa[ia++] = DOT;
                    ceilpos = ia - 1;
                    isCalcCeil = false;
                }
                a = a * TEN;
            }
            while (a < b) {
                result.append("0");
                aa[ia++] = 0;
                a = a * TEN;
            }

            int found = NOT_FOUND;
            for (int ja = 0; ja < ia; ja++) {
                if (aa[ja] == a) {
                    found = ja;
                    break;
                }
            }
            if (found != NOT_FOUND) {
                result = new StringBuilder(result.substring(0, found) + '(' + result.substring(found) + ')');
                break;
            }

            int c = a / b;
            if (isCalcCeil) {
                int c1 = c;
                while (c1 / TEN != 0) {
                    aa[ia++] = c1 % TEN;
                    c1 = c1 / TEN;
                }
                aa[ia++] = c1;
            } else {
                aa[ia++] = a;
            }
            int d = a % b;
            result.append(c);
            a = d;
        }

        return ((isMinus) ? "-" : "") + result;
    }

    @Override
    public List<String> getQuestions() {
        return Arrays.asList(
                "1, 2",
                "1, 1",
                "5, 5",
                "55, 5",
                "55, 44",
                "0, 56",
                "56, 1",
                "1, -2",
                "-1, 2",
                "-1, -2",
                "1, 1000",
                "56, 45",
                "111, 110",
                "111, 11",
                "11111, 11",
                "-11, -222",
                "111, -22",
                "1, 3000",
                "87, 78",
                "45, 56",
                "212, 133",
                "11111, 115",
                "123, 345",
                "66666666, 77727777",
                "666666660, 77727777",
                "666666660, 7772777",
                "100, 97",
                "999, 0");
    }

    @Override
    public String description() {
        return "Напиши метод, который делит два числа в столбик " +
                "с точностью до 100 знака. " +
                "Если в результате встречается период, " +
                "его стоит записать так: \n" +
                "212/133 = 1.(593984962406015037)\n" +
                "87/78   = 1.1(153846)";
    }

    @Override
    public int complexity() {
        return 60;
    }

    @Override
    public String author() {
        return "Александр Баглай (apofig@gmail.com)";
    }
}
