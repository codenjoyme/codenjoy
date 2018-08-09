package com.codenjoy.dojo.kata.model.levels.algorithms;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


//import org.approvaltests.legacycode.LegacyApprovals;
//import org.approvaltests.legacycode.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongDivisionAlgorithmTest {

    @Test
    public void shouldWork() throws Exception {
        LongDivisionAlgorithm algorithm = new LongDivisionAlgorithm();
        assertEquals("0.5", algorithm.get("1, 2"));
        assertEquals("1", algorithm.get("1, 1"));
        assertEquals("1", algorithm.get("5, 5"));
        assertEquals("11", algorithm.get("55, 5"));
        assertEquals("1.25", algorithm.get("55, 44"));
        assertEquals("0", algorithm.get("0, 56"));
        assertEquals("56", algorithm.get("56, 1"));
        assertEquals("-0.5", algorithm.get("1, -2"));
        assertEquals("-0.5", algorithm.get("-1, 2"));
        assertEquals("0.5", algorithm.get("-1, -2"));
        assertEquals("0.001", algorithm.get("1, 1000"));
        assertEquals("1.2(4)", algorithm.get("56, 45"));
        assertEquals("1.00(90)", algorithm.get("111, 110"));
        assertEquals("10.0(90)", algorithm.get("111, 11"));
        assertEquals("1010.0(90)", algorithm.get("11111, 11"));
        assertEquals("0.0(495)", algorithm.get("-11, -222"));
        assertEquals("-5.0(45)", algorithm.get("111, -22"));
        assertEquals("0.000(3)", algorithm.get("1, 3000"));
        assertEquals("1.1(153846)", algorithm.get("87, 78"));
        assertEquals("0.803(571428)", algorithm.get("45, 56"));
        assertEquals("1.(593984962406015037)", algorithm.get("212, 133"));
        assertEquals("96.6(1739130434782608695652)", algorithm.get("11111, 115"));
        assertEquals("0.3(5652173913043478260869)", algorithm.get("123, 345"));
        assertEquals("0.8576942320118070532237143486041032667124906968586017840186012266888836921194851616559161340739231484", algorithm.get("66666666, 77727777"));
        assertEquals("8.5769423201180705322371434860410326671249069685860178401860122668888369211948516165591613407392314847", algorithm.get("666666660, 77727777"));
        assertEquals("85.7694309253951322673994120762759564567464112247141529983428059238030371899258141588263756955847311713", algorithm.get("666666660, 7772777"));
        assertEquals("1.0(309278350515463917525773195876288659793814432989690721649484536082474226804123711340206185567010)", algorithm.get("100, 97"));
        assertEquals("0.8576942320118070532237143486041032667124906968586017840186012266888836921194851616559161340739231484", algorithm.get("66666666, 77727777"));
        assertEquals("Div by zero error!", algorithm.get("999, 0"));

        // TODO вернуть тест без approvals
        // LegacyApprovals.LockDown(this, "get", Range.get(-100, 100), Range.get(-100, 100));
    }

    public String get(Integer i1, Integer i2) {
        String input = String.format("%s, %s", i1, i2);
        return new LongDivisionAlgorithm().get(input);
    }
}
