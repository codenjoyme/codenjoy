package com.codenjoy.dojo.icancode.services.levels;

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
        return "  ############  ############# " +
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
    }

}
