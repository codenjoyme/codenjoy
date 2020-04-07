package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class Level4 implements Level {
    
    @Override
    public String help() {
        return "Try to solve it by adding new IF. Now it should be easy !<br><br>\n" +

                "You can use this method to show data in console:<br>\n" +
                "<pre>var someVariable = \"someData\";\n" +
                "robot.log(someVariable);</pre>\n" +

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
                "        } else if (scanner.atLeft() != \"WALL\") {\n" +
                "            robot.goLeft();\n" +
                "        } else {\n" +
                "            robot.goUp();\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    @Override
    public String map() {
        return  "        \n" +
                "   ###  \n" +
                "   #E#  \n" +
                "   #.#  \n" +
                "   #.#  \n" +
                "   #S#  \n" +
                "   ###  \n" +
                "        \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "    'robot.':{" +
                "	     'synonyms':[]," +
                "	     'values':['log()']" +
                "    }" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new Level3(), "robot-up");
    }
}
