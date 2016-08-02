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


import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by oleksandr.baglai on 24.06.2016.
 */
public class LevelsTest {
    @Test
    public void testLevel1() {
        String map = Levels.LEVEL_1A;

        asrtMap("                " +
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
                "                ", map);

        String decorate = Levels.decorate(map);

        asrtMap("                " +
                "                " +
                "                " +
                "                " +
                "                " +
                "                " +
                "     ╔════┐     " +
                "     ║S..E│     " +
                "     └────┘     " +
                "                " +
                "                " +
                "                " +
                "                " +
                "                " +
                "                " +
                "                ", decorate);
    }

    @Test
    public void testLevel2() {
        String map = Levels.LEVEL_1B;

        asrtMap("                " +
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
                "                ", map);

        String decorate = Levels.decorate(map);

        asrtMap("                " +
                "                " +
                "                " +
                "                " +
                "    ╔══════┐    " +
                "    ║S.....│    " +
                "    ║..┌─╗.│    " +
                "    ║..│ ║.│    " +
                "    ║.$╚═╝.│    " +
                "    ║......│    " +
                "    ║..$..E│    " +
                "    └──────┘    " +
                "                " +
                "                " +
                "                " +
                "                ", decorate);
    }

    @Test
    public void testLevel3() {
        String map = Levels.LEVEL_1C;

        asrtMap("                " +
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
                "                ", map);

        String decorate = Levels.decorate(map);

        asrtMap("                " +
                "                " +
                "                " +
                "                " +
                "    ╔══════┐    " +
                "    ║S.O..$│    " +
                "    ║......│    " +
                "    └──╗...│    " +
                "       ║..O│    " +
                "    ╔══╝...│    " +
                "    ║...O.E│    " +
                "    └──────┘    " +
                "                " +
                "                " +
                "                " +
                "                ", decorate);
    }

    @Test
    public void testLevel4() {
        String map = Levels.LEVEL_1D;

        asrtMap("                " +
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
                "                ", map);

        String decorate = Levels.decorate(map);

        asrtMap("                " +
                "                " +
                "    ╔═════┐     " +
                "    ║S.O..│     " +
                "    └──╗..│     " +
                "       ║..│     " +
                "    ╔══╝..╚═┐   " +
                "    ║$..OO..│   " +
                "    ║.┌─╗...│   " +
                "    ║.│ ║...│   " +
                "    ║.╚═╝..E│   " +
                "    ║.......│   " +
                "    └───────┘   " +
                "                " +
                "                " +
                "                ", decorate);
    }

    @Test
    public void testLevel5() {
        String map = Levels.LEVEL_1E;

        asrtMap("                " +
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
                "                ", map);

        String decorate = Levels.decorate(map);

        asrtMap("                " +
                "                " +
                "    ╔══════┐    " +
                "    ║S...B.│    " +
                "    └─╗B...│    " +
                "      ║B...│    " +
                "    ╔═╝$B..╚══┐ " +
                "    ║$....B..B│ " +
                "    ║.┌───╗...│ " +
                "    ║.│   ║...│ " +
                "    ║.╚═══╝.B.│ " +
                "    ║.E.....B$│ " +
                "    └─────────┘ " +
                "                " +
                "                " +
                "                ", decorate);
    }

    @Test
    public void testLevel6() {
        String map = Levels.LEVEL_1F;

        asrtMap("                " +
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
                "                ", map);

        String decorate = Levels.decorate(map);

        asrtMap("                " +
                "  ╔═══┐         " +
                "  ║S.$│         " +
                "  ║..B╚═════┐   " +
                "  ║B..B˃...$│   " +
                "  └─╗....BBB│   " +
                "    ║.B.....│   " +
                "    ║...˄B..╚═┐ " +
                "    ║.┌─╗˃....│ " +
                "    ║.│ ║B.B.$│ " +
                "    ║.│ ║...┌─┘ " +
                "    ║.│ ║.$┌┘   " +
                "    ║E│ └──┘    " +
                "    └─┘         " +
                "                " +
                "                ", decorate);
    }

