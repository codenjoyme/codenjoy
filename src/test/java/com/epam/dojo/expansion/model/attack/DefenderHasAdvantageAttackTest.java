package com.epam.dojo.expansion.model.attack;

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
    public void test() {
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

    private void assertAttack(String expected, String... forcesCode) {
        List<HeroForces> forces =
                Arrays.stream(forcesCode).map(Forces::get).collect(toList());
        new DefenderHasAdvantageAttack().calculate(forces);
        assertEquals(expected, forces.toString());
    }
}