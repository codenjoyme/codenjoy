package com.epam.dojo.expansion.model.attack;

import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.levels.items.Hero;
import com.epam.dojo.expansion.model.levels.items.HeroForces;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.IntUnaryOperator;

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
        assertAttack("[5♥]", "5♥");
        assertAttack("[3♥]", "7♥", "4♦");
        assertAttack("[5♥]", "2♦", "5♠", "7♣", "12♥");
        assertAttack("[1♥]", "1♥");
        assertAttack("[7♦]", "7♦");
        assertAttack("[]", "1♦", "1♥");
        assertAttack("[]", "12♦", "12♠");
        assertAttack("[]", "12♦", "12♠", "12♣");
        assertAttack("[]", "12♦", "12♠", "12♣", "12♥");
        assertAttack("[]");
    }

    private void assertAttack(String expected, String... forcesCode) {
        List<HeroForces> forces =
                Arrays.stream(forcesCode).map(Forces::get).collect(toList());
        new OneByOneAttack().calculate(forces);
        assertEquals(expected, forces.toString());
    }

}