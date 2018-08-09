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
 * Time: 4:38
 */
public class CubeTest {

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
        Cube cube = new Cube();

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
        Cube cube = new Cube();

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
        Cube cube = new Cube();

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

    /**
     * @autor http://golovolomka.hobby.ru/cubepic/cubepic.htm
     */
    @Test
    public void testFigures() {
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

        assertCube("L2 R' B2 F2 D2 B2 F2 L2 R2 U2 R'",
                "    OWO        \n" +
                "    WWW        \n" +
                "    OWO        \n" +
                "GBG WOW BGB YRY\n" +
                "BBB OOO GGG RRR\n" +
                "GBG WOW BGB YRY\n" +
                "    RYR        \n" +
                "    YYY        \n" +
                "    RYR        \n");

        assertCube("R2 U' F2 D U' L2 B2 F2 U' F' L2 R2 D2 U2 B R2 F2",
                "    OWO        \n" +
                "    WWW        \n" +
                "    OWO        \n" +
                "YBY GOG WGW BRB\n" +
                "BBB OOO GGG RRR\n" +
                "YBY GOG WGW BRB\n" +
                "    RYR        \n" +
                "    YYY        \n" +
                "    RYR        \n");

        assertCube("D' U L' R B' F D' U",
                "    OOO        \n" +
                "    OWO        \n" +
                "    OOO        \n" +
                "YYY GGG WWW BBB\n" +
                "YBY GOG WGW BRB\n" +
                "YYY GGG WWW BBB\n" +
                "    RRR        \n" +
                "    RYR        \n" +
                "    RRR        \n");

        assertCube("L2 R2 D U' L' R B' F D U'",
                "    ORO        \n" +
                "    RWR        \n" +
                "    ORO        \n" +
                "YWY GBG WYW BGB\n" +
                "WBW BOB YGY GRG\n" +
                "YWY GBG WYW BGB\n" +
                "    ROR        \n" +
                "    OYO        \n" +
                "    ROR        \n");

        assertCube("U F2 U' B' U2 B U' F2 U' R' U2 B' U2 B R",
                "    GWG        \n" +
                "    WWW        \n" +
                "    GWW        \n" +
                "RBR WOO GGO YRY\n" +
                "BBB OOO GGG RRR\n" +
                "BBR WOW OGO YRR\n" +
                "    BYB        \n" +
                "    YYY        \n" +
                "    YYB        \n");

        assertCube("U' B2 R2 U2 F' D2 L' F2 U' F2 D2 F U2 R' U2",
                "    OWO        \n" +
                "    OWW        \n" +
                "    OOO        \n" +
                "GGG WWW BGB YRY\n" +
                "GBB OOW BGG RRY\n" +
                "GBG WOW BBB YYY\n" +
                "    RYR        \n" +
                "    YYR        \n" +
                "    RRR        \n");

        assertCube("F2 D' R2 D' L' U' L' R B D' U B L F2 L U2",
                "    OOO        \n" +
                "    OWW        \n" +
                "    OWO        \n" +
                "YYY GOG WGW BBB\n" +
                "BBY GOO GGW BRR\n" +
                "YBY GGG WWW BRB\n" +
                "    RRR        \n" +
                "    YYR        \n" +
                "    RYR        \n");

        assertCube("U B2 L D B' F L' D U' L' R F' D2 R'",
                "    OWO        \n" +
                "    WWO        \n" +
                "    OOO        \n" +
                "YBY GGG WWW BRB\n" +
                "YBB OOG WGG RRB\n" +
                "YYY GOG WGW BBB\n" +
                "    RYR        \n" +
                "    RYY        \n" +
                "    RRR        \n");

        assertCube("R2 B2 D B2 D U R2 D' B' D' R F2 R' D B U'",
                "    WYY        \n" +
                "    WWY        \n" +
                "    WWW        \n" +
                "BBB OOO GOO BBR\n" +
                "BBR GOO GGO BRR\n" +
                "BRR GGO GGG RRR\n" +
                "    WWY        \n" +
                "    WYY        \n" +
                "    YYY        \n");

        assertCube("U F2 D R D' L' U F' L2 U2 R U' R' U2 L' U'",
                "    WGG        \n" +
                "    WWG        \n" +
                "    WWW        \n" +
                "BBB OOO GRR WWR\n" +
                "BBO YOO GGR WRR\n" +
                "BOO YYO GGG RRR\n" +
                "    BBY        \n" +
                "    BYY        \n" +
                "    YYY        \n");

        assertCube("B2 L2 R2 U R2 B2 U L2 U' B F D2 L' B2 R2 D' U B' L' R'",
                "    WYY        \n" +
                "    YWY        \n" +
                "    WYW        \n" +
                "BRB OGO GOO BBR\n" +
                "RBR GOG OGO BRB\n" +
                "BRR GGO GOG RBR\n" +
                "    WWY        \n" +
                "    WYW        \n" +
                "    YWY        \n");

        assertCube("D' B2 L2 F2 R2 F2 U R2 U2 F' R B L D B U' F R' U2 R",
                "    WGG        \n" +
                "    GWG        \n" +
                "    WGW        \n" +
                "BOB OYO GRR WWR\n" +
                "OBO YOY RGR WRW\n" +
                "BOO YYO GRG RWR\n" +
                "    BBY        \n" +
                "    BYB        \n" +
                "    YBY        \n");

        assertCube("F' D L2 U' R B L2 F' D F U' F' L' B D L F'",
                "    YYY        \n" +
                "    YWY        \n" +
                "    YYW        \n" +
                "OOO BBO GRR GGG\n" +
                "OBO BOB RGR GRG\n" +
                "BOO BBB RRR GGR\n" +
                "    WWW        \n" +
                "    WYW        \n" +
                "    YWW        \n");

        assertCube("D F2 U' B F' L R' D L2 U' B R2 B' U L2 U'",
                "    GGG        \n" +
                "    GWG        \n" +
                "    GGW        \n" +
                "RRR WWO GOO YYY\n" +
                "RBR WOW OGO YRY\n" +
                "BRR WWW OOO YYR\n" +
                "    BBB        \n" +
                "    BYB        \n" +
                "    YBB        \n");

        assertCube("D U L2 B2 D U' F' U F' R F2 R' F D' B2 L2 D' U'",
                "    BWB        \n" +
                "    WWB        \n" +
                "    BBW        \n" +
                "WBW RRO GYY ORO\n" +
                "WBB OOR YGG RRO\n" +
                "BWW ROR YGY OOR\n" +
                "    GYG        \n" +
                "    GYY        \n" +
                "    YGG        \n");

        assertCube("B2 U L2 R2 D' F' D' R U F2 L2 U L' D2 L R B' U",
                "    GWG        \n" +
                "    WWG        \n" +
                "    GGW        \n" +
                "RBR WWO GOO YRY\n" +
                "RBB OOW OGG RRY\n" +
                "BRR WOW OGO YYR\n" +
                "    BYB        \n" +
                "    BYY        \n" +
                "    YBB        \n");

        assertCube("U R2 D2 B2 U' F L2 B R D R B R' D' F2 U2",
                "    WGG        \n" +
                "    WWW        \n" +
                "    WWW        \n" +
                "BBB OOO GRR WRR\n" +
                "BBB YOO GGG WRR\n" +
                "BOO YOO GGG RRR\n" +
                "    BBY        \n" +
                "    YYY        \n" +
                "    YYY        \n");

        assertCube("U' L2 U2 R2 U' B2 L' B D R' B' L' B' D2 B' L D B' U'",
                "    GGG        \n" +
                "    GWW        \n" +
                "    GGW        \n" +
                "RRR WOO GOO YYY\n" +
                "BBR WOW GGO YRY\n" +
                "BRR WWW OOO YRR\n" +
                "    BBB        \n" +
                "    YYB        \n" +
                "    YBB        \n");

        assertCube("U' L2 U F' R2 F U' L2 U F' R2 F",
                "    WWG        \n" +
                "    WWW        \n" +
                "    WWW        \n" +
                "BBB OOO GGR WRR\n" +
                "BBB OOO GGG RRR\n" +
                "BBO YOO GGG RRR\n" +
                "    BYY        \n" +
                "    YYY        \n" +
                "    YYY        \n");

        assertCube("U2 F2 R2 U' L2 D B R' B R' B R' D' L2 U'",
                "    GGG        \n" +
                "    GWW        \n" +
                "    GWW        \n" +
                "RRR WOO GGO YYY\n" +
                "BBR WOO GGO YRR\n" +
                "BBR WWW OOO YRR\n" +
                "    BBB        \n" +
                "    YYB        \n" +
                "    YYB        \n");

        assertCube("U' L2 F2 D' L' D U2 R U' R' U2 R2 U F' L' U R'",
                "    GGG        \n" +
                "    GWW        \n" +
                "    GWO        \n" +
                "RRR WOG WGO YYY\n" +
                "BBR WOO GGO YRR\n" +
                "YBR WWW OOO YRB\n" +
                "    BBB        \n" +
                "    YYB        \n" +
                "    RYB        \n");

        assertCube("D' B2 F2 R2 B2 F2 D U2 B L' B L B2 L B2 U' L U'",
                "    YWO        \n" +
                "    WWO        \n" +
                "    OOO        \n" +
                "OBG WWW BBB YRG\n" +
                "BBG WOO GGB YRR\n" +
                "GGG WOB RGB YYY\n" +
                "    RYW        \n" +
                "    RYY        \n" +
                "    RRR        \n");

        assertCube("D2 U B2 U2 B2 L2 B2 D F' L' U' F2 D2 F D B' L F U'",
                "    OGG        \n" +
                "    GWG        \n" +
                "    GGY        \n" +
                "GYY RRB RWW OOY\n" +
                "YBY ROR WGW ORO\n" +
                "YYR GRR WWB WOO\n" +
                "    WBB        \n" +
                "    BYB        \n" +
                "    BBO        \n");

        assertCube("F2 U2 L' R D2 F2 L' R ",
                "    YYY        \n" +
                "    YWY        \n" +
                "    WYW        \n" +
                "BGB ORO GBG OOO\n" +
                "GBG ROR BGB ORO\n" +
                "GGG RRR BBB ROR\n" +
                "    YWY        \n" +
                "    WYW        \n" +
                "    WWW        \n");

        assertCube("B2 D2 L R' D2 B2 L R'",
                "    YWY        \n" +
                "    YWY        \n" +
                "    WWW        \n" +
                "BBB OOO GGG ORO\n" +
                "GBG ROR BGB ORO\n" +
                "GBG ROR BGB RRR\n" +
                "    YYY        \n" +
                "    WYW        \n" +
                "    WYW        \n");

        assertCube("U2 R2 F2 D' U B2 L2 D' U'",
                "    YWY        \n" +
                "    YWY        \n" +
                "    WWW        \n" +
                "GBG RRO GGG OOR\n" +
                "GBG OOO BGB RRR\n" +
                "BBB RRO BGB OOR\n" +
                "    WYW        \n" +
                "    WYW        \n" +
                "    YYY        \n");

        assertCube("D' U B D' L' R F D' B' D' U L",
                "    OOO        \n" +
                "    WWO        \n" +
                "    OOO        \n" +
                "YBY GGG WWW BBB\n" +
                "YBY GOG WGG RRB\n" +
                "YYY GOG WWW BBB\n" +
                "    RYR        \n" +
                "    RYR        \n" +
                "    RRR        \n");

        assertCube("U R2 B L R' U' L' R' U' B' D' L' R F U",
                "    OOO        \n" +
                "    OWO        \n" +
                "    OWO        \n" +
                "YYY GGG WGW BBB\n" +
                "YBY GOO WGW BRR\n" +
                "YBY GGG WWW BBB\n" +
                "    RRR        \n" +
                "    RYR        \n" +
                "    RYR        \n");

        assertCube("B2 L2 R2 D B2 F2 L2 R2 D2 U' F2 L' D U' B F' D2 U2 L R' U'",
                "    RBO        \n" +
                "    GWG        \n" +
                "    RBO        \n" +
                "YOW GWG WRY GYG\n" +
                "RBR YOY OGO WRW\n" +
                "YOW BWB WRY BYB\n" +
                "    RGO        \n" +
                "    BYB        \n" +
                "    RGO        \n");
    }

