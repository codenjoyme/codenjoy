package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.tetris.model.EquiprobableRandomizer;
import com.codenjoy.dojo.tetris.model.ProbabilityRandomizer;
import com.codenjoy.dojo.tetris.model.Randomizer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.Assertions.assertThat;

public class ProbabilityRandomizerTest {

    @Test
    public void checkProbabilities1(){
        Randomizer randomizer = new ProbabilityRandomizer(20);

        Map<Integer, Double> map = calculateFiguresProbabilities(randomizer, 10, 1000000);

        assertEquals("{0=10.0, 1=10.0, 2=10.0, 3=10.0, 4=10.0, 5=10.0, 6=10.0, 7=10.0, 8=10.0, 9=2.0}",
                map.toString());
    }

    @Test
    public void checkProbabilities2(){
        Randomizer randomizer = new ProbabilityRandomizer(50);

        Map<Integer, Double> map = calculateFiguresProbabilities(randomizer, 5, 1000000);

        assertEquals("{0=22.0, 1=22.0, 2=22.0, 3=22.0, 4=10.0}",
                map.toString());
    }

    @Test
    public void checkProbabilities3(){
        Randomizer randomizer = new ProbabilityRandomizer(100);

        Map<Integer, Double> map = calculateFiguresProbabilities(randomizer, 3, 1000000);

        assertEquals("{0=33.0, 1=33.0, 2=33.0}",
                map.toString());
    }

    @Test
    public void checkProbabilities4(){
        Randomizer randomizer = new EquiprobableRandomizer();

        Map<Integer, Double> map = calculateFiguresProbabilities(randomizer, 3, 1000000);

        assertEquals("{0=33.0, 1=33.0, 2=33.0}",
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
            double figureProbability = 100 * entry.getValue() / countIterations;
            map2.put(entry.getKey(), figureProbability);
        }

        return map2;
    }

}
