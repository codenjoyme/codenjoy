package com.codenjoy.dojo.services.settings;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.client.TestGameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.printer.CharElements;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.services.settings.Chance.CHANCE_RESERVED;
import static com.codenjoy.dojo.services.settings.ChanceTest.Elements.*;
import static com.codenjoy.dojo.services.settings.ChanceTest.Keys.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
        chance = new Chance(dice, settings)
            .put(ONE, FIRST)
            .put(TWO, SECOND)
            .put(THREE, THIRD)
            .put(FOUR, FOURTH)
            .run();
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

    @Test
    public void shouldFillAxis_withoutOne_withDefault_case1() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, 50)
                .integer(TWO, 10)
                .integer(THREE, 0)
                .integer(FOUR, -1);

        // when
        buildChance();

        // then
        assertA("ALL:    90\n" +
                "FIRST:  50\n" +
                "SECOND: 10\n" +
                "THIRD:  0\n" +
                "FOURTH: 30\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 50)
                .integer(TWO, 10)
                .integer(THREE, 0)
                .integer(FOUR, -1)
                .changed();

    }

    private SettingsReader<TestGameSettings> assertThat(TestGameSettings settings) {
        TestGameSettings expected = spy(TestGameSettings.class);

        when(expected.changed()).thenAnswer(inv -> {
            assertEquals(toString(expected), toString(settings));
            return true;
        });

        return expected;
    }

    private String toString(TestGameSettings settings) {
        return settings.getParameters().stream()
                .map(param -> String.format("%s: %s", param.getName(), param.getValue()))
                .collect(Collectors.joining("\n"));
    }

    @Test
    public void shouldFillAxis_withoutOne_withDefault_case2() {
        // given
        // порядок ввода не имеет значения
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, 50)
                .integer(FOUR, 10);

        // when
        buildChance();

        // then
        assertA("ALL:    90\n" +
                "FIRST:  0\n" +
                "SECOND: 30\n" +
                "THIRD:  50\n" +
                "FOURTH: 10\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, 50)
                .integer(FOUR, 10)
                .changed();
    }

    @Test
    public void shouldFillAxis_onlyOneIsSet_withoutDefault() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
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

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, 100)
                .integer(THREE, 0)
                .integer(FOUR, 0)
                .changed();
    }

    @Test
    public void shouldFillAxis_noOneAreSet_withoutDefault() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, 0)
                .integer(THREE, 0)
                .integer(FOUR, 0);

        // when
        buildChance();

        // then
        assertA("ALL:    0\n" +
                "FIRST:  0\n" +
                "SECOND: 0\n" +
                "THIRD:  0\n" +
                "FOURTH: 0\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, 0)
                .integer(THREE, 0)
                .integer(FOUR, 0)
                .changed();
    }

    @Test
    public void shouldFillAxis_withAllDefaults() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, -1)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, -1);

        // when
        buildChance();

        // then
        assertA("ALL:    28\n" +
                "FIRST:  7\n" +
                "SECOND: 7\n" +
                "THIRD:  7\n" +
                "FOURTH: 7\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, -1)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, -1)
                .changed();
    }

    @Test
    public void shouldFillAxis_onlyOneDefaults() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, 0)
                .integer(FOUR, 0);

        // when
        buildChance();

        // then
        assertA("ALL:    30\n" +
                "FIRST:  0\n" +
                "SECOND: 30\n" +
                "THIRD:  0\n" +
                "FOURTH: 0\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, 0)
                .integer(FOUR, 0)
                .changed();
    }

    @Test
    public void shouldFillAxis_onlyTwoDefaults() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, 0);

        // when
        buildChance();

        // then
        assertA("ALL:    30\n" +
                "FIRST:  0\n" +
                "SECOND: 15\n" +
                "THIRD:  15\n" +
                "FOURTH: 0\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, 0)
                .changed();
    }

    @Test
    public void shouldFillAxis_onlyThreeDefaults() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, -1)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, 0);

        // when
        buildChance();

        // then
        assertA("ALL:    30\n" +
                "FIRST:  10\n" +
                "SECOND: 10\n" +
                "THIRD:  10\n" +
                "FOURTH: 0\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, -1)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, 0)
                .changed();
    }

    @Test
    public void shouldFillAxis_withoutOne_withoutDefault() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, 60)
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

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 40)  // changed
                .integer(TWO, 33)  // changed
                .integer(THREE, 0)
                .integer(FOUR, 26) // changed
                .changed();
    }

    @Test
    public void shouldFillAxis_allAreSet_withDefault() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, 60)
                .integer(TWO, 50)
                .integer(THREE, -1)
                .integer(FOUR, 40);

        // when
        buildChance();

        // then
        assertA("ALL:    99\n" +
                "FIRST:  28\n" +
                "SECOND: 23\n" +
                "THIRD:  30\n" +
                "FOURTH: 18\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 28)   // changed
                .integer(TWO, 23)   // changed
                .integer(THREE, -1)
                .integer(FOUR, 18)  // changed
                .changed();
    }

    @Test
    public void shouldChangeAxisImmediately_whenChangeAnySetting() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, 25)
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

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, 25)
                .integer(THREE, 25)
                .integer(FOUR, 25)
                .changed();

        // when
        settings.integer(TWO, 1)
                .integer(THREE, 1);

        // then
        assertA("ALL:    27\n" +
                "FIRST:  0\n" +
                "SECOND: 1\n" +
                "THIRD:  1\n" +
                "FOURTH: 25\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, 1)
                .integer(THREE, 1)
                .integer(FOUR, 25)
                .changed();

        // when
        settings.integer(FOUR, -1);

        // then
        assertA("ALL:    32\n" +
                "FIRST:  0\n" +
                "SECOND: 1\n" +
                "THIRD:  1\n" +
                "FOURTH: 30\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, 1)
                .integer(THREE, 1)
                .integer(FOUR, -1)
                .changed();
    }

    @Test
    public void shouldFixSettings_ifSumIsMoreThan100() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, 100)
                .integer(TWO, 100)
                .integer(THREE, 100)
                .integer(FOUR, 100);

        // when
        buildChance();

        // then
        assertA("ALL:    100\n" +
                "FIRST:  25\n" +
                "SECOND: 25\n" +
                "THIRD:  25\n" +
                "FOURTH: 25\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 25)   // changed
                .integer(TWO, 25)   // changed
                .integer(THREE, 25) // changed
                .integer(FOUR, 25)  // changed
                .changed();
    }

    @Test
    public void shouldChangeAxisImmediately_whenChangedReserved() {
        // given
        settings.integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, 0);

        // when
        buildChance();

        // then
        assertA("ALL:    30\n" +
                "FIRST:  0\n" +
                "SECOND: 15\n" +
                "THIRD:  15\n" +
                "FOURTH: 0\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 30)
                .integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, 0)
                .changed();

        // when
        settings.integer(CHANCE_RESERVED, 50);

        // then
        assertA("ALL:    50\n" +
                "FIRST:  0\n" +
                "SECOND: 25\n" +
                "THIRD:  25\n" +
                "FOURTH: 0\n");

        assertThat(settings)
                .integer(CHANCE_RESERVED, 50)   // changed
                .integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, 0)
                .changed();
    }

    @Test
    public void shouldSetDefaultReserved_whenNotSet() {
        // given
        settings.integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, 0);

        // when
        buildChance();

        // then
        assertA("ALL:    30\n" +
                "FIRST:  0\n" +
                "SECOND: 15\n" +
                "THIRD:  15\n" +
                "FOURTH: 0\n");

        assertThat(settings)
                .integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, 0)
                .integer(CHANCE_RESERVED, 30)  // added to end
                .changed();

        // when
        settings.integer(CHANCE_RESERVED, 50);

        // then
        assertA("ALL:    50\n" +
                "FIRST:  0\n" +
                "SECOND: 25\n" +
                "THIRD:  25\n" +
                "FOURTH: 0\n");

        assertThat(settings)
                .integer(ONE, 0)
                .integer(TWO, -1)
                .integer(THREE, -1)
                .integer(FOUR, 0)
                .integer(CHANCE_RESERVED, 50)  // changed
                .changed();
    }
}
