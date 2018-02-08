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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class ReverseAddPalindromeAlgorithm extends AlgorithmLevelImpl {

    @Override
    public String get(int n) {
        BigInteger result = BigInteger.valueOf(n);
        do {
            String reverseString = ReverseString(String.valueOf(result));
            BigInteger rev = new BigInteger(reverseString);
            result = result.add(rev);
        } while (!isPolyndrom(result));
        return result.toString();
    }

    private boolean isPolyndrom(BigInteger n) {
        String number = String.valueOf(n);
        return number.equals(ReverseString(number));
    }

    private String ReverseString(String number) {
        return new StringBuilder(number).reverse().toString();
    }

    @Override
    public String description() {
        return "For the most of numbers iterative 'reverse and add' " +
                "actions lead to palindrome number. For example: \n" +
                "Number 23: 23 + 32 = 55; \n" +
                "Number 254: 254 + 452 = 706; 706 + 607 = 1313; 1313 + 3131 = 4444;\n" +
                "Write a method which calculates 'ReverseAndAdd' polyndrom " +
                "for a number. i.e f(23) = 55; f(254) = 4444";
    }

    @Override
    public List<String> getQuestions() { // TODO погенерить кейзов
        return Arrays.asList(
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "9",
                "10",
                "11",
                "12",
                "123",
                "234",
                "345",
                "456",
                "567",
                "678"
        );
    }

    @Override
    public int complexity() {
        return 6;
    }

    @Override
    public String author() {
        return "Alexey.Shcheglov (Alexey_Shcheglov@epam.com)";
    }
}
