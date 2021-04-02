//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.utils;

import java.util.Random;

public class RandomDice implements Dice {
    public RandomDice() {
    }

    public int next(int max) {
        return (new Random()).nextInt(max);
    }
}
