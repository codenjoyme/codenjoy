package com.codenjoy.dojo.tetris.model.levels.random;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ProbabilityRandomizerTest {

    public static final int COUNT_ITERATIONS = 100000;
    private Dice dice = new TrueRandomDice();

    @Test
    public void checkProbabilities1(){
        Randomizer randomizer = new ProbabilityRandomizer(dice, 20);

        Map<Integer, Integer> map = calculateFiguresProbabilities(randomizer, 10, COUNT_ITERATIONS);

        assertEquals("{0=11, 1=11, 2=11, 3=11, 4=11, 5=11, 6=11, 7=11, 8=11, 9=2}",
                map.toString());
    }

    @Test
    public void checkProbabilities2(){
        Randomizer randomizer = new ProbabilityRandomizer(dice, 50);

        Map<Integer, Integer> map = calculateFiguresProbabilities(randomizer, 5, COUNT_ITERATIONS);

        assertEquals("{0=22, 1=22, 2=22, 3=22, 4=10}",
                map.toString());
    }

    @Test
    public void checkProbabilities3(){
        Randomizer randomizer = new ProbabilityRandomizer(dice, 100);

        Map<Integer, Integer> map = calculateFiguresProbabilities(randomizer, 3, COUNT_ITERATIONS);

        assertEquals("{0=33, 1=33, 2=33}",
                map.toString());
    }

    @Test
    public void checkProbabilities4(){
        Randomizer randomizer = new EquiprobableRandomizer(dice);

        Map<Integer, Integer> map = calculateFiguresProbabilities(randomizer, 3, COUNT_ITERATIONS);

        assertEquals("{0=33, 1=33, 2=33}",
                map.toString());
    }

    public static Map<Integer, Integer> calculateFiguresProbabilities(Randomizer randomizer, int countFigures, int countIterations) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < countIterations; i ++) {
            int number = randomizer.getNextNumber(countFigures);
            Integer count = map.get(number);
            if (count == null) {
                count = 0;
            }
            map.put(number, count + 1);
        }

        Map<Integer, Integer> map2 = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            double figureProbability = 100D * entry.getValue() / countIterations;
            map2.put(entry.getKey(), (int)Math.round(figureProbability));
        }

        return map2;
    }

}
