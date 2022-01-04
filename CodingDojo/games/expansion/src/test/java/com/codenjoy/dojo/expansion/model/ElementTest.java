package com.codenjoy.dojo.expansion.model;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.games.expansion.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ElementTest {

    @Test
    public void testGetForce() {
        assertEquals(Element.FORCE1, Element.getForce(0));
        assertEquals(Element.FORCE2, Element.getForce(1));
        assertEquals(Element.FORCE3, Element.getForce(2));
        assertEquals(Element.FORCE4, Element.getForce(3));

        assertEquals(Element.FORCE1, Element.valueOf('♥'));
        assertEquals(Element.FORCE2, Element.valueOf('♦'));
        assertEquals(Element.FORCE3, Element.valueOf('♣'));
        assertEquals(Element.FORCE4, Element.valueOf('♠'));
    }

    @Test
    public void testGetAllElements() {
        Element.valueOf('♥'); // because of lazy
        assertEquals("{.=., └=└, -=-, ┐=┐, ┌=┌, $=$, O=O,  = , " +
                "│=│, ─=─, G=G, ╝=╝, F=F, E=E, ╚=╚, B=B, ╗=╗, " +
                "╔=╔, ║=║, ═=═, ♦=♦, ♥=♥, 4=4, 3=3, ♣=♣, 2=2, " +
                "1=1, ┘=┘, ♠=♠}", Element.elementsMap.toString());
    }
}
