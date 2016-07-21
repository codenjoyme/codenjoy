package com.epam.dojo.icancode.services;

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


    public static final String LEVELA1 =
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

    public static final String LEVELA2 =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "     ######     " +
            "     #S...#     " +
            "     ####E#     " +
            "        ###     " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVELA3 =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "        ###     " +
            "     ####E#     " +
            "     #S...#     " +
            "     ######     " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVELA4 =
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

    public static final String LEVELA5 =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "     ###        " +
            "     #E####     " +
            "     #...S#     " +
            "     ######     " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVELA6 =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "     ######     " +
            "     #...S#     " +
            "     #E####     " +
            "     ###        " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVELA7 =
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

    public static final String LEVELA8 =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "      ####      " +
            "      #E.#      " +
            "      ##.#      " +
            "       #.#      " +
            "       #S#      " +
            "       ###      " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVELA9 =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "       ####     " +
            "       #.E#     " +
            "       #.##     " +
            "       #.#      " +
            "       #S#      " +
            "       ###      " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVELA10 =
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

    public static final String LEVELA11 =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "       ###      " +
            "       #S#      " +
            "       #.#      " +
            "      ##.#      " +
            "      #E.#      " +
            "      ####      " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVELA12 =
            "                " +
            "                " +
            "                " +
            "                " +
            "                " +
            "       ###      " +
            "       #S#      " +
            "       #.#      " +
            "       #.##     " +
            "       #.E#     " +
            "       ####     " +
            "                " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVELA13 =
            "                " +
            " ########### ###" +
            " #.........# #E#" +
            " #.#######.# #.#" +
            " #.#     #.# #.#" +
            " #.# ### #.# #.#" +
            " #.# #S# #.# #.#" +
            " #.# #.# #.# #.#" +
            " #.# #.###.# #.#" +
            " #.# #.....# #.#" +
            " #.# ####### #.#" +
            " #.#         #.#" +
            " #.###########.#" +
            " #.............#" +
            " ###############" +
            "                ";

    public static final String LEVELB =
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

    public static final String LEVELC =
            "                " +
            "                " +
            "                " +
            "                " +
            "    ########    " +
            "    #S.O..$#    " +
            "    #......#    " +
            "    ####...#    " +
            "       #.O.#    " +
            "    ####...#    " +
            "    #...O.E#    " +
            "    ########    " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static final String LEVELD =
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

    public static final String LEVELE =
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

    public static final String LEVELF =
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
        return collect(LEVELA1, LEVELA2, LEVELA3, LEVELA4, LEVELA5, LEVELA6,
                LEVELA7, LEVELA8, LEVELA9, LEVELA10, LEVELA11, LEVELA12, LEVELA13,
                LEVELB, LEVELC, LEVELD, LEVELE, LEVELF);
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
