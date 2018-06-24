package com.codenjoy.dojo.kata.model.levels.algorithms;


import com.codenjoy.dojo.kata.model.levels.AlgorithmLevelImpl;

import java.util.Arrays;
import java.util.List;

public class ArabicToRomanNumbersAlgorithm extends AlgorithmLevelImpl {

    private static final String[] THOUSANDS = {"", "M", "MM", "MMM"};
    private static final String[] HUNDREDS = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
    private static final String[] TENS = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
    private static final String[] UNITS = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

    @Override
    public String get(String input) {
        int th, h, t, u;
        int num = Integer.parseInt(input);
        th = num / 1000;
        h = (num / 100) % 10;
        t = (num / 10) % 10;
        u = num % 10;

        return THOUSANDS[th] + HUNDREDS[h] + TENS[t] + UNITS[u];
    }

    @Override
    public List<String> getQuestions() {
        return Arrays.asList(
                "5",
                "9",
                "10",
                "11",
                "20",
                "19",
                "39",
                "50",
                "56"
        );
    }

    @Override
    public String author() {
        return "https://projecteuler.net/problem=89";
    }

    @Override
    public int complexity() {
        return 45;
    }

    @Override
    public String description() {
        return "Напиши метод, переводящий числа, записанные Римскими цифрами,\n" +
                "в Арабские (Decimal), используя минимальную нотацию\n" +
                "Пример:\n" +
                "f(10) -> X\n" +
                "f(9) -> IX\n" +
                "и т.д.";
    }
}
