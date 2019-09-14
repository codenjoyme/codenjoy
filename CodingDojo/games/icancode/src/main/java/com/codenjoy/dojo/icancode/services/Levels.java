package com.codenjoy.dojo.icancode.services;

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


import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.LevelImpl;
import com.codenjoy.dojo.icancode.model.interfaces.ILevel;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public final class Levels {

    public static int VIEW_SIZE = 20;
    public static final int VIEW_SIZE_TESTING = 16;
    public static final int COUNT_LAYERS = 3;

    private Levels() {
        throw new IllegalAccessError("Utility class");
    }

    public static final String DEMO_LEVEL =
            "                " +
            " ############## " +
            " #S...O.....˅.# " +
            " #˃.....$O....# " +
            " #....####....# " +
            " #....#  #....# " +
            " #.O###  ###.O# " +
            " #.$#      #..# " +
            " #..#      #$.# " +
            " #O.###  ###O.# " +
            " #....#  #....# " +
            " #....####....# " +
            " #....O$.....˂# " +
            " #.˄.....O...E# " +
            " ############## " +
            "                ";

    public static final String MULTI_LEVEL =
            "                                      " +
            "   ######      ###########            " +
            "   #$..˅#      #˃.....$..#            " +
            "   #BB.O#      #....Z....# ########   " +
            "   #B...#      #...B.BBBB# #˃.O..O#   " +
            "   #.SO.#  #####˃...$...O# #..$.BB#   " +
            "   #˃...####......O..S...# #O.S.O˂#   " +
            "   #..$......###....$..OO# #O....B#   " +
            "   #B...###### #.O.......# #B.#####   " +
            "   #B..O#      #.........###B.#       " +
            "   ##.### ######..BOO.........#       " +
            "    #.#   #..$..B.B....B.B..BB#       " +
            "    #.#   #$..###.B.#######B.B###     " +
            "    #.#   #...# #BB.#     #O..BB#     " +
            "    #.#   ##### #...#     #.$..˂#     " +
            "   ##.###       #.B.#  ####.Z...#     " +
            "   #..B.#  ######.BB#  #....BO.$#     " +
            "   #...$#  #B.......####.BB.B...#     " +
            "   #OZ..####O...O...$....######.#     " +
            "   #..O............O######    #.##### " +
            "   #˄...####.OB.....#       ###.B...# " +
            "   #BB..#  #BBB....˄#  ######˃....$.# " +
            "   ###.##  #˃..O..$O#  #˃......$.Z..# " +
            "     #.#   #####.####  ######˃......# " +
            " #####.###     #.#          #####..O# " +
            " #..O...˂#  ####.##########     #.O.# " +
            " #....O..#  #......$..B.BB#     #O..# " +
            " #$#######  #.#####.BB..BB#######.### " +
            " #.#        #.#   #....O....Z.....#   " +
            " #.# ########.##  ####....#####.B##   " +
            " #.# #.....˂...##### ###.##   #..#    " +
            " #.# #B.O....O..$..#   #.#    #B.#### " +
            " #.###..O.$E...###.#####.#### #.$...# " +
            " #.$....O.ZO.BB# #.BB˃...O..# #....˂# " +
            " #.#####BB.BBBB# #.....$E...# #.BE..# " +
            " #˄#   #.$....˂# ####.BO..OB# #˃....# " +
            " ###   #...$...#    ######### #...B.# " +
            "       #########              ####### ";

    public static final String MULTI_LEVEL_SIMPLE_WITHOUT_LASERS =
            "    ############### " +
            "    #......O...$..# " +
            "    #B.O.O###B.S..# " +
            "  ###.B.B.# #.....# " +
            "  #.$.S...# #B.$..# " +
            "  #...B#### ##....# " +
            "  #.O..#     ###O.# " +
            "  #..$.#####   #.O# " +
            "  #BB..O...#####..# " +
            "  ######.B....O...# " +
            "       ##..E.###### " +
            " #####  #....#      " +
            " #.$.#  #.$.B###### " +
            " #...####O....O...# " +
            " #...O....####B.$.# " +
            " ####B.$..#  ###### " +
            "    #.S.O.#         " +
            " ####....B########  " +
            " #B...O$..O......#  " +
            " #################  ";

    public static final String MULTI_LEVEL_SIMPLE =
            "    ############### " +
            "    #˃.........$.˅# " +
            "    #B.O.O###B.S..# " +
            "  ###.B.B.# #.....# " +
            "  #.$.S...# #B.$..# " +
            "  #...B#### ##.O..# " +
            "  #.O..#     ###..# " +
            "  #..$.#####   #..# " +
            "  #BB....˅.#####..# " +
            "  ######..........# " +
            "       ##..E.###### " +
            " #####  #....#      " +
            " #.$.#  #.$.B###### " +
            " #...####.......O.# " +
            " #˃.......####B.$.# " +
            " ####B.$..#  ###### " +
            "    #.S...#         " +
            " ####..O.B########  " +
            " #B...O$........˂#  " +
            " #################  ";


    public static final String LEVEL_1A =
            "        " +
            "        " +
            " ###### " +
            " #S..E# " +
            " ###### " +
            "        " +
            "        " +
            "        ";

    public static final String LEVEL_2A =
            "        " +
            "   ###  " +
            "   #S#  " +
            "   #.#  " +
            "   #.#  " +
            "   #E#  " +
            "   ###  " +
            "        ";

    public static final String LEVEL_3A =
            "        " +
            "        " +
            " ###### " +
            " #E..S# " +
            " ###### " +
            "        " +
            "        " +
            "        ";

    public static final String LEVEL_4A =
            "        " +
            "   ###  " +
            "   #E#  " +
            "   #.#  " +
            "   #.#  " +
            "   #S#  " +
            "   ###  " +
            "        ";

    public static final String LEVEL_5A =
            "        " +
            " ###### " +
            " #S...# " +
            " ####.# " +
            "    #.# " +
            "    #E# " +
            "    ### " +
            "        ";

    public static final String LEVEL_6A =
            "        " +
            "    ### " +
            "    #S# " +
            "    #.# " +
            " ####.# " +
            " #E...# " +
            " ###### " +
            "        ";

    public static final String LEVEL_7A =
            "        " +
            " ###    " +
            " #E#    " +
            " #.#    " +
            " #.#### " +
            " #...S# " +
            " ###### " +
            "        ";

    public static final String LEVEL_8A =
            "        " +
            " ###### " +
            " #...E# " +
            " #.#### " +
            " #.#    " +
            " #S#    " +
            " ###    " +
            "        ";

    public static final String LEVEL_9A =
            "              " +
            "              " +
            " ############ " +
            " #..........# " +
            " #.########.# " +
            " #.#      #.# " +
            " #.# #### #.# " +
            " #.# #.S# #.# " +
            " #.# #.## #.# " +
            " #.# #.#  #.# " +
            " #.# #.####.# " +
            " #E# #......# " +
            " ### ######## " +
            "              ";

    public static final String LEVEL_1B =
            "          " +
            " ######## " +
            " #S.....# " +
            " #..###.# " +
            " #..# #.# " +
            " #.$###.# " +
            " #......# " +
            " #..$..E# " +
            " ######## " +
            "          ";

    public static final String LEVEL_1C =
            "          " +
            " ######## " +
            " #S.O..$# " +
            " #......# " +
            " ####...# " +
            "    #..O# " +
            " ####...# " +
            " #...O.E# " +
            " ######## " +
            "          ";

    public static final String LEVEL_2C =
            "             " +
            "   #######   " +
            "   #S.O..#   " +
            "   ####..#   " +
            "      #..#   " +
            "   ####..### " +
            "   #$B.OO..# " +
            "   #.###...# " +
            "   #.# #...# " +
            "   #.###..E# " +
            "   #.......# " +
            "   ######### " +
            "             ";

    public static final String LEVEL_1D =
            "              " +
            "   ########   " +
            "   #S...B.#   " +
            "   ###B...#   " +
            "     #B...#   " +
            "   ###$B..####" +
            "   #$....B..B#" +
            "   #.#####...#" +
            "   #.#   #...#" +
            "   #.#####.B.#" +
            "   #.E.....B$#" +
            "   ###########" +
            "              " +
            "              ";

    public static final String LEVEL_1E =
            "                " +
            "  #####         " +
            "  #S..#         " +
            "  #..B#######   " +
            "  #B..B˃...$#   " +
            "  ###....BBB#   " +
            "    #.B....$#   " +
            "    #...˄B..### " +
            "    #.###˃....# " +
            "    #.# #B.B.$# " +
            "    #.# #...### " +
            "    #.# #.$##   " +
            "    #E# ####    " +
            "    ###         " +
            "                " +
            "                ";

    public static final String LEVEL_1F =
            "                " +
            "  ##### ####### " +
            "  #S..# #....Z# " +
            "  #...###...### " +
            "  #...$...$.#   " +
            "  ###.......#   " +
            "    #..$....#   " +
            "    #.....$.### " +
            "    #.###....E# " +
            "    #$# #.$...# " +
            "    #.###...### " +
            "    #...$..##   " +
            "    #.######    " +
            "    ###         " +
            "                " +
            "                ";

    public static final String LEVEL_2F =
            "              " +
            "              " +
            " ############ " +
            " #S.........# " +
            " ##########.# " +
            "          #.# " +
            " ##########.# " +
            " #....Z.....# " +
            " #.########## " +
            " #.#          " +
            " #.########## " +
            " #.........E# " +
            " ############ " +
            "              ";

    public static final String LEVEL_3F =
            "    ############### " +
            "    #Z.....E...$.Z# " +
            "    #B...O###B....# " +
            "  ###.B.B.# #.....# " +
            "  #.$.....# #B.$..# " +
            "  #...B#### ##..O.# " +
            "  #.O..#     ###..# " +
            "  #..$.#####   #.O# " +
            "  #BB......#####..# " +
            "  ######˃.........# " +
            "       ##....###### " +
            " #####  #.O..#      " +
            " #.$.#  #.$.B###### " +
            " #...####.......O.# " +
            " #....O...####B...# " +
            " ####..$..#  ###### " +
            "    #...O.#         " +
            " ####....B########  " +
            " #S...O$........S#  " +
            " #################  ";

    public static final String LEVEL_1G =
            "  ############  ############# " +
            "  #..OB..$.BB#  #...B..B.O..# " +
            "  #.####..O..####.###...$...# " +
            "###.#  #B.....$...# #.####B.# " +
            "#.$.#  ###..B..O..# #O#  #..# " +
            "#.###    #.$.E....###.## #BB# " +
            "#O#      #.......B..$..# #..# " +
            "#.###    #B...O###B...$# #.$# " +
            "#.$$#  ###.B.B.# #.....# #B$# " +
            "#####  #.$.....# #B.$..# #..# " +
            "       #...B#### ##..O.# #.B# " +
            " #######.O..#     ###..# #..# " +
            " #$...B...$.#####   #.O# #..# " +
            " #..####BB......#####..# #.O# " +
            " #OB#  ######...$......# #..# " +
            " #..#       ##....###### #B.# " +
            " #$$# #####  #.O..#      #$$# " +
            " #..# #.$.#  #.$.B###### #.$# " +
            " #BB# #...####.......O.# #..# " +
            " #..# #....O...####B..## #### " +
            " #.O# ####..$..#  #####       " +
            " #..#    #$..O.#        ##### " +
            " #$$# ####....B######## #$$.# " +
            " #### #....O$...O...$.# #...# " +
            "      #.#####..######## ###B# " +
            "   ####$#   #..#          #.# " +
            "   #..O.#   #..#####  #####.# " +
            "####.####   #.O....####..$..# " +
            "#S...#      #...$..B.....#### " +
            "######      ##############    ";

    public static ILevel loadLevel(int level) {
        return load(getSingleMaps().get(level));
    }

    public static List<String> getSingleMaps() {
        return Arrays.asList(LEVEL_1A, LEVEL_2A, LEVEL_3A, LEVEL_4A, LEVEL_5A, LEVEL_6A,
                LEVEL_7A, LEVEL_8A, LEVEL_9A,
                LEVEL_1B,
                LEVEL_1C, LEVEL_2C,
                LEVEL_1D,
                LEVEL_1E,
                LEVEL_1F, LEVEL_2F, LEVEL_3F,
                LEVEL_1G);
    }

    public static ILevel getMultiple() {
        return load(MULTI_LEVEL);
    }

    public static ILevel load(String levelMap) {
        return new LevelImpl(resize(decorate(levelMap), size()));
    }

    // TODO я думаю этот метод не нужен тут, так как он дублирует Layered view
    static String resize(String level, int toSize) {
        double sqrt = Math.sqrt(level.length());
        int currentSize = (int) sqrt;
        if (sqrt - currentSize != 0) {
            throw new IllegalArgumentException("Level is not square: " + level);
        }
        if (currentSize >= toSize) {
            return level;
        }

        int before = (int)((toSize - currentSize)/2);
        int after = (toSize - currentSize - before);
        String result = "";
        for (int i = 0; i < currentSize; i++) {
            String part = level.substring(i*currentSize, (i + 1)*currentSize);
            part = StringUtils.leftPad(part, before + part.length());
            part = StringUtils.rightPad(part, after + part.length());
            result += part;
        }
        result = StringUtils.leftPad(result, before*toSize + result.length());
        result = StringUtils.rightPad(result, after*toSize + result.length());

        return result;
    }

    public static String decorate(String level) {
        LengthToXY.Map map = new LengthToXY.Map(level);
        LengthToXY.Map out = new LengthToXY.Map(map.getSize());
        for (int x = 0; x < map.getSize(); ++x) {
            for (int y = 0; y < map.getSize(); ++y) {
                char at = map.getAt(x, y);
                if (at != '#') {
                    out.setAt(x, y, at);
                    continue;
                }

                if (isaBoolean(map, x, y, "###", "#  ", "## "))
                {
                    out.setAt(x, y, Elements.ANGLE_IN_LEFT.ch());
                } else
                if (chk("###" +
                        "  #" +
                        "  #", x, y, map) ||
                    chk(" ##" +
                        "  #" +
                        "  #", x, y, map) ||
                    chk("###" +
                        "  #" +
                        "   ", x, y, map) ||
                    chk(" ##" +
                        "  #" +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Elements.ANGLE_IN_RIGHT.ch());
                } else
                if (chk("#  " +
                        "#  " +
                        "###", x, y, map) ||
                    chk("   " +
                        "#  " +
                        "###", x, y, map) ||
                    chk("#  " +
                        "#  " +
                        "## ", x, y, map) ||
                    chk("   " +
                        "#  " +
                        "## ", x, y, map))
                {
                    out.setAt(x, y, Elements.ANGLE_BACK_LEFT.ch());
                } else
                if (chk("  #" +
                        "  #" +
                        "###", x, y, map) ||
                    chk("   " +
                        "  #" +
                        "###", x, y, map) ||
                    chk("  #" +
                        "  #" +
                        " ##", x, y, map) ||
                    chk("   " +
                        "  #" +
                        " ##", x, y, map))
                {
                    out.setAt(x, y, Elements.ANGLE_BACK_RIGHT.ch());
                } else
                if (chk("   " +
                        "   " +
                        "###", x, y, map) ||
                    chk("   " +
                        "   " +
                        " ##", x, y, map) ||
                    chk("   " +
                        "   " +
                        "## ", x, y, map) ||
                    chk("   " +
                        "   " +
                        " # ", x, y, map))
                {
                    out.setAt(x, y, Elements.WALL_BACK.ch());
                } else
                if (chk("#  " +
                        "#  " +
                        "#  ", x, y, map) ||
                    chk("   " +
                        "#  " +
                        "#  ", x, y, map) ||
                    chk("#  " +
                        "#  " +
                        "   ", x, y, map) ||
                    chk("   " +
                        "#  " +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Elements.WALL_LEFT.ch());
                } else
                if (chk("  #" +
                        "  #" +
                        "  #", x, y, map) ||
                    chk("  #" +
                        "  #" +
                        "   ", x, y, map) ||
                    chk("   " +
                        "  #" +
                        "  #", x, y, map) ||
                    chk("   " +
                        "  #" +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Elements.WALL_RIGHT.ch());
                } else
                if (chk("###" +
                        "   " +
                        "   ", x, y, map) ||
                    chk(" ##" +
                        "   " +
                        "   ", x, y, map) ||
                    chk("## " +
                        "   " +
                        "   ", x, y, map) ||
                    chk(" # " +
                        "   " +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Elements.WALL_FRONT.ch());
                } else
                if (chk("   " +
                        "   " +
                        "  #", x, y, map))
                {
                    out.setAt(x, y, Elements.WALL_BACK_ANGLE_LEFT.ch());
                } else
                if (chk("   " +
                        "   " +
                        "#  ", x, y, map))
                {
                    out.setAt(x, y, Elements.WALL_BACK_ANGLE_RIGHT.ch());
                } else
                if (chk("#  " +
                        "   " +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Elements.ANGLE_OUT_RIGHT.ch());
                } else
                if (chk("  #" +
                        "   " +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Elements.ANGLE_OUT_LEFT.ch());
                }
                if (chk("   " +
                        "   " +
                        "   ", x, y, map))
                {
                    out.setAt(x, y, Elements.BOX.ch());
                }

            }
        }

        return out.getMap();
    }

    private static boolean isaBoolean(LengthToXY.Map map, int x, int y, String s, String s2, String s3) {
        return chk(s +
                s2 +
                s2, x, y, map) ||
                chk(s3 +
                        s2 +
                        s2, x, y, map) ||
                chk(s +
                        s2 +
                        "   ", x, y, map) ||
                chk(s3 +
                        s2 +
                        "   ", x, y, map);
    }

    private static boolean chk(String mask, int x, int y, LengthToXY.Map map) {
        LengthToXY.Map mm = new LengthToXY.Map(mask);
        LengthToXY.Map out = new LengthToXY.Map(mm.getSize());
        for (int xx = -1; xx <= 1; xx++) {
            for (int yy = -1; yy <= 1; yy++) {
                char ch = ' ';
                if (map.isOutOf(x + xx, y + yy) || map.getAt(x + xx, y + yy) == ' ') {
                    ch = '#';
                }
                out.setAt(xx + 1, yy + 1, ch);
            }
        }
        String actual = TestUtils.injectN(out.getMap());
        String expected = TestUtils.injectN(mask);
//        System.out.print(actual);
//        System.out.println("-----------");
        return actual.equals(expected);
    }

    public static int size() {
        return VIEW_SIZE; // TODO think about it
    }
}
