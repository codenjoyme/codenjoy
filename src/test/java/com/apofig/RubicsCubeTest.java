package com.apofig;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 4:38
 */
public class RubicsCubeTest {

    public static final String INIT =
            "    WWW        \n" +
                    "    WWW        \n" +
                    "    WWW        \n" +
                    "BBB OOO GGG RRR\n" +
                    "BBB OOO GGG RRR\n" +
                    "BBB OOO GGG RRR\n" +
                    "    YYY        \n" +
                    "    YYY        \n" +
                    "    YYY        \n";

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
        assertCube("", INIT);

    }

    @Test
    public void testRunCommandF() {
        assertCube("F",
                "    WWW        \n" +
                "    WWW        \n" +
                "    BBB        \n" +
                "BBY OOO WGG RRR\n" +
                "BBY OOO WGG RRR\n" +
                "BBY OOO WGG RRR\n" +
                "    GGG        \n" +
                "    YYY        \n" +
                "    YYY        \n");
    }

    @Test
    public void testRunCommandF2() {
        assertCube("F2",
                "    WWW        \n" +
                "    WWW        \n" +
                "    YYY        \n" +
                "BBG OOO BGG RRR\n" +
                "BBG OOO BGG RRR\n" +
                "BBG OOO BGG RRR\n" +
                "    WWW        \n" +
                "    YYY        \n" +
                "    YYY        \n");
    }

    @Test
    public void testRunCommandF_() {
        assertCube("F'",
                "    WWW        \n" +
                "    WWW        \n" +
                "    GGG        \n" +
                "BBW OOO YGG RRR\n" +
                "BBW OOO YGG RRR\n" +
                "BBW OOO YGG RRR\n" +
                "    BBB        \n" +
                "    YYY        \n" +
                "    YYY        \n");
    }

    @Test
    public void testRunCommandR() {
        assertCube("R",
                "    WWO        \n" +
                "    WWO        \n" +
                "    WWO        \n" +
                "BBB OOY GGG WRR\n" +
                "BBB OOY GGG WRR\n" +
                "BBB OOY GGG WRR\n" +
                "    YYR        \n" +
                "    YYR        \n" +
                "    YYR        \n");
    }

    @Test
    public void testRunCommandR2() {
        assertCube("R2",
                "    WWY        \n" +
                "    WWY        \n" +
                "    WWY        \n" +
                "BBB OOR GGG ORR\n" +
                "BBB OOR GGG ORR\n" +
                "BBB OOR GGG ORR\n" +
                "    YYW        \n" +
                "    YYW        \n" +
                "    YYW        \n");
    }

    @Test
    public void testRunCommandR_() {
        assertCube("R'",
                "    WWR        \n" +
                "    WWR        \n" +
                "    WWR        \n" +
                "BBB OOW GGG YRR\n" +
                "BBB OOW GGG YRR\n" +
                "BBB OOW GGG YRR\n" +
                "    YYO        \n" +
                "    YYO        \n" +
                "    YYO        \n");
    }

    @Test
    public void testRunCommandFRF_R_() {
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

        cube.doCommand("R");

        assertEquals(
                "    WWO        \n" +
                "    WWO        \n" +
                "    BBO        \n" +
                "BBY OOG WWW BRR\n" +
                "BBY OOY GGG WRR\n" +
                "BBY OOY GGG WRR\n" +
                "    GGR        \n" +
                "    YYR        \n" +
                "    YYR        \n", cube.toString());

        cube.doCommand("F'");

        assertEquals(
                "    WWO        \n" +
                "    WWO        \n" +
                "    WGG        \n" +
                "BBO GYY RWW BRR\n" +
                "BBB OOO GGG WRR\n" +
                "BBB OOO GGG WRR\n" +
                "    YYY        \n" +
                "    YYR        \n" +
                "    YYR        \n", cube.toString());

        cube.doCommand("R'");

        assertEquals(
                "    WWW        \n" +
                "    WWW        \n" +
                "    WGB        \n" +
                "BBO GYO WGG RRR\n" +
                "BBB OOO WGG RRR\n" +
                "BBB OOG RGG YRR\n" +
                "    YYY        \n" +
                "    YYO        \n" +
                "    YYO        \n", cube.toString());

        cube.doCommand("FRF'R'FRF'R'FRF'R'FRF'R'FRF'R'");

        assertEquals(INIT, cube.toString());
    }

    @Test
    public void testRunCommandU() {
        assertCube("U",
                "    WWW        \n" +
                "    WWW        \n" +
                "    WWW        \n" +
                "OOO GGG RRR BBB\n" +
                "BBB OOO GGG RRR\n" +
                "BBB OOO GGG RRR\n" +
                "    YYY        \n" +
                "    YYY        \n" +
                "    YYY        \n");
    }

    @Test
    public void testRunCommandU2() {
        assertCube("U2",
                "    WWW        \n" +
                "    WWW        \n" +
                "    WWW        \n" +
                "GGG RRR BBB OOO\n" +
                "BBB OOO GGG RRR\n" +
                "BBB OOO GGG RRR\n" +
                "    YYY        \n" +
                "    YYY        \n" +
                "    YYY        \n");
    }

    @Test
    public void testRunCommandU_() {
        assertCube("U'",
                "    WWW        \n" +
                "    WWW        \n" +
                "    WWW        \n" +
                "RRR BBB OOO GGG\n" +
                "BBB OOO GGG RRR\n" +
                "BBB OOO GGG RRR\n" +
                "    YYY        \n" +
                "    YYY        \n" +
                "    YYY        \n");
    }

    @Test
    public void testRunCommandL() {
        assertCube("L",
                "    RWW        \n" +
                "    RWW        \n" +
                "    RWW        \n" +
                "BBB WOO GGG RRY\n" +
                "BBB WOO GGG RRY\n" +
                "BBB WOO GGG RRY\n" +
                "    OYY        \n" +
                "    OYY        \n" +
                "    OYY        \n");
    }

    @Test
    public void testRunCommandL2() {
        assertCube("L2",
                "    YWW        \n" +
                "    YWW        \n" +
                "    YWW        \n" +
                "BBB ROO GGG RRO\n" +
                "BBB ROO GGG RRO\n" +
                "BBB ROO GGG RRO\n" +
                "    WYY        \n" +
                "    WYY        \n" +
                "    WYY        \n");
    }

    @Test
    public void testRunCommandL_() {
        assertCube("L'",
                "    OWW        \n" +
                "    OWW        \n" +
                "    OWW        \n" +
                "BBB YOO GGG RRW\n" +
                "BBB YOO GGG RRW\n" +
                "BBB YOO GGG RRW\n" +
                "    RYY        \n" +
                "    RYY        \n" +
                "    RYY        \n");
    }

    @Test
    public void testRunCommandRULFCycle() {
        RubicsCube cube = new RubicsCube();

        for (int index = 0; index < 90; index++) {
            cube.doCommand("RULF");
        }

        assertEquals(INIT, cube.toString());
    }

    @Test
    public void testRunCommandRULFF_L_U_R_() {
        assertCube("RULFF'L'U'R'", INIT);
    }

    @Test
    public void testRunCommandD() {
        assertCube("D",
                "    WWW        \n" +
                "    WWW        \n" +
                "    WWW        \n" +
                "BBB OOO GGG RRR\n" +
                "BBB OOO GGG RRR\n" +
                "RRR BBB OOO GGG\n" +
                "    YYY        \n" +
                "    YYY        \n" +
                "    YYY        \n");
    }

    @Test
    public void testRunCommandD_() {
        assertCube("D'",
                "    WWW        \n" +
                "    WWW        \n" +
                "    WWW        \n" +
                "BBB OOO GGG RRR\n" +
                "BBB OOO GGG RRR\n" +
                "OOO GGG RRR BBB\n" +
                "    YYY        \n" +
                "    YYY        \n" +
                "    YYY        \n");
    }

    @Test
    public void testRunCommandRULFDD_F_L_U_R_() {
        assertCube("RULFDD'F'L'U'R'", INIT);
    }

    @Test
    public void testRunCommandB() {
        assertCube("B",
                "    GGG        \n" +
                "    WWW        \n" +
                "    WWW        \n" +
                "WBB OOO GGY RRR\n" +
                "WBB OOO GGY RRR\n" +
                "WBB OOO GGY RRR\n" +
                "    YYY        \n" +
                "    YYY        \n" +
                "    BBB        \n");
    }

    @Test
    public void testRunCommandRULFDBB_D_F_L_U_R_() {
        assertCube("RULFDBB'D'F'L'U'R'", INIT);
    }

    @Test
    public void testChess() {
        assertCube("R2L2U2D2F2B2",
                "    WYW        \n" +
                "    YWY        \n" +
                "    WYW        \n" +
                "BGB ORO GBG ROR\n" +
                "GBG ROR BGB ORO\n" +
                "BGB ORO GBG ROR\n" +
                "    YWY        \n" +
                "    WYW        \n" +
                "    YWY        \n");


        assertCube("L2 R' F D2 L' F' D U' B F' D R F2 D' L R2",
                "    WGW        \n" +
                "    GWG        \n" +
                "    WGW        \n" +
                "BRB OWO GOG RYR\n" +
                "RBR WOW OGO YRY\n" +
                "BRB OWO GOG RYR\n" +
                "    YBY        \n" +
                "    BYB        \n" +
                "    YBY        \n");

        assertCube("D' B2 F2 U2 L2 R2 U' L' B' F D U' L' R F2 D2 U2 F'",
                "    WOW        \n" +
                "    OWO        \n" +
                "    WOW        \n" +
                "BYB OBO GWG RGR\n" +
                "YBY BOB WGW GRG\n" +
                "BYB OBO GWG RGR\n" +
                "    YRY        \n" +
                "    RYR        \n" +
                "    YRY        \n");
    }

    private void assertCube(String command, String expected) {
        RubicsCube cube = new RubicsCube();
        cube.doCommand(command);
        assertEquals(expected, cube.toString());
    }
}
