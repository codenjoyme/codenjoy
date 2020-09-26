package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.random;

public class PrizeGenerator {

    private static final List<Elements> PRIZES = Arrays.asList(
            Elements.PRIZE_IMMORTALITY,
            Elements.PRIZE_BREAKING_WALLS,
            Elements.PRIZE_WALKING_ON_WATER);

    private Field field;
    private Dice dice;

    public PrizeGenerator(Field field, Dice dice) {
        this.field = field;
        this.dice = dice;
    }

    public void drop() {
        Point pt;
        int c = 0;
        do {
            pt = random(dice, field.size());
        } while (field.isBarrier(pt) && c++ < field.size());

        if (!field.isBarrier(pt)) {
            field.addPrize(new Prize(pt, PRIZES.get(dice.next(PRIZES.size()))));
        }
    }
}
