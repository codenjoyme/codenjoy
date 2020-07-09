package com.codenjoy.dojo.icancode.services.levels;

import java.util.Arrays;
import java.util.List;

public class Level2 implements Level {
    
    @Override
    public String help() {
        return "Looks like the Maze was changed. Our old program will not help.<br><br>\n" +

                "We need to change it ! The robot must learn how to use the scanner.<br>\n" +
                "Scanner will help robot to detect walls and other obstacles.<br>\n" +

                "To use scanner is necessary to execute the following code:<br><br>\n" +
                "<pre>function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    if (scanner.atRight() != \"WALL\") {\n" +
                "        robot.goRight();\n" +
                "    } else {\n" +
                "        // TODO Uncomment one line that will help\n" +
                "        // robot.goDown();\n" +
                "        // robot.goUp();\n" +
                "        // robot.goLeft();\n" +
                "        // robot.goRight();\n" +
                "    }\n" +
                "}</pre>\n" +
                "In this code, you can see the new IF-ELSE construction:<br>\n" +
                "<pre>if (expression) {\n" +
                "    // statement\n" +
                "} else {\n" +
                "    // statement\n" +
                "}</pre>\n" +

                "Be careful ! The program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    if (scanner.atRight() != \"WALL\") {\n" +
                "        robot.goRight();\n" +
                "    } else {\n" +
                "        // TODO write your code here\n" +
                "    }\n" +
                "}";
    }

    @Override
    public String winCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    if (scanner.atRight() != \"WALL\") {\n" +
                "        robot.goRight();\n" +
                "    } else {\n" +
                "        robot.goDown();\n" +
                "    }\n" +
                "}";
    }

    @Override
    public String map() {
        return  "        \n" +
                "   ###  \n" +
                "   #S#  \n" +
                "   #.#  \n" +
                "   #.#  \n" +
                "   #E#  \n" +
                "   ###  \n" +
                "        \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "    'robot.':{" +
                "        'synonyms':[]," +
                "        'values':['getScanner()']" +
                "    }," +
                "    'scanner.':{" +
                "        'synonyms':['robot.getScanner().']," +
                "        'values':['atRight()', 'atLeft()', 'atUp()', 'atDown()']" +
                "    }," +
                "    ' == ':{" +
                "        'synonyms':[' != ']," +
                "        'values':['\\'WALL\\'']" +
                "    }" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new Level1(), "robot-down");
    }
}
