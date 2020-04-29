package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class LevelB1 implements Level {
    
    @Override
    public String help() {
        return "Look at that gold! You can get extra points for it. \n" +
                "Most likely the Robot will simply ignore it. \n" +
                "Try retraining the Robot and collect all golden bags.<br><br>\n" +

                "You can use new methods for the scanner:<br>\n" +
                "<pre>var goldPoints = scanner.getGold();\n" +
                "var startPoint = scanner.getStart();\n" +
                "var exitPoint = scanner.getExit();\n" +
                "var robotPoint = scanner.getMe();</pre>\n" +
                "Coordinate {x:0, y:0} in the left-bottom corner of board.<br><br>\n" +

                "Try this code for check Robot position.<br>\n" +
                "<pre>robot.log(scanner.getMe());</pre>\n" +
                "So you should collect all golden bags in the Maze.<br><br>\n" +

                "If it’s very difficult, try to use debug.\n" +
                "Just open browser console (Ctrl-Shift-J),\n" +
                "write \\'debugger;\\' in your code,\n" +
                "then press \\'COMMIT\\' button again.\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    var dest = scanner.getGold();\n" +
                "    var start = scanner.getStart();\n" +
                "    var exit = scanner.getExit();\n" +
                "    var from = scanner.getMe();\n" +
                "    // TODO write your code here\n" +
                "}";
    }

    @Override // TODO попробовать решить без getShortestWay
    public String winCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    var dest = scanner.getGold();\n" +
                "    if (dest.length === 0) {\n" +
                "        dest = scanner.getExit();\n" +
                "    }\n" +
                "    var to = scanner.getShortestWay(dest[0])[1];\n" +
                "    var from = scanner.getMe();\n" +
                "    \n" +
                "    var dx = to.getX() - from.getX();\n" +
                "    var dy = to.getY() - from.getY();\n" +
                "    if (dx > 0) {\n" +
                "        robot.goRight();\n" +
                "    } else if (dx < 0) {\n" +
                "        robot.goLeft();\n" +
                "    } else if (dy > 0) {\n" +
                "        robot.goDown();\n" +
                "    } else if (dy < 0) {\n" +
                "        robot.goUp();\n" +
                "    }\n" +
                "}";
    }

    @Override
    public String map() {
        return  "          \n" +
                "          \n" +
                " ######## \n" +
                " #S....E# \n" +
                " ###..### \n" +
                "   #$$#   \n" +
                "   #$$#   \n" +
                "   ####   \n" +
                "          \n" +
                "          \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "	'scanner.':{" +
                "		'synonyms':['robot.getScanner().']," +
                "		'values':['getGold()', 'getExit()', 'getStart()', 'getMe()']" +
                "	}" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new Level9(),
                "value-gold", "value-end", "value-start");
    }
}