    private void assertCube(String command, String expected) {
        Cube cube = new Cube();
        cube.doCommand(command);
        assertEquals(expected, cube.toString());
    }

    @Test
    public void testSolve() {
        Cube cube = new Cube();

        cube.doCommand("FRUF'B2DR'UF'B2R'ULR'BR'L2BL'BFR'RURB'LR'D2FD");

        assertEquals(
                "    ORG        \n" +
                "    BWG        \n" +
                "    RYG        \n" +
                "BRY BOR WYO YYW\n" +
                "YBW BOB OGW RRB\n" +
                "BOY GGW GGB OOW\n" +
                "    RRO        \n" +
                "    GYW        \n" +
                "    RWY        \n", cube.toString());

        assertEquals("[" +
                "[FRONT-UP:OY], " +
                "[FRONT-DOWN:GR], " +
                "[FRONT-LEFT:BW], " +
                "[FRONT-RIGHT:BO], " +
                "[UP-BACK:RY], " +
                "[UP-FRONT:YO], " +
                "[UP-LEFT:BR], " +
                "[UP-RIGHT:GY], " +
                "[DOWN-BACK:WO], " +
                "[DOWN-FRONT:RG], " +
                "[DOWN-LEFT:GO], " +
                "[DOWN-RIGHT:WG], " +
                "[LEFT-UP:RB], " +
                "[LEFT-DOWN:OG], " +
                "[LEFT-FRONT:WB], " +
                "[LEFT-BACK:YB], " +
                "[RIGHT-UP:YG], " +
                "[RIGHT-DOWN:GW], " +
                "[RIGHT-FRONT:OB], " +
                "[RIGHT-BACK:WR], " +
                "[BACK-UP:YR], " +
                "[BACK-DOWN:OW], " +
                "[BACK-LEFT:BY], " +
                "[BACK-RIGHT:RW]]", cube.getEdges().toString());

        assertEquals("[FRONT-UP:OY]", cube.getEdges().find("OY").toString());
        assertEquals("[UP-FRONT:YO]", cube.getEdges().find("YO").toString());
        assertEquals("[BACK-DOWN:OW]", cube.getEdges().find("OW").toString());
        assertEquals("[DOWN-BACK:WO]", cube.getEdges().find("WO").toString());

//        assertEquals("", getCommand(cube, "OY"));  // TODO тут полная ересь, надо продумать первый алгоритм лучше
        assertEquals("D'", getCommand(cube, "OG"));
        assertEquals("D'", getCommand(cube, "OW"));
        assertEquals("", getCommand(cube, "OB"));
    }

    private String getCommand(Cube cube, String oy) {
        Edge edge = cube.getEdges().find(oy);
        if (edge.face1 != Face.FRONT && edge.face2 != Face.FRONT) {
            if (edge.face1 == Face.UP || edge.face2 == Face.UP) {
                if (edge.face1 == Face.RIGHT || edge.face2 == Face.RIGHT) {
                    return "U";
                } else {
                    return "U'";
                }
            } else if (edge.face1 == Face.DOWN || edge.face2 == Face.DOWN) {
                if (edge.face1 == Face.RIGHT || edge.face2 == Face.RIGHT) {
                    return "D";
                } else {
                    return "D'";
                }
            } else if (edge.face1 == Face.BACK || edge.face2 == Face.BACK) {
                if (edge.face1 == Face.RIGHT || edge.face2 == Face.RIGHT) {
                    return "R2";
                } else {
                    return "L2";
                }
            }
        }

        return "";
    }
}
