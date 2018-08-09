package com.codenjoy.dojo.rubicscube.model;

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


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 5:58
 */
public class FaceValueTest {

    @Test
    public void testUpdateLine() {
        FaceValue face = new FaceValue(Elements.BLUE);

        assertEquals(
                "BBB" +
                "BBB" +
                "BBB", face.toString());

        face.updateLine(0, new Line("YGO"));

        assertEquals(
                "YGO" +
                "BBB" +
                "BBB", face.toString());

        face.updateLine(1, new Line("GRY"));

        assertEquals(
                "YGO" +
                "GRY" +
                "BBB", face.toString());

        face.updateLine(2, new Line("ORG"));

        assertEquals(
                "YGO" +
                "GRY" +
                "ORG", face.toString());

        assertEquals("YGO", face.getLine(0).toString());
        assertEquals("GRY", face.getLine(1).toString());
        assertEquals("ORG", face.getLine(2).toString());
    }

    @Test
    public void testUpdateRow() {
        FaceValue face = new FaceValue(Elements.BLUE);

        assertEquals(
                "BBB" +
                "BBB" +
                "BBB", face.toString());

        face.updateRow(0, new Line("YGO"));

        assertEquals(
                "YBB" +
                "GBB" +
                "OBB", face.toString());

        face.updateRow(1, new Line("GRY"));

        assertEquals(
                "YGB" +
                "GRB" +
                "OYB", face.toString());

        face.updateRow(2, new Line("ORG"));

        assertEquals(
                "YGO" +
                "GRR" +
                "OYG", face.toString());

        assertEquals("YGO", face.getRow(0).toString());
        assertEquals("GRY", face.getRow(1).toString());
        assertEquals("ORG", face.getRow(2).toString());
    }

    @Test
    public void testInvertLine() {
        assertEquals("OGY", new Line("YGO").invert().toString());
    }
}
