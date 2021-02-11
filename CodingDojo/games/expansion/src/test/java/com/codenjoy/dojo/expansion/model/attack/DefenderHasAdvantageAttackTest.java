package com.codenjoy.dojo.expansion.model.attack;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.expansion.model.Elements;
import com.codenjoy.dojo.expansion.model.levels.Cell;
import com.codenjoy.dojo.expansion.model.levels.items.HeroForces;
import com.codenjoy.dojo.expansion.services.SettingsWrapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.expansion.services.SettingsWrapper.data;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksandr_Baglai on 2017-09-12.
 */
public class DefenderHasAdvantageAttackTest {

    public static class Forces extends HeroForces {
        private boolean removed;
        private int startCount;
        private int decrease;

        public Forces(int count, Elements element) {
            this.count = count;
            this.startCount = count;
            this.element = element;
            removed = false;
        }

        public static Forces get(String code) {
            int count = Integer.valueOf(code.substring(0, code.length() - 1));
            char ch = code.charAt(code.length() - 1);
            Elements element = Elements.valueOf(ch);
            return new Forces(count, element);
        }

        @Override
        public Cell removeFromCell() {
            removed = true;
            return super.removeFromCell();
        }

        @Override
        public int leave(int count, int countToStay) {
            this.decrease = count;
            return super.leave(count, countToStay);
        }

        @Override
        public String toString() {
            return String.format("%s%s%s=(%s-%s)",
                    removed?'-':'+',
                    count,
                    element.ch(),
                    startCount,
                    decrease);
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
        assertAttack("[-2♥=(2-0), -5♦=(5-0), -7♣=(7-0), +5♠=(12-7)]", "2♥", "5♦", "7♣", "12♠");
        assertAttack("[-2♥=(2-0), -5♦=(5-0), -0♣=(12-12), -12♠=(12-0)]", "2♥", "5♦", "12♣", "12♠");
        assertAttack("[+5♥=(5-0)]", "5♥");
        assertAttack("[+3♥=(7-4), -4♦=(4-0)]", "7♥", "4♦");
        assertAttack("[-2♦=(2-0), -5♠=(5-0), -7♣=(7-0), +5♥=(12-7)]", "2♦", "5♠", "7♣", "12♥");
        assertAttack("[+1♥=(1-0)]", "1♥");
        assertAttack("[+7♦=(7-0)]", "7♦");
        assertAttack("[-0♦=(1-1), -1♥=(1-0)]", "1♦", "1♥");
        assertAttack("[-1♦=(1-0), +1♥=(2-1)]", "1♦", "2♥");
        assertAttack("[+1♦=(2-1), -1♥=(1-0)]", "2♦", "1♥");
        assertAttack("[-0♦=(12-12), -12♠=(12-0)]", "12♦", "12♠");
        assertAttack("[-12♦=(12-0), +1♠=(13-12)]", "12♦", "13♠");
        assertAttack("[+1♦=(13-12), -12♠=(12-0)]", "13♦", "12♠");
        assertAttack("[-12♦=(12-0), -0♠=(12-12), -12♣=(12-0)]", "12♦", "12♠", "12♣");
        assertAttack("[-12♦=(12-0), -0♠=(12-12), -12♣=(12-0), -12♥=(12-0)]", "12♦", "12♠", "12♣", "12♥");
        assertAttack("[-12♦=(12-0), -12♠=(12-0), +1♣=(13-12), -12♥=(12-0)]", "12♦", "12♠", "13♣", "12♥");
    }

