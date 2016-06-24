package com.epam.dojo.icancode.services;

import com.epam.dojo.icancode.model.ILevel;
import com.epam.dojo.icancode.model.LevelImpl;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by oleksandr.baglai on 18.06.2016.
 */
public class
Levels {

    private static final String DEMO_LEVEL =
            "                " +
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
            "                ";

    private static final String MULTI_LEVEL =
            "                                      " +
            "   ╔════┐                             " +
            "   ║...˅│                             " +
            "   ║BB.O│      ╔═════════┐ ╔══════┐   " +
            "   ║B...│      ║...B.BBBB│ ║˃.O..O│   " +
            "   ║..O.│  ╔═══╝˃.......O│ ║....BB│   " +
            "   ║˃...╚══╝......O......│ ║O...O˂│   " +
            "   ║.........┌─╗.......OO│ ║O....B│   " +
            "   ║B...┌────┘ ║.O.......│ └╗.┌───┘   " +
            "   ║B..O│      ║.........╚══╝.│       " +
            "   └╗.┌─┘      ║.BBOO.........│       " +
            "    ║.│        ║BBB....BB┌────┘       " +
            "    ║.│        └──╗.┌────┘╔═════┐     " +
            "    ║.│         ╔═╝.│     ║O..BB│     " +
            "    ║.│         ║...│     ║....˂│     " +
            "   ╔╝.╚═┐       ║.┌─┘  ╔══╝.....│     " +
            "   ║..B.│  ╔════╝.╚═┐  ║....BO..│     " +
            "   ║....│  ║B.......╚══╝.┌╗BB...│     " +
            "   ║O...╚══╝O...O........│└─────┘     " +
            "   ║..O.........S..O┌────┘            " +
            "   ║˄...┌──╗.OB.....│                 " +
            "   ║BB..│  ║BBB....˄│                 " +
            "   └────┘  ║˃..O...O│                 " +
            "           └───╗.┌──┘                 " +
            "               ║.│                    " +
            "            ╔══╝.╚══┐╔════┐           " +
            "            ║.......│║B.BB│           " +
            "            ║.┌───╗.╚╝..BB│           " +
            "            ║.│   ║....O..│           " +
            "     ╔══════╝.╚┐  └──╗....│           " +
            "     ║.....˂...╚═══┐ ╚─╗.┌┘           " +
            "     ║B.O....O.....│   ║.│            " +
            "     ║..O......┌─╗.│╔══╝.╚══┐         " +
            "     ║..O..O.BB│ ║.╚╝˃...O..│         " +
            "     └─────────┘ ║..........│         " +
            "                 └──╗.BO..OB│         " +
            "                    └───────┘         " +
            "                                      ";


    private static final String LEVEL1 =
            "                " +
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
            "                ";

    private static final String LEVEL2 =
            "                " +
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
            "                ";

    private static final String LEVEL3 =
            "                " +
            "                " +
            "                " +
            "                " +
            "    ╔══════┐    " +
            "    ║S.O..$│    " +
            "    ║......│    " +
            "    └──╗...│    " +
            "    ╔══╝.O.│    " +
            "    ║......│    " +
            "    ║...O.E│    " +
            "    └──────┘    " +
            "                " +
            "                " +
            "                " +
            "                ";

    private static final String LEVEL4 =
            "                " +
            "                " +
            "                " +
            "    ╔═════┐     " +
            "    ║S.O..│     " +
            "    └──╗..│     " +
            "    ╔══╝..╚═┐   " +
            "    ║$..OO..│   " +
            "    ║.┌─╗...│   " +
            "    ║.╚═╝..E│   " +
            "    ║.......│   " +
            "    └───────┘   " +
            "                " +
            "                " +
            "                " +
            "                ";

    public static List<ILevel> collect() {
        return collect(LEVEL1, LEVEL2, LEVEL3, LEVEL4, MULTI_LEVEL);
    }

    private static List<ILevel> collect(String... levels) {
        List<ILevel> result = new LinkedList<ILevel>();
        for (String level : levels) {
            result.add(new LevelImpl(level));
        }
        return result;
    }

    public static int size() {
        return 16; // TODO think about it
    }
}
