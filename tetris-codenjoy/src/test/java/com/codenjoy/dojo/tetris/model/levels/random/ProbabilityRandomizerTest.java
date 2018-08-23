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

import static junit.framework.Assert.assertEquals;

public class ProbabilityRandomizerTest {

    public static final int COUNT_ITERATIONS = 100000;
    private Dice dice = new TrueRandomDice();

    @Test
    public void checkProbabilities1(){
        Randomizer randomizer = new ProbabilityRandomizer(dice, 20);

        Map<Integer, Double> map = calculateFiguresProbabilities(randomizer, 10, COUNT_ITERATIONS);

        assertEquals("{0=10.87, 1=10.87, 2=10.87, 3=10.87, 4=10.87, 5=10.87, 6=10.87, 7=10.87, 8=10.87, 9=2.1}",
                map.toString());
    }

    @Test
    public void checkProbabilities2(){
        Randomizer randomizer = new ProbabilityRandomizer(dice, 50);

        Map<Integer, Double> map = calculateFiguresProbabilities(randomizer, 5, COUNT_ITERATIONS);

        assertEquals("{0=22.45, 1=22.45, 2=22.45, 3=22.45, 4=10.2}",
                map.toString());
    }

    @Test
    public void checkProbabilities3(){
        Randomizer randomizer = new ProbabilityRandomizer(dice, 100);

        Map<Integer, Double> map = calculateFiguresProbabilities(randomizer, 3, COUNT_ITERATIONS);

        assertEquals("{0=33.33, 1=33.33, 2=33.33}",
                map.toString());
    }

    @Test
    public void checkProbabilities4(){
        Randomizer randomizer = new EquiprobableRandomizer(dice);

        Map<Integer, Double> map = calculateFiguresProbabilities(randomizer, 3, COUNT_ITERATIONS);

        assertEquals("{0=33.33, 1=33.33, 2=33.33}",
                map.toString());
    }

    public static Map<Integer, Double> calculateFiguresProbabilities(Randomizer randomizer, int countFigures, int countIterations) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < countIterations; i ++) {
            int number = randomizer.getNextNumber(countFigures);
            Integer count = map.get(number);
            if (count == null) {
                count = 0;
            }
            map.put(number, count + 1);
        }

        Map<Integer, Double> map2 = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            double figureProbability = 10000L * entry.getValue() / countIterations;
            map2.put(entry.getKey(), 1D * figureProbability / 100);
        }

        return map2;
    }

}