    @Test
    public void testAdvantage1_3() {
        data.defenderAdvantage(1.3);

        assertAttack("[-10♦=(10-0), +85♥=(98-13)]", "10♦", "98♥");
        assertAttack("[]");

		assertAttack("[+5♣=(5-0)]", "5♣");
        assertAttack("[+5♥=(5-0)]", "5♥");
        assertAttack("[+1♥=(1-0)]", "1♥");
        assertAttack("[+7♦=(7-0)]", "7♦");

        assertAttack("[+2♥=(12-10), -13♦=(13-0)]", "12♥", "13♦");
        assertAttack("[+2♥=(12-10), -6♦=(6-0), -7♣=(7-0)]", "12♥", "6♦", "7♣");
        assertAttack("[+2♥=(12-10), -6♦=(6-0), -5♣=(5-0), -2♠=(2-0)]", "12♥", "6♦", "5♣", "2♠");

        assertAttack("[-10♥=(10-0), +5♦=(20-15), -15♣=(15-0)]", "10♥", "20♦", "15♣");
        assertAttack("[-10♥=(10-0), +5♦=(20-15), -15♣=(15-0), -7♠=(7-0)]", "10♥", "20♦", "15♣", "7♠");
        assertAttack("[-10♥=(10-0), -5♦=(5-0), -15♣=(15-0), +5♠=(20-15)]", "10♥", "5♦", "15♣", "20♠");

        assertAttack("[-10♦=(10-0), +7♠=(20-13)]", "10♦", "20♠");
        assertAttack("[-10♦=(10-0), +9♠=(19-10), -1♣=(1-0), -2♥=(2-0)]", "10♦", "19♠", "1♣", "2♥");
        assertAttack("[-10♦=(10-0), -2♥=(2-0), -1♣=(1-0), +9♠=(19-10)]", "10♦", "2♥", "1♣", "19♠");
        assertAttack("[-10♦=(10-0), +6♥=(17-11), -2♣=(2-0)]", "10♦", "17♥", "2♣");

        assertAttack("[-0♦=(10-10), -13♥=(13-0)]", "10♦", "13♥");
        assertAttack("[-10♦=(10-0), -0♠=(100-100), -100♣=(100-0)]", "10♦", "100♠", "100♣");
        assertAttack("[-10♦=(10-0), -0♠=(100-100), -100♣=(100-0), -20♥=(20-0)]", "10♦", "100♠", "100♣", "20♥");
    }

