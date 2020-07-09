package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class LevelB4 implements Level {
    
    @Override
    public String help() {
        return "Wow! You can use this amazing method for the scanner:<br>\n" +
                "<pre>var destinationPoints = scanner.getGold();\n" +
                "var nextPoint = scanner.getShortestWay(destinationPoints[0]);</pre>\n" +
                "Coordinate {x:0, y:0} in the left-bottom corner of board.<br><br>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    var dest = scanner.getGold();\n" +
                "    var next = scanner.getShortestWay(dest[0])[1];\n" +
                "    var exit = scanner.getExit();\n" +
                "    var from = scanner.getMe();\n" +
                "    // TODO write your code here\n" +
                "}";
    }

    @Override
    public String winCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    var dest = destination(scanner);\n" +
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
                "        robot.goUp();\n" +
                "    } else if (dy < 0) {\n" +
                "        robot.goDown();\n" +
                "    }\n" +
                "}\n" +
                "function destination(scanner) {\n" +
                "    var result = scanner.getGold();\n" +
                "    if (result.length > 0) {\n" +
                "        return result;\n" +
                "    }\n" +
                "    return scanner.getExit();\n" +
                "}";
    }

    @Override
    public String map() {
        return  "  ###        \n" +
                "  #E#########\n" +
                "  #...$$$...#\n" +
                "  #########.#\n" +
                "          #$#\n" +
                "  #########.#\n" +
                "  #....S....#\n" +
                "  #.#########\n" +
                "  #$#        \n" +
                "  #.#########\n" +
                "  #...$$$...#\n" +
                "  #########E#\n" +
                "          ###\n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "	'scanner.':{" +
                "		'synonyms':['robot.getScanner().']," +
                "		'values':['getShortestWay()']" +
                "	}" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelB2(),
                "shortest-way", "check-stack",
                "print-stack");
    }
}
