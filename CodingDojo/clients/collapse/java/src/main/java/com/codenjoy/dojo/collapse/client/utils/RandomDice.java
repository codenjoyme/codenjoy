package com.codenjoy.dojo.collapse.client.utils;

import java.util.Random;

public class RandomDice implements Dice {
    @Override
    public int next(int max) {
        return new Random().nextInt(max);
    }
}
