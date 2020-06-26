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
import com.codenjoy.dojo.expansion.model.levels.items.HeroForces;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

/**
 * Created by Oleksandr_Baglai on 2017-09-12.
 */
public class OneByOneAttackTest {

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

    @Test
    public void test() {
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
        assertAttack("[]");
    }

    private void assertAttack(String expected, String... forcesCode) {
        List<HeroForces> forces =
                Arrays.stream(forcesCode).map(Forces::get).collect(toList());
        new OneByOneAttack().calculate(forces);
        assertEquals(expected, forces.toString());
    }

}