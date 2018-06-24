package com.codenjoy.dojo.kata.model.levels.algorithms;

import com.codenjoy.dojo.kata.model.levels.AlgorithmLevelImpl;

import java.util.Arrays;
import java.util.List;

/**
 * Aka makeChocolate
 * Aka extended version of {@link MakeBricksAlgorithm}
 */
public class PackageGiftBoxesAlgorithm extends AlgorithmLevelImpl {

    @Override
    public String get(int... input) {
        int small = input[0];
        int big = input[1];
        int goal = input[2];
        int remainder = goal >= 5 * big ? goal - (5 * big) : goal % 5;

        return String.valueOf(remainder <= small ? remainder : -1);
    }

    @Override
    public List<String> getQuestions() {
        return Arrays.asList(
                "4, 1, 9",
                "4, 1, 10",
                "4, 1, 7"
        );
    }

    @Override
    public String author() {
        return "codingbat - makeChocolate: http://codingbat.com/prob/p191363";
    }

    @Override
    public int complexity() {
        return 5;
    }

    @Override
    public String description() {
        return "We want make a package of goal kilos of chocolate. We have \n" +
                "small bars (1 kilo each) and big bars (5 kilos each). \n" +
                "Return the number of small bars to use, assuming we always \n" +
                "use big bars before small bars. Return -1 if it can't be done.\n" +
                "e.g. function(small, big, int) { /* ... */ }\n" +
                "Examples:\n" +
                "packGifts(4, 1, 9) → 4\n" +
                "packGifts(4, 1, 10) → -1\n" +
                "packGifts(4, 1, 7) → 2";
    }
}
