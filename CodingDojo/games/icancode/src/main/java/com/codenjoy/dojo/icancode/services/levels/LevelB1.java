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

    @Override
    public String winCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    \n" +
                "    var where = freeDirections(scanner, robot);\n" +
                "    var dest = destination(scanner);\n" +
                "    var me = scanner.getMe();\n" +
                "    \n" +
                "    var min = 1000;\n" +
                "    var direction = null;\n" +
                "    for (var i in where) {\n" +
                "        var from = next(me, where[i]);\n" +
                "        for (var j in dest) {\n" +
                "            var len = distance(from, dest[j]);\n" +
                "            if (min > len) {\n" +
                "                direction = where[i];\n" +
                "                min = len;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    robot.go(direction);\n" +
                "}\n" +
                "\n" +
                "function destination(scanner) {\n" +
                "    var result = scanner.getGold();\n" +
                "    if (result.length > 0) {\n" +
                "        return result;\n" +
                "    }\n" +
                "    return scanner.getExit();\n" +
                "}\n" +
                "\n" +
                "function next(from, direction) {\n" +
                "    return Direction.get(direction).change(from);\n" +
                "}\n" +
                "\n" +
                "function distance(from, to) {\n" +
                "    return Math.sqrt(\n" +
                "        Math.pow(to.getX() - from.getX(), 2) +\n" +
                "        Math.pow(to.getY() - from.getY(), 2)\n" +
                "    );\n" +
                "}\n" +
                "\n" +
                "function freeDirections(scanner, robot) {\n" +
                "    var directions = [\"RIGHT\", \"DOWN\", \"LEFT\", \"UP\"];\n" +
                "    var result = [];\n" +
                "    for (var index in directions) {\n" +
                "        var direction = directions[index];\n" +
                "        if (robot.cameFrom() == direction) {\n" +
                "            continue;\n" +
                "        }\n" +
                "        if (scanner.at(direction) != \"WALL\") {\n" +
                "            result.push(direction);\n" +
                "        }\n" +
                "    }\n" +
                "    return result;\n" +
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
