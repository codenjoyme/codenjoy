package com.codenjoy.dojo.battlecity.services;

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.settings.Chance;
import com.codenjoy.dojo.services.settings.Parameter;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

//import static com.codenjoy.dojo.battlecity.model.Elements.*;
import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ChanceTest {

    private GameSettings gameSettings;
    private Chance chance;

    private Map<CharElements, Parameter> input;

    @Before
    public void setup() {
        gameSettings = new GameSettings();
        input = new LinkedHashMap<>();
    }

    private int assertAxis(Chance chance, Elements elements) {
        return (int) chance.axis().stream().filter(x -> x.equals(elements)).count();
    }

    private Map<CharElements, Parameter> map() {
        input.put(Elements.PRIZE_IMMORTALITY, gameSettings.integerValue(IMMORTALITY));
        input.put(Elements.PRIZE_BREAKING_WALLS, gameSettings.integerValue(BREAKING_WALLS));
        input.put(Elements.PRIZE_WALKING_ON_WATER, gameSettings.integerValue(WALKING_ON_WATER));
        input.put(Elements.PRIZE_VISIBILITY, gameSettings.integerValue(VISIBILITY));
        return input;
    }

    @Test
    public void shouldFillAxisWithoutWalkingOnWater() {
        //given
        gameSettings.integer(IMMORTALITY, 50)
                    .integer(BREAKING_WALLS, 10)
                    .integer(WALKING_ON_WATER, 0)
                    .integer(VISIBILITY, -1);

        //when
        chance = new Chance(map());
        chance.run();

        //then
        assertEquals(80, chance.axis().size());
        assertEquals(50, assertAxis(chance, Elements.PRIZE_IMMORTALITY));
        assertEquals(10, assertAxis(chance, Elements.PRIZE_BREAKING_WALLS));
        assertEquals(0, assertAxis(chance, Elements.PRIZE_WALKING_ON_WATER));
        assertEquals(20, assertAxis(chance, Elements.PRIZE_VISIBILITY));
    }

    @Test
    public void shouldFillAxisWithoutImmortality() {
        //given
        gameSettings.integer(IMMORTALITY, 0)
                .integer(BREAKING_WALLS, -1)
                .integer(WALKING_ON_WATER, 50)
                .integer(VISIBILITY, 10);

        //when
        chance = new Chance(map());
        chance.run();

        //then
        assertEquals(80, chance.axis().size());
        assertEquals(0, assertAxis(chance, Elements.PRIZE_IMMORTALITY));
        assertEquals(20, assertAxis(chance, Elements.PRIZE_BREAKING_WALLS));
        assertEquals(50, assertAxis(chance, Elements.PRIZE_WALKING_ON_WATER));
        assertEquals(10, assertAxis(chance, Elements.PRIZE_VISIBILITY));
    }

    @Test
    public void shouldAxisIsEmpty() {
        //given
        gameSettings.integer(IMMORTALITY, 0)
                    .integer(BREAKING_WALLS, 0)
                    .integer(WALKING_ON_WATER, 0)
                    .integer(VISIBILITY, 0);

        //when
        chance = new Chance(map());
        chance.run();

        //then
        assertEquals(0, chance.axis().size());
    }

    @Test
    public void shouldFillAxisIfAllParametersMinus() {
        //given
        gameSettings.integer(IMMORTALITY, -1)
                    .integer(BREAKING_WALLS, -1)
                    .integer(WALKING_ON_WATER, -1)
                    .integer(VISIBILITY, -1);

        //when
        chance = new Chance(map());
        chance.run();

        //then
        assertEquals(100, chance.axis().size());
        assertEquals(25, assertAxis(chance, Elements.PRIZE_IMMORTALITY));
        assertEquals(25, assertAxis(chance, Elements.PRIZE_BREAKING_WALLS));
        assertEquals(25, assertAxis(chance, Elements.PRIZE_WALKING_ON_WATER));
        assertEquals(25, assertAxis(chance, Elements.PRIZE_VISIBILITY));
    }

    @Test
    public void shouldFillAxisChangeParametersWithoutMinus() {
        //given
        gameSettings.integer(IMMORTALITY, 60)
                .integer(BREAKING_WALLS, 50)
                .integer(WALKING_ON_WATER, 0)
                .integer(VISIBILITY, 40);

        //when
        chance = new Chance(map());
        chance.run();

        //then
        assertEquals(99, chance.axis().size());
        assertEquals(40, assertAxis(chance, Elements.PRIZE_IMMORTALITY));
        assertEquals(33, assertAxis(chance, Elements.PRIZE_BREAKING_WALLS));
        assertEquals(0, assertAxis(chance, Elements.PRIZE_WALKING_ON_WATER));
        assertEquals(26, assertAxis(chance, Elements.PRIZE_VISIBILITY));
    }

    @Test
    public void shouldFillAxisChangeParametersWithMinus() {
        //given
        gameSettings.integer(IMMORTALITY, 60)
                .integer(BREAKING_WALLS, 50)
                .integer(WALKING_ON_WATER, -1)
                .integer(VISIBILITY, 40);

        //when
        chance = new Chance(map());
        chance.run();

        //then
        assertEquals(84, chance.axis().size());
        assertEquals(28, assertAxis(chance, Elements.PRIZE_IMMORTALITY));
        assertEquals(23, assertAxis(chance, Elements.PRIZE_BREAKING_WALLS));

        assertEquals(15, assertAxis(chance, Elements.PRIZE_WALKING_ON_WATER));
        assertEquals(18, assertAxis(chance, Elements.PRIZE_VISIBILITY));
    }

}
