package com.apofig;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 4:38
 */
public class RubicsCubeTest {

    @Test
    public void testFaces() {
        RubicsCube cube = new RubicsCube();

        assertEquals(
                "OOO" +
                "OOO" +
                "OOO", cube.getFace(Face.FRONT));

        assertEquals(
                "WWW" +
                "WWW" +
                "WWW", cube.getFace(Face.UP));

        assertEquals(
                "BBB" +
                "BBB" +
                "BBB", cube.getFace(Face.RIGHT));

        assertEquals(
                "GGG" +
                "GGG" +
                "GGG", cube.getFace(Face.LEFT));

        assertEquals(
                "YYY" +
                "YYY" +
                "YYY", cube.getFace(Face.DOWN));

        assertEquals(
                "RRR" +
                "RRR" +
                "RRR", cube.getFace(Face.BACK));
    }

    @Test
    public void testToString() {
        RubicsCube cube = new RubicsCube();

        assertEquals(
                "    WWW        \n" +
                "    WWW        \n" +
                "    WWW        \n" +
                "BBB OOO GGG RRR\n" +
                "BBB OOO GGG RRR\n" +
                "BBB OOO GGG RRR\n" +
                "    YYY        \n" +
                "    YYY        \n" +
                "    YYY        \n", cube.toString());

    }
}
