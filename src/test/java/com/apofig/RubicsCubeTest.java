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
                "BBB", cube.getFace(Face.LEFT));

        assertEquals(
                "GGG" +
                "GGG" +
                "GGG", cube.getFace(Face.RIGHT));

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

    @Test
    public void testRunCommandF() {
        RubicsCube cube = new RubicsCube();

        cube.doCommand("F");

        assertEquals(
                "    WWW        \n" +
                "    WWW        \n" +
                "    BBB        \n" +
                "BBY OOO WGG RRR\n" +
                "BBY OOO WGG RRR\n" +
                "BBY OOO WGG RRR\n" +
                "    GGG        \n" +
                "    YYY        \n" +
                "    YYY        \n", cube.toString());
    }

    @Test
    public void testRunCommandF2() {
        RubicsCube cube = new RubicsCube();

        cube.doCommand("F2");

        assertEquals(
            "    WWW        \n" +
            "    WWW        \n" +
            "    YYY        \n" +
            "BBG OOO BGG RRR\n" +
            "BBG OOO BGG RRR\n" +
            "BBG OOO BGG RRR\n" +
            "    WWW        \n" +
            "    YYY        \n" +
            "    YYY        \n", cube.toString());
    }

    @Test
    public void testRunCommandF_() {
        RubicsCube cube = new RubicsCube();

        cube.doCommand("F'");

        assertEquals(
                "    WWW        \n" +
                "    WWW        \n" +
                "    GGG        \n" +
                "BBW OOO YGG RRR\n" +
                "BBW OOO YGG RRR\n" +
                "BBW OOO YGG RRR\n" +
                "    BBB        \n" +
                "    YYY        \n" +
                "    YYY        \n", cube.toString());
    }

    @Test
    public void testRunCommandR() {
        RubicsCube cube = new RubicsCube();

        cube.doCommand("R");

        assertEquals(
                "    WWO        \n" +
                "    WWO        \n" +
                "    WWO        \n" +
                "BBB OOY GGG WRR\n" +
                "BBB OOY GGG WRR\n" +
                "BBB OOY GGG WRR\n" +
                "    YYR        \n" +
                "    YYR        \n" +
                "    YYR        \n", cube.toString());
    }
}
