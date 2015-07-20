package com.codenjoy.dojo.a2048.model.generator;

import com.codenjoy.dojo.a2048.model.*;
import com.codenjoy.dojo.a2048.model.Number;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class RandomGenerator implements Generator {

    public static final Point NO_SPACE = pt(-1, -1);

    private int count;
    private Dice dice;

    public RandomGenerator(Dice dice, int count) {
        this.count = count;
        this.dice = dice;
    }

    @Override
    public void generate(Numbers numbers) {
        for (int i = 0; i < count; i++) {
            Point pt = getFreeRandom(numbers);
            if (!pt.itsMe(NO_SPACE)) {
                numbers.add(new Number(2, pt));
            }
        }
    }

    public Point getFreeRandom(Numbers numbers) {
        int rndX = 0;
        int rndY = 0;
        int c = 0;
        do {
            rndX = dice.next(numbers.size());
            rndY = dice.next(numbers.size());
        } while (rndX != -1 && rndY != -1 && numbers.isBusy(rndX, rndY) && c++ < 100);

        if (c >= 100) {
            return NO_SPACE;
        }

        return pt(rndX, rndY);
    }
}
