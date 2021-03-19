package com.codenjoy.dojo.services.settings;

import com.codenjoy.dojo.client.TestGameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.printer.CharElements;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static com.codenjoy.dojo.services.settings.ChanceTest.Elements.*;
import static com.codenjoy.dojo.services.settings.ChanceTest.Keys.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ChanceTest {

    private TestGameSettings settings;
    private Chance chance;
    private Dice dice;

    @Before
    public void setup() {
        settings = new TestGameSettings();
        dice = mock(Dice.class);
    }

    private int axis(Elements elements) {
        return (int) chance.axis().stream()
                .filter(x -> x.equals(elements))
                .count();
    }

    private void buildChance() {
        chance = new Chance(dice, settings, new HashMap(){{
            put(FIRST, ONE);
            put(SECOND, TWO);
            put(THIRD, THREE);
            put(FOURTH, FOUR);
        }});
        chance.run();
    }

    enum Elements implements CharElements {

        FIRST('1'),
        SECOND('2'),
        THIRD('3'),
        FOURTH('4'),
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
            for (Elements el : values()) {
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

    private void assertA(String expected) {
        assertEquals(expected,
                String.format(
                        "ALL:    %s\n" +
                        "FIRST:  %s\n" +
                        "SECOND: %s\n" +
                        "THIRD:  %s\n" +
                        "FOURTH: %s\n",
                        chance.axis().size(),
                        axis(FIRST),
                        axis(SECOND),
                        axis(THIRD),
                        axis(FOURTH)));
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
        settings.integer(ONE, 50)
                .integer(TWO, 10)
                .integer(THREE, 0)
                .integer(FOUR, -1);

        // when
        buildChance();

        // then
        assertA("ALL:    80\n" +
                "FIRST:  50\n" +
                "SECOND: 10\n" +
                "THIRD:  0\n" +
                "FOURTH: 20\n");
    }

    // порядок ввода не имеет значения
    @Test
    public void shouldFillAxisWithoutImmortality() {
        // given
        settings.integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, 50)
                .integer(FOUR, 10);

        // when
        buildChance();

        // then
        assertA("ALL:    80\n" +
                "FIRST:  0\n" +
                "SECOND: 20\n" +
                "THIRD:  50\n" +
                "FOURTH: 10\n");
    }

    @Test
    public void shouldFillAxis_Two() {
        // given
        settings.integer(ONE, 0)
                .integer(TWO, 100)
                .integer(THREE, 0)
                .integer(FOUR, 0);

        // when
        buildChance();

        // then
        assertA("ALL:    100\n" +
                "FIRST:  0\n" +
                "SECOND: 100\n" +
                "THIRD:  0\n" +
                "FOURTH: 0\n");
    }

    @Test
    public void shouldAxisIsEmpty() {
        // given
        settings.integer(ONE, 0)
                .integer(TWO, 0)
                .integer(THREE, 0)
                .integer(FOUR, 0);

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
        settings.integer(ONE, -1)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, -1);

        // when
        buildChance();

        // then
        assertA("ALL:    100\n" +
                "FIRST:  25\n" +
                "SECOND: 25\n" +
                "THIRD:  25\n" +
                "FOURTH: 25\n");
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
        settings.integer(ONE, 60)
                .integer(TWO, 50)
                .integer(THREE, 0)
                .integer(FOUR, 40);

        // when
        buildChance();

        // then
        assertA("ALL:    99\n" +
                "FIRST:  40\n" +
                "SECOND: 33\n" +
                "THIRD:  0\n" +
                "FOURTH: 26\n");
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
        settings.integer(ONE, 60)
                .integer(TWO, 50)
                .integer(THREE, -1)
                .integer(FOUR, 40);

        // when
        buildChance();

        // then
        assertA("ALL:    84\n" +
                "FIRST:  28\n" +
                "SECOND: 23\n" +
                "THIRD:  15\n" +
                "FOURTH: 18\n");
    }

    @Test
    public void shouldChangeSettings_willChangeAxisImmediately() {
        // given
        settings.integer(ONE, 25)
                .integer(TWO, 25)
                .integer(THREE, 25)
                .integer(FOUR, 25);

        buildChance();

        assertA("ALL:    100\n" +
                "FIRST:  25\n" +
                "SECOND: 25\n" +
                "THIRD:  25\n" +
                "FOURTH: 25\n");

        // when
        settings.integer(ONE, 0);

        // then
        assertA("ALL:    75\n" +
                "FIRST:  0\n" +
                "SECOND: 25\n" +
                "THIRD:  25\n" +
                "FOURTH: 25\n");

        // when
        settings.integer(TWO, 1);
        settings.integer(THREE, 1);

        // then
        assertA("ALL:    27\n" +
                "FIRST:  0\n" +
                "SECOND: 1\n" +
                "THIRD:  1\n" +
                "FOURTH: 25\n");

        // when
        settings.integer(FOUR, -1);

        // then
        assertA("ALL:    51\n" +
                "FIRST:  0\n" +
                "SECOND: 1\n" +
                "THIRD:  1\n" +
                "FOURTH: 49\n");; // TODO вот тут немного не очевидно
    }
}
