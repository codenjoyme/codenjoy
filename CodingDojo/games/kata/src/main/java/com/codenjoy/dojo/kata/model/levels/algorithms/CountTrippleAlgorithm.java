package com.codenjoy.dojo.kata.model.levels.algorithms;

import com.codenjoy.dojo.kata.model.levels.AlgorithmLevelImpl;

import java.util.Arrays;
import java.util.List;


public class CountTrippleAlgorithm extends AlgorithmLevelImpl {

    @Override
    public List<String> getQuestions() {
        return Arrays.asList(
                "abcXXXabc",
                "vvaad",
                "xxxabyyyycd",
                "c",
                "saedfasaaaaasadddfsffff",
                "9991jd12Xxx8888daadaaa",
                "jgbansdkaaffffasssjjjjjjddsaasss0",
                "ss23412;;;dsdawrfas^^^"
        );
    }

    @Override
    public String get(String input) {
        int count = 0;

        for(int i = 0; i <= input.length() - 3; i++) {
            if(input.charAt(i) == input.charAt(i+1) && input.charAt(i) == input.charAt(i+2)) {
                count++;
            }
        }

        return String.valueOf(count);

    }

    @Override
    public String author() {
        return "http://codingbat.com/prob/p195714";
    }

    @Override
    public int complexity() {
        return 7;
    }

    // TODO: translate
    @Override
    public String description() {
        return "We'll say that a \"triple\" in a string is a char appearing three times in a row. Return the number of triples in the given string. The triples may overlap.\n" +
                "\n" +
                "countTriple(\"abcXXXabc\") → 1\n" +
                "countTriple(\"xxxabyyyycd\") → 3\n" +
                "countTriple(\"a\") → 0";
    }
}
