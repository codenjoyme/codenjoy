package com.codenjoy.dojo.services.settings;

import com.codenjoy.dojo.client.TestGameSettings;
import com.codenjoy.dojo.services.printer.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChanceTest {

    private TestGameSettings settings;
    private Chance chance;

    @Before
    public void setup() {
        settings = new TestGameSettings();
    }

    private int assertAxis(Chance chance, Elements elements) {
        return (int) chance.axis().stream().filter(x -> x.equals(elements)).count();
    }

    private void buildChance() {
        chance = new Chance();
        chance.put(Elements.ONE, settings.integerValue(Keys.ONE));
        chance.put(Elements.TWO, settings.integerValue(Keys.TWO));
        chance.put(Elements.THREE, settings.integerValue(Keys.THREE));
        chance.put(Elements.FOUR, settings.integerValue(Keys.FOUR));
        chance.run();
    }

    // параметр "0" -> не участвует
    // 50 + 10 = 60, 60 < 100
    // 50 -> 50
    // 10 -> 10
    // -1 (1 шт) -> (100 - (sum = 60)) / 2 = 20
    // axis.size() = 80
    @Test
    public void shouldFillAxisWithoutWalkingOnWater() {
        // given
        settings.integer(Keys.ONE, 50)
                .integer(Keys.TWO, 10)
                .integer(Keys.THREE, 0)
                .integer(Keys.FOUR, -1);

        // when
        buildChance();

        // then
        assertEquals(80, chance.axis().size());
        assertEquals(50, assertAxis(chance, Elements.ONE));
        assertEquals(10, assertAxis(chance, Elements.TWO));
        assertEquals(0, assertAxis(chance, Elements.THREE));
        assertEquals(20, assertAxis(chance, Elements.FOUR));
    }

    // порядок ввода не имеет значения
    @Test
    public void shouldFillAxisWithoutImmortality() {
        // given
        settings.integer(Keys.ONE, 0)
                .integer(Keys.TWO, -1)
                .integer(Keys.THREE, 50)
                .integer(Keys.FOUR, 10);

        // when
        buildChance();

        // then
        assertEquals(80, chance.axis().size());
        assertEquals(0, assertAxis(chance, Elements.ONE));
        assertEquals(20, assertAxis(chance, Elements.TWO));
        assertEquals(50, assertAxis(chance, Elements.THREE));
        assertEquals(10, assertAxis(chance, Elements.FOUR));
    }

    @Test
    public void shouldFillAxis_Two() {
        // given
        settings.integer(Keys.ONE, 0)
                .integer(Keys.TWO, 100)
                .integer(Keys.THREE, 0)
                .integer(Keys.FOUR, 0);

        // when
        buildChance();

        // then
        assertEquals(100, chance.axis().size());
        assertEquals(0, assertAxis(chance, Elements.ONE));
        assertEquals(100, assertAxis(chance, Elements.TWO));
        assertEquals(0, assertAxis(chance, Elements.THREE));
        assertEquals(0, assertAxis(chance, Elements.FOUR));
    }

    @Test
    public void shouldAxisIsEmpty() {
        // given
        settings.integer(Keys.ONE, 0)
                .integer(Keys.TWO, 0)
                .integer(Keys.THREE, 0)
                .integer(Keys.FOUR, 0);

        // when
        buildChance();

        // then
        assertEquals(0, chance.axis().size());
    }

    // параметр "-1" > 1
    // -1 (4 шт) -> (100 - (sum = 0)) / countOfMinus (4) = 25
    // axis.size() = 100
    @Test
    public void shouldFillAxisIfAllParametersMinus() {
        // given
        settings.integer(Keys.ONE, -1)
                .integer(Keys.TWO, -1)
                .integer(Keys.THREE, -1)
                .integer(Keys.FOUR, -1);

        // when
        buildChance();

        // then
        assertEquals(100, chance.axis().size());
        assertEquals(25, assertAxis(chance, Elements.ONE));
        assertEquals(25, assertAxis(chance, Elements.TWO));
        assertEquals(25, assertAxis(chance, Elements.THREE));
        assertEquals(25, assertAxis(chance, Elements.FOUR));
    }

    // параметр "0" -> не участвует
    // параметра "-1" нету
    // 60 + 50 + 40 = 150, 150 > 100
    // 60 -> 60 * 100 / 150 = 40
    // 50 -> 50 * 100 / 150 = 33
    // 40 -> 40 * 100 / 150 = 26
    // axis.size() = 99
    @Test
    public void shouldFillAxisChangeParametersWithoutMinus() {
        // given
        settings.integer(Keys.ONE, 60)
                .integer(Keys.TWO, 50)
                .integer(Keys.THREE, 0)
                .integer(Keys.FOUR, 40);

        // when
        buildChance();

        // then
        assertEquals(99, chance.axis().size());
        assertEquals(40, assertAxis(chance, Elements.ONE));
        assertEquals(33, assertAxis(chance, Elements.TWO));
        assertEquals(0, assertAxis(chance, Elements.THREE));
        assertEquals(26, assertAxis(chance, Elements.FOUR));
    }

    // параметра "0" нету
    // параметр "-1" (1 шт)
    // 60 + 50 + 40 = 150, 150 > 100
    // 60 -> 60 * (100 - 30) / 150 = 28
    // 50 -> 50 * (100 - 30) / 150 = 23
    // 40 -> 40 * (100 - 30) / 150 = 18
    // -1 (1 шт) -> (100 - (sum = 69)) / 2 = 15
    // axis.size() = 84
    @Test
    public void shouldFillAxisChangeParametersWithMinus() {
        // given
        settings.integer(Keys.ONE, 60)
                .integer(Keys.TWO, 50)
                .integer(Keys.THREE, -1)
                .integer(Keys.FOUR, 40);

        // when
        buildChance();

        // then
        assertEquals(84, chance.axis().size());
        assertEquals(28, assertAxis(chance, Elements.ONE));
        assertEquals(23, assertAxis(chance, Elements.TWO));
        assertEquals(15, assertAxis(chance, Elements.THREE));
        assertEquals(18, assertAxis(chance, Elements.FOUR));
    }

    enum Elements implements CharElements {

        ONE('1'),
        TWO('2'),
        THREE('3'),
        FOUR('4'),
        NONE(' ');

        final char ch;

        Elements(char ch) {
            this.ch = ch;
        }

        @Override
        public char ch() {
            return ch;
        }

        @Override
        public String toString() {
            return String.valueOf(ch);
        }

        public static Elements valueOf(char ch) {
            for (Elements el : Elements.values()) {
                if (el.ch == ch) {
                    return el;
                }
            }
            throw new IllegalArgumentException("No such element for " + ch);
        }
    }

    enum Keys implements SettingsReader.Key {

        ONE("One"),
        TWO("Two"),
        THREE("Three"),
        FOUR("Four"),
        NONE("None");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }
}
