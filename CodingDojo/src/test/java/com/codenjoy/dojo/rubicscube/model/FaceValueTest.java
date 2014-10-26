package com.codenjoy.dojo.rubicscube.model;

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