    @Test
    public void testAdvantage2() {
        data.defenderAdvantage(2.0);

        assertAttack("[]");

        assertAttack("[+5♣=(5-0)]", "5♣");
        assertAttack("[+5♥=(5-0)]", "5♥");
        assertAttack("[+1♥=(1-0)]", "1♥");
        assertAttack("[+7♦=(7-0)]", "7♦");

        assertAttack("[+6♥=(12-6), -13♦=(13-0)]", "12♥", "13♦");
        assertAttack("[+6♥=(12-6), -6♦=(6-0), -7♣=(7-0)]", "12♥", "6♦", "7♣");
        assertAttack("[+6♥=(12-6), -6♦=(6-0), -5♣=(5-0), -2♠=(2-0)]", "12♥", "6♦", "5♣", "2♠");

        assertAttack("[+5♥=(10-5), -10♦=(10-0)]", "10♥", "10♦");
        assertAttack("[-0♥=(10-10), -10♦=(10-0), -10♣=(10-0)]", "10♥", "10♦", "10♣");
        assertAttack("[-10♥=(10-0), -0♦=(10-10), -10♣=(10-0), -10♠=(10-0)]", "10♥", "10♦", "10♣", "10♠");

        assertAttack("[-0♥=(10-10), -20♦=(20-0)]", "10♥", "20♦");
        assertAttack("[-10♥=(10-0), -10♦=(10-0), +10♣=(20-10)]", "10♥", "10♦", "20♣");
        assertAttack("[-10♥=(10-0), -10♦=(10-0), -10♣=(10-0), +10♠=(20-10)]", "10♥", "10♦", "10♣", "20♠");

        assertAttack("[-10♥=(10-0), +20♦=(40-20)]", "10♥", "40♦");
        assertAttack("[-10♥=(10-0), -10♦=(10-0), +30♣=(40-10)]", "10♥", "10♦", "40♣");

        assertAttack("[-10♥=(10-0), -10♦=(10-0), -20♣=(20-0), +20♠=(40-20)]", "10♥", "10♦", "20♣", "40♠");
        assertAttack("[-10♥=(10-0), -10♦=(10-0), -19♣=(19-0), +21♠=(40-19)]", "10♥", "10♦", "19♣", "40♠");
        assertAttack("[-10♥=(10-0), -10♦=(10-0), -11♣=(11-0), +29♠=(40-11)]", "10♥", "10♦", "11♣", "40♠");
        assertAttack("[-10♥=(10-0), -10♦=(10-0), -10♣=(10-0), +30♠=(40-10)]", "10♥", "10♦", "10♣", "40♠");

        assertAttack("[-10♥=(10-0), -10♦=(10-0), -9♣=(9-0), +30♠=(40-10)]", "10♥", "10♦", "9♣", "40♠");
        assertAttack("[-10♥=(10-0), -10♦=(10-0), -5♣=(5-0), +30♠=(40-10)]", "10♥", "10♦", "5♣", "40♠");

        assertAttack("[-10♥=(10-0), -10♦=(10-0), -10♣=(10-0), +30♠=(40-10)]", "10♥", "10♦", "10♣", "40♠");
        assertAttack("[-10♥=(10-0), -9♦=(9-0), -9♣=(9-0), +31♠=(40-9)]", "10♥", "9♦", "9♣", "40♠");
        assertAttack("[-10♥=(10-0), -8♦=(8-0), -8♣=(8-0), +32♠=(40-8)]", "10♥", "8♦", "8♣", "40♠");
        assertAttack("[-10♥=(10-0), -7♦=(7-0), -7♣=(7-0), +33♠=(40-7)]", "10♥", "7♦", "7♣", "40♠");
        assertAttack("[-10♥=(10-0), -6♦=(6-0), -6♣=(6-0), +32♠=(40-8)]", "10♥", "6♦", "6♣", "40♠");
        assertAttack("[-10♥=(10-0), -5♦=(5-0), -5♣=(5-0), +30♠=(40-10)]", "10♥", "5♦", "5♣", "40♠");

        assertAttack("[-10♥=(10-0), -10♦=(10-0), -0♣=(40-40), -40♠=(40-0)]", "10♥", "10♦", "40♣", "40♠");
        assertAttack("[-10♥=(10-0), -20♦=(20-0), -0♣=(40-40), -40♠=(40-0)]", "10♥", "20♦", "40♣", "40♠");
        assertAttack("[-10♥=(10-0), -0♦=(40-40), -40♣=(40-0), -40♠=(40-0)]", "10♥", "40♦", "40♣", "40♠");

        assertAttack("[-10♥=(10-0), -40♦=(40-0), -40♣=(40-0), +1♠=(41-40)]", "10♥", "40♦", "40♣", "41♠");
        assertAttack("[-10♥=(10-0), -40♦=(40-0), -0♣=(41-41), -41♠=(41-0)]", "10♥", "40♦", "41♣", "41♠");
        assertAttack("[-10♥=(10-0), +1♦=(42-41), -41♣=(41-0), -41♠=(41-0)]", "10♥", "42♦", "41♣", "41♠");

        assertAttack("[-10♥=(10-0), +5♦=(20-15), -15♣=(15-0)]", "10♥", "20♦", "15♣");
        assertAttack("[-10♥=(10-0), +5♦=(20-15), -15♣=(15-0), -7♠=(7-0)]", "10♥", "20♦", "15♣", "7♠");
        assertAttack("[-10♥=(10-0), -5♦=(5-0), -15♣=(15-0), +5♠=(20-15)]", "10♥", "5♦", "15♣", "20♠");

        assertAttack("[-0♦=(10-10), -20♠=(20-0)]", "10♦", "20♠");
        assertAttack("[-10♦=(10-0), +2♠=(19-17), -1♣=(1-0), -2♥=(2-0)]", "10♦", "19♠", "1♣", "2♥");
        assertAttack("[-10♦=(10-0), -2♥=(2-0), -1♣=(1-0), +2♠=(19-17)]", "10♦", "2♥", "1♣", "19♠");
        assertAttack("[+1♦=(10-9), -17♥=(17-0), -2♣=(2-0)]", "10♦", "17♥", "2♣");

        assertAttack("[+4♦=(10-6), -13♥=(13-0)]", "10♦", "13♥");
        assertAttack("[-10♦=(10-0), +80♠=(100-20)]", "10♦", "100♠");
        assertAttack("[-10♦=(10-0), -0♠=(100-100), -100♣=(100-0)]", "10♦", "100♠", "100♣");
        assertAttack("[-10♦=(10-0), -0♠=(100-100), -100♣=(100-0), -20♥=(20-0)]", "10♦", "100♠", "100♣", "20♥");
    }

    private void assertAttack(String expected, String... forcesCode) {
        List<HeroForces> forces = Arrays.stream(forcesCode).map(Forces::get).collect(toList());
        List<HeroForces> clone = new LinkedList<>(forces);
        new DefenderHasAdvantageAttack().calculate(forces);
        assertEquals(expected, clone.toString());
    }
}