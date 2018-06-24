package com.codenjoy.dojo.kata.model.levels.algorithms;


import org.junit.Test;

public class SquareUpAlgorithmTest {

    @Test
    public void shouldSquareUpCorrectly() {
        String[] expected = new String[] {
                "[]",
                "[1]",
                "[0, 1, 2, 1]",
                "[0, 0, 1, 0, 2, 1, 3, 2, 1]",
                "[0, 0, 0, 1, 0, 0, 2, 1, 0, 3, 2, 1, 4, 3, 2, 1]",
                "[0, 0, 0, 0, 1, 0, 0, 0, 2, 1, 0, 0, 3, 2, 1, 0, 4, 3, 2, 1, 5, 4, 3, 2, 1]"
        };

        SquareUpAlgorithm algorithm = new SquareUpAlgorithm();
        Assertions.assertAlgorithm(expected, algorithm);

        for (int i = 0; i < 10; i++) {
            System.out.println(algorithm.get(String.valueOf(i)));
        }
    }

}
