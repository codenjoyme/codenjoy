package com.codenjoy.dojo.services;

import java.util.Random;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 11:33 PM
 */
public class RandomDice implements Dice {

    @Override
    public int next(int n) {
        return new Random().nextInt(n);
    }
}
