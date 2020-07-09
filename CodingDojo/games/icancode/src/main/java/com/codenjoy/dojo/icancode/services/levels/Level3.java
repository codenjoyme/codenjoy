package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class Level3 implements Level {
    
    @Override
    public String help() {
        return "This Maze is very similar to the previous. But it’s not so easy.<br>\n" +
                "Try to solve it by adding new IF.<br>\n" +
                "You can use new methods for refactoring:<br>\n" +
                "<pre>scanner.at(\"RIGHT\");\n" +
                "robot.go(\"LEFT\");</pre>\n" +

                "If you want to know where we came from - use this expression:<br>\n" +
                "<pre>robot.cameFrom() == \"LEFT\"</pre>\n" +

                "If you want to know where we came to on our previous step, use:<br>\n" +
                "<pre>robot.previousDirection() == \"RIGHT\"</pre>\n" +

                "You can use these commands with previous to tell robot to go on one direction, like:<br>\n" +
                "<pre>robot.go(robot.previousDirection());</pre>\n" +

                "Be careful ! The program should work for all previous levels too.";
    }

    @Override
    public String winCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    if (robot.cameFrom() != null) {\n" +
                "        robot.go(robot.previousDirection());\n" +
                "    } else {\n" +
                "        if (scanner.atRight() != \"WALL\") {\n" +
                "            robot.goRight();\n" +
                "        } else if (scanner.atDown() != \"WALL\") {\n" +
                "            robot.goDown();\n" +
                "        } else {\n" +
                "            robot.goLeft();\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    @Override
    public String map() {
        return  "        \n" +
                "        \n" +
                " ###### \n" +
                " #E..S# \n" +
                " ###### \n" +
                "        \n" +
                "        \n" +
                "        \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "    'robot.':{" +
                "        'synonyms':[]," +
                "        'values':['cameFrom()', 'previousDirection()', 'go()']" +
                "    }," +
                "    'scanner.':{" +
                "        'synonyms':['robot.getScanner().']," +
                "        'values':['at()']" +
                "    }," +
                "    ' == ':{" +
                "        'synonyms':[' != ']," +
                "        'values':['\\'RIGHT\\'', '\\'DOWN\\'', '\\'LEFT\\'', '\\'UP\\'', 'null']" +
                "    }," +
                "    '.at(':{" +
                "        'synonyms':['.go(']," +
                "        'values':['\\'RIGHT\\'', '\\'DOWN\\'', '\\'LEFT\\'', '\\'UP\\'']" +
                "    }" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new Level2(), "robot-left");
    }
}
