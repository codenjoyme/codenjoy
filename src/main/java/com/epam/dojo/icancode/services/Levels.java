package com.epam.dojo.icancode.services;

import com.epam.dojo.icancode.model.Level;
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

    private static final String LEVEL5 = DEMO_LEVEL;

    public static List<Level> collect() {
        return collect(LEVEL1, LEVEL2, LEVEL3, LEVEL4, LEVEL5, DEMO_LEVEL);
    }

    private static List<Level> collect(String... levels) {
        List<Level> result = new LinkedList<Level>();
        for (String level : levels) {
            result.add(new LevelImpl(level));
        }
        return result;
    }

    public static int size() {
        return 16; // TODO think about it
    }
}