    @Test
    public void testDemoLevel() {
        String map = Levels.DEMO_LEVEL;

        asrtMap("                " +
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
                "                ", map);

        String decorate = Levels.decorate(map);

        asrtMap("                " +
                " ╔════════════┐ " +
                " ║S...O.....˅.│ " +
                " ║˃.....$O....│ " +
                " ║....┌──╗....│ " +
                " ║....│  ║....│ " +
                " ║.O┌─┘  └─╗.O│ " +
                " ║.$│      ║..│ " +
                " ║..│      ║$.│ " +
                " ║O.╚═┐  ╔═╝O.│ " +
                " ║....│  ║....│ " +
                " ║....╚══╝....│ " +
                " ║....O$.....˂│ " +
                " ║.˄.....O...E│ " +
                " └────────────┘ " +
                "                ", decorate);
    }

    @Test
    public void testLevel13() {
        String map = Levels.LEVEL_9A;

        asrtMap("                " +
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
                "                ", map);

        String decorate = Levels.decorate(map);

        asrtMap("                " +
                "                " +
                "                " +
                "  ╔══════════┐  " +
                "  ║..........│  " +
                "  ║.┌──────╗.│  " +
                "  ║.│      ║.│  " +
                "  ║.│ ╔══┐ ║.│  " +
                "  ║.│ ║.S│ ║.│  " +
                "  ║.│ ║.┌┘ ║.│  " +
                "  ║.│ ║.│  ║.│  " +
                "  ║.│ ║.╚══╝.│  " +
                "  ║E│ ║......│  " +
                "  └─┘ └──────┘  " +
                "                " +
                "                ", decorate);
    }

    @Test
    public void testMultiLevel() {
        String map = Levels.MULTI_LEVEL;

        asrtMap("                                      " +
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
                "                                      ", map);

        String decorate = Levels.decorate(map);

        asrtMap("                                      " +
                "   ╔════┐                             " +
                "   ║...˅│                             " +
                "   ║BB.O│      ╔═════════┐ ╔══════┐   " +
                "   ║B...│      ║...B.BBBB│ ║˃.O..O│   " +
                "   ║..O.│  ╔═══╝˃.......O│ ║..$.BB│   " +
                "   ║˃...╚══╝......O......│ ║O...O˂│   " +
                "   ║..$......┌─╗....$..OO│ ║O....B│   " +
                "   ║B...┌────┘ ║.O.......│ ║B.┌───┘   " +
                "   ║B..O│      ║.........╚═╝B.│       " +
                "   └╗.┌─┘      ║..BOO.........│       " +
                "    ║.│        ║B.B....B.B..BB│       " +
                "    ║.│        └╗.B.┌─────╗B.B╚═┐     " +
                "    ║.│         ║BB.│     ║O..BB│     " +
                "    ║.│         ║...│     ║....˂│     " +
                "   ╔╝.╚═┐       ║.B.│  ╔══╝.....│     " +
                "   ║..B.│  ╔════╝.BB│  ║....BO.$│     " +
                "   ║...$│  ║B.......╚══╝.BB.B...│     " +
                "   ║O...╚══╝O...O........┌──────┘     " +
                "   ║..O.........S..O┌────┘            " +
                "   ║˄...┌──╗.OB.E...│                 " +
                "   ║BB..│  ║BBB....˄│                 " +
                "   └────┘  ║˃..O..$O│                 " +
                "           └───╗.┌──┘                 " +
                "               ║.│                    " +
                "            ╔══╝.╚════════┐           " +
                "            ║......$..B.BB│           " +
                "            ║.┌───╗.BB..BB│           " +
                "            ║.│   ║....O..│           " +
                "     ╔══════╝.╚┐  ║┌─╗....│           " +
                "     ║.....˂...╚══╝│ └─╗.┌┘           " +
                "     ║B.O....O.....│   ║.│            " +
                "     ║..O.$....┌─╗.╚═══╝.╚══┐         " +
                "     ║..O..O.BB│ ║.BB˃...O..│         " +
                "     └─────────┘ ║.....$....│         " +
                "                 └──╗.BO..OB│         " +
                "                    └───────┘         " +
                "                                      ", decorate);
    }

    private void asrtMap(String expected, String actual) {
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(actual));
    }

}
