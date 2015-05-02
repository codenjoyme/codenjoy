package com.codenjoy.dojo.services;

import java.util.Random;

public class RandomDice implements Dice {

    @Override
    public int next(int n) {
        return new Random().nextInt(n);
    }
}
