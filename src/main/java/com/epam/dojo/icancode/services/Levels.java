package com.epam.dojo.icancode.services;

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
import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.LevelImpl;
import com.epam.dojo.icancode.model.interfaces.ILevel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by oleksandr.baglai on 18.06.2016.
 */
public final class Levels {

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
            "   ######                             " +
            "   #...˅#                             " +
            "   #BB.O#      ########### ########   " +
            "   #B...#      #...B.BBBB# #˃.O..O#   " +
            "   #..O.#  #####˃.......O# #..$.BB#   " +
            "   #˃...####......O......# #O...O˂#   " +
            "   #..$......###....$..OO# #O....B#   " +
            "   #B...###### #.O.......# #B.#####   " +
            "   #B..O#      #.........###B.#       " +
            "   ##.###      #..BOO.........#       " +
            "    #.#        #B.B....B.B..BB#       " +
            "    #.#        ##.B.#######B.B###     " +
            "    #.#         #BB.#     #O..BB#     " +
            "    #.#         #...#     #....˂#     " +
            "   ##.###       #.B.#  ####.....#     " +
            "   #..B.#  ######.BB#  #....BO.$#     " +
            "   #...$#  #B.......####.BB.B...#     " +
            "   #O...####O...O........########     " +
            "   #..O.........S..O######            " +
            "   #˄...####.OB.E...#                 " +
            "   #BB..#  #BBB....˄#                 " +
            "   ######  #˃..O..$O#                 " +
            "           #####.####                 " +
            "               #.#                    " +
            "            ####.##########           " +
            "            #......$..B.BB#           " +
            "            #.#####.BB..BB#           " +
            "            #.#   #....O..#           " +
            "     ########.##  ####....#           " +
            "     #.....˂...##### ###.##           " +
            "     #B.O....O.....#   #.#            " +
            "     #..O.$....###.#####.####         " +
            "     #..O..O.BB# #.BB˃...O..#         " +
            "     ########### #.....$....#         " +
            "                 ####.BO..OB#         " +
            "                    #########         " +
            "                                      ";


    public static final String LEVEL_1A =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "     ######     " +
            "     #S..E#     " +
            "     ######     " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_2A =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "       ###      " +
            "       #S#      " +
            "       #.#      " +
            "       #.#      " +
            "       #E#      " +
            "       ###      " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_3A =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "     ######     " +
            "     #E..S#     " +
            "     ######     " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_4A =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "       ###      " +
            "       #E#      " +
            "       #.#      " +
            "       #.#      " +
            "       #S#      " +
            "       ###      " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_5A =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "     ######     " +
            "     #S...#     " +
            "     ####.#     " +
            "        #.#     " +
            "        #E#     " +
            "        ###     " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_6A =
            "                " +
            "                " +
            "                " +
            "                " +
            "        ###     " +
            "        #S#     " +
            "        #.#     " +
            "     ####.#     " +
            "     #E...#     " +
            "     ######     " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_7A =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "     ###        " +
            "     #E#        " +
            "     #.#        " +
            "     #.####     " +
            "     #...S#     " +
            "     ######     " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_8A =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "     ######     " +
            "     #...E#     " +
            "     #.####     " +
            "     #.#        " +
            "     #S#        " +
            "     ###        " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_9A =
            "                " +
            "                " +
            "                " +
            "  ############  " +
            "  #..........#  " +
            "  #.########.#  " +
            "  #.#      #.#  " +
            "  #.# #### #.#  " +
            "  #.# #.S# #.#  " +
            "  #.# #.## #.#  " +
            "  #.# #.#  #.#  " +
            "  #.# #.####.#  " +
            "  #E# #......#  " +
            "  ### ########  " +
            "                " +
            "                ";

    public static final String LEVEL_1B =
            "                " +
            "                " +
            "                " +
            "                " +
            "    ########    " +
            "    #S.....#    " +
            "    #..###.#    " +
            "    #..# #.#    " +
            "    #.$###.#    " +
            "    #......#    " +
            "    #..$..E#    " +
            "    ########    " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_1C =
            "                " +
            "                " +
            "                " +
            "                " +
            "    ########    " +
            "    #S.O..$#    " +
            "    #......#    " +
            "    ####...#    " +
            "       #..O#    " +
            "    ####...#    " +
            "    #...O.E#    " +
            "    ########    " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_1D =
            "                " +
            "                " +
            "    #######     " +
            "    #S.O..#     " +
            "    ####..#     " +
            "       #..#     " +
            "    ####..###   " +
            "    #$..OO..#   " +
            "    #.###...#   " +
            "    #.# #...#   " +
            "    #.###..E#   " +
            "    #.......#   " +
            "    #########   " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_1E =
            "                " +
            "                " +
            "    ########    " +
            "    #S...B.#    " +
            "    ###B...#    " +
            "      #B...#    " +
            "    ###$B..#### " +
            "    #$....B..B# " +
            "    #.#####...# " +
            "    #.#   #...# " +
            "    #.#####.B.# " +
            "    #.E.....B$# " +
            "    ########### " +
            "                " +
            "                " +
            "                ";

    public static final String LEVEL_1F =
            "                " +
            "  #####         " +
            "  #S.$#         " +
            "  #..B#######   " +
            "  #B..B˃...$#   " +
            "  ###....BBB#   " +
            "    #.B.....#   " +
            "    #...˄B..### " +
            "    #.###˃....# " +
            "    #.# #B.B.$# " +
            "    #.# #...### " +
            "    #.# #.$##   " +
            "    #E# ####    " +
            "    ###         " +
            "                " +
            "                ";

    public static List<ILevel> collectSingle() {
        return collect(LEVEL_1A, LEVEL_2A, LEVEL_3A, LEVEL_4A, LEVEL_5A, LEVEL_6A,
                LEVEL_7A, LEVEL_8A, LEVEL_9A,
                LEVEL_1B, LEVEL_1C, LEVEL_1D, LEVEL_1E, LEVEL_1F);
    }

    public static List<ILevel> collectMultiple() {
        return collect(MULTI_LEVEL);
    }

    private static List<ILevel> collect(String... levels) {
        List<ILevel> result = new LinkedList<>();
        for (String level : levels) {
            result.add(new LevelImpl(decorate(level)));
        }
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
                    out.setAt(x, y, Elements.BOX.ch());
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
        return 16; // TODO think about it
    }
}
