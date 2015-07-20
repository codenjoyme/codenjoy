package com.codenjoy.dojo.a2048.model.generator;

import com.codenjoy.dojo.services.Dice;

public class Factory {
    public static Generator get(int mode, Dice dice) {
        switch (mode) {
            case -1: return new CornerGenerator();
            default: return new RandomGenerator(dice, mode);
        }
    }
}
