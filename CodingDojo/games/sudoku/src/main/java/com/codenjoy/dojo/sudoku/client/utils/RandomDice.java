package com.codenjoy.dojo.sudoku.client.utils;

import java.util.Random;

/**
 * User: sanja
 * Date: 17.08.13
 * Time: 16:43
 */
public class RandomDice implements Dice {
    @Override
    public int next(int max) {
        return new Random().nextInt(max);
    }
}
