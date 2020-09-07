package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class LevelB2 implements Level {
    
    @Override
    public String help() {
        return "If it’s very difficult, try to use debug.\n" +
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
                "        if (scanner.at(direction) != \"WALL\") {\n" +
                "            result.push(direction);\n" +
                "        }\n" +
                "        \n" +
                "        var previous = robot.previousDirection();\n" +
                "        var index = result.indexOf(previous);\n" +
                "        if (index != -1) {\n" +
                "          result.splice(index, 1);\n" +
                "          result.splice(0, 0, previous)\n" +
                "        }\n" +
                "    }\n" +
                "    return result;\n" +
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
