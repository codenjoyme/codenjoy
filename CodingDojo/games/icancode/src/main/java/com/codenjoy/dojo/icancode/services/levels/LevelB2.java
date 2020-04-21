package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class LevelB2 implements Level {
    
    @Override
    public String help() {
        return "You can use new methods for the scanner:<br>\n" +
                "<pre>var goldPoints = scanner.getGold();\n" +
                "var startPoint = scanner.getStart();\n" +
                "var exitPoint = scanner.getExit();\n" +
                "var robotPoint = scanner.getMe();</pre>\n" +
                "Coordinate {x:0, y:0} in the left-top corner of board.<br><br>\n" +

                "Try this code for check Robot position.<br>\n" +
                "<pre>robot.log(scanner.getMe());</pre>\n" +
                "So you should collect all the golden bags in the Maze.<br><br>\n" +

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
                "   ####   \n" +
                "   #$$#   \n" +
                "   #$$#   \n" +
                " ###..### \n" +
                " #S....E# \n" +
                " ###..### \n" +
                "   #$$#   \n" +
                "   #$$#   \n" +
                "   ####   \n";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelB1());
    }
}
