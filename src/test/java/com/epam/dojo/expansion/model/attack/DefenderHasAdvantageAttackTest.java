package com.epam.dojo.expansion.model.attack;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.levels.items.HeroForces;
import com.epam.dojo.expansion.services.SettingsWrapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.epam.dojo.expansion.services.SettingsWrapper.data;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksandr_Baglai on 2017-09-12.
 */
public class DefenderHasAdvantageAttackTest {

    public static class Forces extends HeroForces {
        public Forces(int count, Elements element) {
            this.count = count;
            this.element = element;
        }

        public static Forces get(String code) {
            int count = Integer.valueOf(code.substring(0, code.length() - 1));
            char ch = code.charAt(code.length() - 1);
            Elements element = Elements.valueOf(ch);
            return new Forces(count, element);
        }

        @Override
        public String toString() {
            return String.valueOf(count) + element.ch();
        }
    }

    @Before
    public void setup() {
        SettingsWrapper.setup();
    }

    @Test
    public void testAdvantage1() {
        data.defenderAdvantage(1);

        assertAttack("[]");
        assertAttack("[5♠]", "2♥", "5♦", "7♣", "12♠");
        assertAttack("[]", "2♥", "5♦", "12♣", "12♠");
        assertAttack("[5♥]", "5♥");
        assertAttack("[3♥]", "7♥", "4♦");
        assertAttack("[5♥]", "2♦", "5♠", "7♣", "12♥");
        assertAttack("[1♥]", "1♥");
        assertAttack("[7♦]", "7♦");
        assertAttack("[]", "1♦", "1♥");
        assertAttack("[1♥]", "1♦", "2♥");
        assertAttack("[1♦]", "2♦", "1♥");
        assertAttack("[]", "12♦", "12♠");
        assertAttack("[1♠]", "12♦", "13♠");
        assertAttack("[1♦]", "13♦", "12♠");
        assertAttack("[]", "12♦", "12♠", "12♣");
        assertAttack("[]", "12♦", "12♠", "12♣", "12♥");
        assertAttack("[1♣]", "12♦", "12♠", "13♣", "12♥");
    }

    @Test
    public void testAdvantage1_3() {
        data.defenderAdvantage(1.3);

        assertAttack("[]");

		assertAttack("[5♣]", "5♣");
        assertAttack("[5♥]", "5♥");
        assertAttack("[1♥]", "1♥");
        assertAttack("[7♦]", "7♦");

        assertAttack("[2♥]", "12♥", "13♦");
        assertAttack("[2♥]", "12♥", "6♦", "7♣");
        assertAttack("[2♥]", "12♥", "6♦", "5♣", "2♠");

        assertAttack("[5♦]", "10♥", "20♦", "15♣");
        assertAttack("[5♦]", "10♥", "20♦", "15♣", "7♠");
        assertAttack("[5♠]", "10♥", "5♦", "15♣", "20♠");

        assertAttack("[7♠]", "10♦", "20♠");
        assertAttack("[9♠]", "10♦", "19♠", "1♣", "2♥");
        assertAttack("[9♠]", "10♦", "2♥", "1♣", "19♠");
        assertAttack("[6♥]", "10♦", "17♥", "2♣");

        assertAttack("[]", "10♦", "13♥");
        assertAttack("[]", "10♦", "100♠", "100♣");
        assertAttack("[]", "10♦", "100♠", "100♣", "20♥");
    }

    @Test
    public void testAdvantage2() {
        data.defenderAdvantage(2.0);

        assertAttack("[]");

        assertAttack("[5♣]", "5♣");
        assertAttack("[5♥]", "5♥");
        assertAttack("[1♥]", "1♥");
        assertAttack("[7♦]", "7♦");

        assertAttack("[6♥]", "12♥", "13♦");
        assertAttack("[6♥]", "12♥", "6♦", "7♣");
        assertAttack("[6♥]", "12♥", "6♦", "5♣", "2♠");

        assertAttack("[5♥]", "10♥", "10♦");
        assertAttack("[]", "10♥", "10♦", "10♣");
        assertAttack("[]", "10♥", "10♦", "10♣", "10♠");

        assertAttack("[]", "10♥", "20♦");
        assertAttack("[10♣]", "10♥", "10♦", "20♣");
        assertAttack("[10♠]", "10♥", "10♦", "10♣", "20♠");

        assertAttack("[20♦]", "10♥", "40♦");
        assertAttack("[30♣]", "10♥", "10♦", "40♣");

        assertAttack("[20♠]", "10♥", "10♦", "20♣", "40♠");
        assertAttack("[21♠]", "10♥", "10♦", "19♣", "40♠");
        assertAttack("[29♠]", "10♥", "10♦", "11♣", "40♠");
        assertAttack("[30♠]", "10♥", "10♦", "10♣", "40♠");

        assertAttack("[30♠]", "10♥", "10♦", "9♣", "40♠");
        assertAttack("[30♠]", "10♥", "10♦", "5♣", "40♠");

        assertAttack("[30♠]", "10♥", "10♦", "10♣", "40♠");
        assertAttack("[31♠]", "10♥", "9♦", "9♣", "40♠");
        assertAttack("[32♠]", "10♥", "8♦", "8♣", "40♠");
        assertAttack("[34♠]", "10♥", "7♦", "7♣", "40♠");
        assertAttack("[32♠]", "10♥", "6♦", "6♣", "40♠");
        assertAttack("[30♠]", "10♥", "5♦", "5♣", "40♠");

        assertAttack("[]", "10♥", "10♦", "40♣", "40♠");
        assertAttack("[]", "10♥", "20♦", "40♣", "40♠");
        assertAttack("[]", "10♥", "40♦", "40♣", "40♠");

        assertAttack("[1♠]", "10♥", "40♦", "40♣", "41♠");
        assertAttack("[]", "10♥", "40♦", "41♣", "41♠");
        assertAttack("[1♦]", "10♥", "42♦", "41♣", "41♠");

        assertAttack("[5♦]", "10♥", "20♦", "15♣");
        assertAttack("[5♦]", "10♥", "20♦", "15♣", "7♠");
        assertAttack("[5♠]", "10♥", "5♦", "15♣", "20♠");

        assertAttack("[]", "10♦", "20♠");
        assertAttack("[2♠]", "10♦", "19♠", "1♣", "2♥");
        assertAttack("[2♠]", "10♦", "2♥", "1♣", "19♠");
        assertAttack("[1♦]", "10♦", "17♥", "2♣");

        assertAttack("[4♦]", "10♦", "13♥");
        assertAttack("[]", "10♦", "100♠", "100♣");
        assertAttack("[]", "10♦", "100♠", "100♣", "20♥");
    }

    private void assertAttack(String expected, String... forcesCode) {
        List<HeroForces> forces =
                Arrays.stream(forcesCode).map(Forces::get).collect(toList());
        new DefenderHasAdvantageAttack().calculate(forces);
        assertEquals(expected, forces.toString());
    }
}