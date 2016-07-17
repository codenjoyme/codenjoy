package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.tetris.model.Randomizer;

import java.util.Random;

/**
 * User: oleksandr.baglai
 * Date: 9/25/12
 * Time: 10:36 AM
 */
public class ProbabilityRandomizer implements Randomizer {

    private Random random = new Random();
    private int lastFigureProbability;

    public ProbabilityRandomizer(int lastFigureProbability) {
        this.lastFigureProbability = lastFigureProbability;
    }

    @Override
    public int getNextNumber(int count) {
        int i = random.nextInt(count);
        if (i == count - 1) {
            int j = random.nextInt(100);
            if (j <= lastFigureProbability) {
                return i;
            } else {
                return random.nextInt(count - 1);
            }
        }
        return i;
    }
}
