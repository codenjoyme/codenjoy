package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class Level7 implements Level {
    
    @Override
    public String help() {
        return "It is too hard to solve this issue without refactoring.<br>\n" +
                "You can use new FOR construction:<br>\n" +
                "<pre>var directions = [\"RIGHT\", \"DOWN\", \"LEFT\", \"UP\"];\n" +
                "for (var index in directions) {\n" +
                "    var direction = directions[index];\n" +
                "    // do something with current direction\n" +
                "}</pre>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String winCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    if (robot.cameFrom() != null &&\n" +
                "        scanner.at(robot.previousDirection()) != \"WALL\")\n" +
                "    {\n" +
                "        robot.go(robot.previousDirection());\n" +
                "    } else {\n" +
                "        robot.go(freeDirection(scanner, robot));\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "function freeDirection(scanner, robot) {\n" +
                "    var directions = [\"RIGHT\", \"DOWN\", \"LEFT\", \"UP\"];\n" +
                "    for (var index in directions) {\n" +
                "        var direction = directions[index];\n" +
                "        if (robot.cameFrom() == direction) {\n" +
                "            continue;\n" +
                "        }\n" +
                "        if (scanner.at(direction) != \"WALL\") {\n" +
                "            return direction;\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    @Override
    public String map() {
        return  "        \n" +
                " ###    \n" +
                " #E#    \n" +
                " #.#    \n" +
                " #.#### \n" +
                " #...S# \n" +
                " ###### \n" +
                "        \n";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new Level6(),
                "value-up");
    }

}
