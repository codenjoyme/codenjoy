package com.codenjoy.dojo.expansion.model;

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


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksandr_Baglai on 2017-09-01.
 */
public class ElementsTest {

    @Test
    public void testGetForce() {
        assertEquals(Elements.FORCE1, Elements.getForce(0));
        assertEquals(Elements.FORCE2, Elements.getForce(1));
        assertEquals(Elements.FORCE3, Elements.getForce(2));
        assertEquals(Elements.FORCE4, Elements.getForce(3));

        assertEquals(Elements.FORCE1, Elements.valueOf('♥'));
        assertEquals(Elements.FORCE2, Elements.valueOf('♦'));
        assertEquals(Elements.FORCE3, Elements.valueOf('♣'));
        assertEquals(Elements.FORCE4, Elements.valueOf('♠'));
    }

    @Test
    public void testGetAllElements() {
        Elements.valueOf('♥'); // because of lazy
        assertEquals("{.=., └=└, -=-, ┐=┐, ┌=┌, $=$, O=O,  = , " +
                "│=│, ─=─, G=G, ╝=╝, F=F, E=E, ╚=╚, B=B, ╗=╗, " +
                "╔=╔, ║=║, ═=═, ♦=♦, ♥=♥, 4=4, 3=3, ♣=♣, 2=2, " +
                "1=1, ┘=┘, ♠=♠}", Elements.elementsMap.toString());
    }
}
