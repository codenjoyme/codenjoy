package com.codenjoy.dojo.kata.model.levels.algorithms;


import com.codenjoy.dojo.kata.model.levels.AlgorithmLevelImpl;

import java.util.Arrays;
import java.util.List;

public class SquareUpAlgorithm extends AlgorithmLevelImpl {

    @Override
    public String get(String input) {
        int n = Integer.valueOf(input);
        int[] result = new int[n * n];
        int idx = 0;

        for (int i = 1; i <= n; i++) {
            for (int k = 1; k <= n - i; k++) {
                result[idx++] = 0;
            }
            for (int j = i; j > 0; j--) {
                result[idx++] = j;
            }
        }
        return Arrays.toString(result);
    }

    @Override
    public List<String> getQuestions() {
        return new QuestionGenerator(0, 10).generate();
    }

    @Override
    public String author() {
        return "http://codingbat.com/prob/p155405";
    }

    @Override
    public int complexity() {
        return 5;
    }

    @Override
    public String description() {
        return "Given n>=0, create an array length n*n with the following pattern, \n" +
                "shown here for n=3 : {0, 0, 1,    0, 2, 1,    3, 2, 1} \n" +
                "(spaces added to show the 3 groups).\n" +
                "Take into account that the only valid data format is shown in example below\n" +
                "e.g.\n" +
                "f(2) → [0, 1, 2, 1]\n" +
                "f(3) → [0, 0, 1, 0, 2, 1, 3, 2, 1]\n" +
                "f(4) → [0, 0, 0, 1, 0, 0, 2, 1, 0, 3, 2, 1, 4, 3, 2, 1]";
    }
}
