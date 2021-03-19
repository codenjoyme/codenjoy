package com.codenjoy.dojo.services.settings;

import com.codenjoy.dojo.client.TestGameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.printer.CharElements;
import org.junit.Before;
import org.junit.Test;

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
        chance = new Chance(dice);

        Parameter<Integer> parameter1 = settings.integerValue(ONE);
        Parameter<Integer> parameter2 = settings.integerValue(TWO);
        Parameter<Integer> parameter3 = settings.integerValue(THREE);
        Parameter<Integer> parameter4 = settings.integerValue(FOUR);

        chance.put(FIRST, parameter1);
        chance.put(SECOND, parameter2);
        chance.put(THIRD, parameter3);
        chance.put(FOURTH, parameter4);

        parameter1.onChange(value -> chance.run());
        parameter2.onChange(value -> chance.run());
        parameter3.onChange(value -> chance.run());
        parameter4.onChange(value -> chance.run());

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
        settings.integer(ONE, 50)
                .integer(TWO, 10)
                .integer(THREE, 0)
                .integer(FOUR, -1);

        // when
        buildChance();

        // then
        assertEquals(80, chance.axis().size());
        assertEquals(50, axis(FIRST));
        assertEquals(10, axis(SECOND));
        assertEquals(0, axis(THIRD));
        assertEquals(20, axis(FOURTH));
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
        assertEquals(80, chance.axis().size());
        assertEquals(0, axis(FIRST));
        assertEquals(20, axis(SECOND));
        assertEquals(50, axis(THIRD));
        assertEquals(10, axis(FOURTH));
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
        assertEquals(100, chance.axis().size());
        assertEquals(0, axis(FIRST));
        assertEquals(100, axis(SECOND));
        assertEquals(0, axis(THIRD));
        assertEquals(0, axis(FOURTH));
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
        assertEquals(100, chance.axis().size());
        assertEquals(25, axis(FIRST));
        assertEquals(25, axis(SECOND));
        assertEquals(25, axis(THIRD));
        assertEquals(25, axis(FOURTH));
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
        assertEquals(99, chance.axis().size());
        assertEquals(40, axis(FIRST));
        assertEquals(33, axis(SECOND));
        assertEquals(0, axis(THIRD));
        assertEquals(26, axis(FOURTH));
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
        assertEquals(84, chance.axis().size());
        assertEquals(28, axis(FIRST));
        assertEquals(23, axis(SECOND));
        assertEquals(15, axis(THIRD));
        assertEquals(18, axis(FOURTH));
    }

    @Test
    public void shouldChangeSettings_willChangeAxisImmediately() {
        // given
        settings.integer(ONE, 25)
                .integer(TWO, 25)
                .integer(THREE, 25)
                .integer(FOUR, 25);

        buildChance();

        assertEquals(100, chance.axis().size());
        assertEquals(25, axis(FIRST));
        assertEquals(25, axis(SECOND));
        assertEquals(25, axis(THIRD));
        assertEquals(25, axis(FOURTH));

        // when
        settings.integer(ONE, 0);

        // then
        assertEquals(75, chance.axis().size());
        assertEquals(0, axis(FIRST));
        assertEquals(25, axis(SECOND));
        assertEquals(25, axis(THIRD));
        assertEquals(25, axis(FOURTH));

        // when
        settings.integer(TWO, 1);
        settings.integer(THREE, 1);

        // then
        assertEquals(27, chance.axis().size());
        assertEquals(0, axis(FIRST));
        assertEquals(1, axis(SECOND));
        assertEquals(1, axis(THIRD));
        assertEquals(25, axis(FOURTH));

        // when
        settings.integer(FOUR, -1);

        // then
        assertEquals(51, chance.axis().size());
        assertEquals(0, axis(FIRST));
        assertEquals(1, axis(SECOND));
        assertEquals(1, axis(THIRD));
        assertEquals(49, axis(FOURTH)); // TODO вот тут немного не очевидно
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
}
