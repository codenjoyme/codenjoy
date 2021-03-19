package com.codenjoy.dojo.battlecity.services;

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.settings.Chance;
import com.codenjoy.dojo.services.settings.Parameter;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.codenjoy.dojo.battlecity.model.Elements.*;
import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ChanceTest {

    private GameSettings settings;
    private Chance chance;

    private Map<CharElements, Parameter> input;

    @Before
    public void setup() {
        settings = new GameSettings();
        input = new LinkedHashMap<>();
    }

    private int axisCount(Chance chance, Elements elements) {
        return (int) chance.axis()
                .stream()
                .filter(x -> x.equals(elements))
                .count();
    }

    private void buildChance() {
        chance = new Chance();

        chance.put(PRIZE_IMMORTALITY,
                settings.integerValue(IMMORTALITY));

        chance.put(PRIZE_BREAKING_WALLS,
                settings.integerValue(BREAKING_WALLS));

        chance.put(PRIZE_WALKING_ON_WATER,
                settings.integerValue(WALKING_ON_WATER));

        chance.put(PRIZE_VISIBILITY,
                settings.integerValue(VISIBILITY));

        chance.run();
    }

    @Test
    public void shouldFillAxisWithoutWalkingOnWater() {
        // given
        settings.integer(IMMORTALITY, 50)
                .integer(BREAKING_WALLS, 10)
                .integer(WALKING_ON_WATER, 0)
                .integer(VISIBILITY, -1);

        // when
        buildChance();

        // then
        assertEquals(80, chance.axis().size());
        assertEquals(50, axisCount(chance, PRIZE_IMMORTALITY));
        assertEquals(10, axisCount(chance, PRIZE_BREAKING_WALLS));
        assertEquals(0, axisCount(chance, PRIZE_WALKING_ON_WATER));
        assertEquals(20, axisCount(chance, Elements.PRIZE_VISIBILITY));
    }

    @Test
    public void shouldFillAxisWithoutImmortality() {
        // given
        settings.integer(IMMORTALITY, 0)
                .integer(BREAKING_WALLS, -1)
                .integer(WALKING_ON_WATER, 50)
                .integer(VISIBILITY, 10);

        // when
        buildChance();

        // then
        assertEquals(80, chance.axis().size());
        assertEquals(0, axisCount(chance, PRIZE_IMMORTALITY));
        assertEquals(20, axisCount(chance, PRIZE_BREAKING_WALLS));
        assertEquals(50, axisCount(chance, PRIZE_WALKING_ON_WATER));
        assertEquals(10, axisCount(chance, Elements.PRIZE_VISIBILITY));
    }

    @Test
    public void shouldAxisIsEmpty() {
        // given
        settings.integer(IMMORTALITY, 0)
                    .integer(BREAKING_WALLS, 0)
                    .integer(WALKING_ON_WATER, 0)
                    .integer(VISIBILITY, 0);

        // when
        buildChance();

        // then
        assertEquals(0, chance.axis().size());
    }

    @Test
    public void shouldFillAxisIfAllParametersMinus() {
        // given
        settings.integer(IMMORTALITY, -1)
                .integer(BREAKING_WALLS, -1)
                .integer(WALKING_ON_WATER, -1)
                .integer(VISIBILITY, -1);

        // when
        buildChance();

        // then
        assertEquals(100, chance.axis().size());
        assertEquals(25, axisCount(chance, PRIZE_IMMORTALITY));
        assertEquals(25, axisCount(chance, PRIZE_BREAKING_WALLS));
        assertEquals(25, axisCount(chance, PRIZE_WALKING_ON_WATER));
        assertEquals(25, axisCount(chance, Elements.PRIZE_VISIBILITY));
    }

    @Test
    public void shouldFillAxisChangeParametersWithoutMinus() {
        // given
        settings.integer(IMMORTALITY, 60)
                .integer(BREAKING_WALLS, 50)
                .integer(WALKING_ON_WATER, 0)
                .integer(VISIBILITY, 40);

        // when
        buildChance();

        // then
        assertEquals(99, chance.axis().size());
        assertEquals(40, axisCount(chance, PRIZE_IMMORTALITY));
        assertEquals(33, axisCount(chance, PRIZE_BREAKING_WALLS));
        assertEquals(0, axisCount(chance, PRIZE_WALKING_ON_WATER));
        assertEquals(26, axisCount(chance, Elements.PRIZE_VISIBILITY));
    }

    @Test
    public void shouldFillAxisChangeParametersWithMinus() {
        // given
        settings.integer(IMMORTALITY, 60)
                .integer(BREAKING_WALLS, 50)
                .integer(WALKING_ON_WATER, -1)
                .integer(VISIBILITY, 40);

        // when
        buildChance();

        // then
        assertEquals(84, chance.axis().size());
        assertEquals(28, axisCount(chance, PRIZE_IMMORTALITY));
        assertEquals(23, axisCount(chance, PRIZE_BREAKING_WALLS));

        assertEquals(15, axisCount(chance, PRIZE_WALKING_ON_WATER));
        assertEquals(18, axisCount(chance, Elements.PRIZE_VISIBILITY));
    }

}
