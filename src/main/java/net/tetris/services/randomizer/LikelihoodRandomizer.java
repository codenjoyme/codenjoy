package net.tetris.services.randomizer;

import java.util.Random;

/**
 * User: oleksandr.baglai
 * Date: 9/25/12
 * Time: 10:36 AM
 */
public class LikelihoodRandomizer implements Randomizer {

    private Random random = new Random();
    private int lastFigureLikelihood;

    public LikelihoodRandomizer(int lastFigureLikelihood) {
        this.lastFigureLikelihood = lastFigureLikelihood;
    }

    @Override
    public int getNextNumber(int count) {
        int i = random.nextInt(count);
        if (i == count - 1) {
            int j = random.nextInt(100);
            if (j <= lastFigureLikelihood) {
                return i;
            } else {
                return random.nextInt(count - 1);
            }
        }
        return i;
    }
}
