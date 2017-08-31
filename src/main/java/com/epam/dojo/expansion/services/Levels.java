package com.epam.dojo.expansion.services;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.LevelImpl;
import com.epam.dojo.expansion.model.interfaces.ILevel;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by oleksandr.baglai on 18.06.2016.
 */
public final class Levels {

    public static int VIEW_SIZE = 20;
    public static final int VIEW_SIZE_TESTING = 16;

    private Levels() {
        throw new IllegalAccessError("Utility class");
    }

    public static final String DEMO_LEVEL =
            "                " +
            " ############## " +
            " #1...O......2# " +
            " #......$O....# " +
            " #....####....# " +
            " #....#  #....# " +
            " #.O###  ###.O# " +
            " #.$#      #..# " +
            " #..#      #$.# " +
            " #O.###  ###O.# " +
            " #....#  #....# " +
            " #....####....# " +
            " #....O$......# " +
            " #4......O...3# " +
            " ############## " +
            "                ";

    public static final String MULTI_LEVEL =
            "                                      " +
            "   ######      ###########            " +
            "   #$...#      #......$..#            " +
            "   #BB.O#      #.........# ########   " +
            "   #B...#      #...B.BBBB# #..O..O#   " +
            "   #.4..#  #####....$...O# #..$.BB#   " +
            "   #....####......O......# #O.3.O.#   " +
            "   #..$......###....$..OO# #O....B#   " +
            "   #B...###### #.O.......# #B.#####   " +
            "   #B..O#      #.........###B.#       " +
            "   ##.### ######..BOO.........#       " +
            "    #.#   #..$..B.B....B.B..BB#       " +
            "    #.#   #$..###.B.#######B.B###     " +
            "    #.#   #...# #BB.#     #O..BB#     " +
            "    #.#   ##### #...#     #.$...#     " +
            "   ##.###       #.B.#  ####.....#     " +
            "   #..B.#  ######.BB#  #....BO.$#     " +
            "   #...$#  #B.......####.BB.B...#     " +
            "   #O...####O...O...$....######.#     " +
            "   #..O............O######    #.##### " +
            "   #....####.OB.E...#       ###.B...# " +
            "   #BB..#  #BBB.....#  ######.....$.# " +
            "   ###.##  #...O..$O#  #.......$....# " +
            "     #.#   #####.####  ######.......# " +
            " #####.###     #.#          #####..O# " +
            " #..O....#  ####.##########     #.O.# " +
            " #....O..#  #......$..B.BB#     #O..# " +
            " #$#######  #.#####.BB..BB#######.### " +
            " #.#        #.#   #....O..........#   " +
            " #.# ########.##  ####....#####.B##   " +
            " #.# #.........##### ###.##   #..#    " +
            " #.# #B.O....O..$..#   #.#    #B.#### " +
            " #.###..O.$....###.#####.#### #.$...# " +
            " #.$.2..O..O.BB# #.BB....O..# #.....# " +
            " #.#####BB.BBBB# #.....$....# #..1..# " +
            " #.#   #.$.....# ####.BO..OB# #.....# " +
            " ###   #...$...#    ######### #...B.# " +
            "       #########              ####### ";

    public static final String MULTI_LEVEL_SIMPLE =
            "    ############### " +
            "    #..........$..# " +
            "    #B.O.O###B.2..# " +
            "  ###.B.B.# #.....# " +
            "  #.$.3...# #B.$..# " +
            "  #...B#### ##.O..# " +
            "  #.O..#     ###..# " +
            "  #..$.#####   #..# " +
            "  #BB......#####..# " +
            "  ######..........# " +
            "       ##..E.###### " +
            " #####  #....#      " +
            " #.$.#  #.$.B###### " +
            " #.4.####.......O.# " +
            " #........####B.$.# " +
            " ####B.$..#  ###### " +
            "    #.....#         " +
            " ####..O.B########  " +
            " #B...O$......1..#  " +
            " #################  ";

    public static final String LEVEL_1A =
            "        " +
            "        " +
            " ###### " +
            " #1..E# " +
            " ###### " +
            "        " +
            "        " +
            "        ";

    public static final String LEVEL_2A =
            "        " +
            "   ###  " +
            "   #1#  " +
            "   #.#  " +
            "   #.#  " +
            "   #E#  " +
            "   ###  " +
            "        ";

    public static final String LEVEL_3A =
            "        " +
            "        " +
            " ###### " +
            " #E..1# " +
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
            "   #1#  " +
            "   ###  " +
            "        ";

    public static final String LEVEL_5A =
            "        " +
            " ###### " +
            " #1...# " +
            " ####.# " +
            "    #.# " +
            "    #E# " +
            "    ### " +
            "        ";

    public static final String LEVEL_6A =
            "        " +
            "    ### " +
            "    #1# " +
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
            " #...1# " +
            " ###### " +
            "        ";

    public static final String LEVEL_8A =
            "        " +
            " ###### " +
            " #...E# " +
            " #.#### " +
            " #.#    " +
            " #1#    " +
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
            " #.# #.1# #.# " +
            " #.# #.## #.# " +
            " #.# #.#  #.# " +
            " #.# #.####.# " +
            " #E# #......# " +
            " ### ######## " +
            "              ";

    public static final String LEVEL_1B =
            "          " +
            " ######## " +
            " #1.....# " +
            " #..###.# " +
            " #..# #.# " +
            " #.$###.# " +
            " #......# " +
            " #..$..E# " +
            " ######## " +
            "          ";

    public static final String LEVEL_2B =
            "          " +
            " ######## " +
            " #1.O..$# " +
            " #......# " +
            " ####...# " +
            "    #..O# " +
            " ####...# " +
            " #...O.E# " +
            " ######## " +
            "          ";

    public static final String LEVEL_3B =
            "             " +
            "   #######   " +
            "   #1.O..#   " +
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

    public static final String LEVEL_1C =
            "              " +
            "   ########   " +
            "   #1...B.#   " +
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

    public static List<ILevel> collectSingle() {
        return collect(LEVEL_1A, LEVEL_2A, LEVEL_3A, LEVEL_4A, LEVEL_5A, LEVEL_6A,
                LEVEL_7A, LEVEL_8A, LEVEL_9A,
                LEVEL_1B, LEVEL_2B, LEVEL_3B, LEVEL_1C);
    }

    public static List<ILevel> collectMultiple() {
        return collect(MULTI_LEVEL_SIMPLE);
    }

    private static List<ILevel> collect(String... levels) {
        List<ILevel> result = new LinkedList<>();
        for (String level : levels) {
            result.add(new LevelImpl(resize(decorate(level), size())));
        }
        return result;
    }

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

                if (chk("###" +
                        "#  " +
                        "#  ", x, y, map) ||
                    chk("## " +
                        "#  " +
                        "#  ", x, y, map) ||
                    chk("###" +
                        "#  " +
                        "   ", x, y, map) ||
                    chk("## " +
                        "#  " +
                        "   ", x, y, map))
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
                    out.setAt(x, y, Elements.BREAK.ch());
                }

            }
        }

        return out.getMap();
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
