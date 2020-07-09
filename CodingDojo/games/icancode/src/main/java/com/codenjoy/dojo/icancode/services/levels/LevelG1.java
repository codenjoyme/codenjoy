package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class LevelG1 implements Level {
    
    @Override
    public String help() {
        return "The more gold you collect, the more points you earn. But zombies are on the way. <br><br>\n" +

                "Рay attention - the laser kills zombie. If it happens you will see \"ZOMBIE_DIE\" on board. <br><br>\n" +

                "Another way to get lasershow on board - fire. Yes you can do it ! Sorry, we forgot to tell you about that...<br><br>\n" +

                "There are several method for the Robot:\n" +
                "<pre>robot.fireLeft();\n" +
                "robot.fireRight();\n" +
                "robot.fireUp();\n" +
                "robot.fireDown();\n" +
                "robot.fire(\"LEFT\");</pre>\n" +
                "Good luck !<br><br>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return new LevelB1().defaultCode();
    }

    @Override
    public String winCode() {
        return new LevelF3().winCode(); // TODO реализовать
    }

    @Override
    public String map() {
        return  "  ############  ############# \n" +
                "  #..OB..$.BB#  #...B..B.O..# \n" +
                "  #.####..O..####.###...$...# \n" +
                "###.#  #B.....$...# #.####B.# \n" +
                "#.$.#  ###..B..O..# #O#  #..# \n" +
                "#.###    #.$.E....###.## #BB# \n" +
                "#O#      #.......B..$..# #..# \n" +
                "#.###    #B...O###B...$# #.$# \n" +
                "#.$$#  ###.B.B.# #.....# #B$# \n" +
                "#####  #.$.....# #B.$..# #..# \n" +
                "       #...B#### ##..O.# #.B# \n" +
                " #######.O..#     ###..# #..# \n" +
                " #$...B...$.#####   #.O# #..# \n" +
                " #..####BB......#####..# #.O# \n" +
                " #OB#  ######...$......# #..# \n" +
                " #..#       ##....###### #B.# \n" +
                " #$$# #####  #.O..#      #$$# \n" +
                " #..# #.$.#  #.$.B###### #.$# \n" +
                " #BB# #...####.......O.# #..# \n" +
                " #..# #....O...####B..## #### \n" +
                " #.O# ####..$..#  #####       \n" +
                " #..#    #$..O.#        ##### \n" +
                " #$$# ####....B######## #$$.# \n" +
                " #### #....O$...O...$.# #...# \n" +
                "      #.#####..######## ###B# \n" +
                "   ####$#   #..#          #.# \n" +
                "   #..O.#   #..#####  #####.# \n" +
                "####.####   #.O....####..$..# \n" +
                "#S...#      #...$..B.....#### \n" +
                "######      ##############    \n";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelF3(),
                "procedure-1", "procedure-2", "procedure-3");
    }

}
