package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class LevelF3 implements Level {
    
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
        return new LevelF2().winCode(); // TODO реализовать
    }

    @Override
    public String map() {
        return "    ############### " +
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
    }

    @Override
    public String autocomplete() {
        return "{" +
                "	'robot.': {" +
                "		'synonyms': []," +
                "		'values': ['fireLeft()', 'fireUp()', 'fireLeft()', 'fireRight()', 'fire()']" +
                "	}," +
                "	'.fire(': {" +
                "		'synonyms': []," +
                "		'values': ['\\'RIGHT\\'', '\\'DOWN\\'', '\\'LEFT\\'', '\\'UP\\'']" +
                "	}," +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelF2(),
                // TODO этой команды в befunge не существует (достаточно одной универсальной команды)
                "robot-fire");
    }
}
