package com.codenjoy.dojo.icancode.services.levels;

public class Level8 implements Level {

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
        return "        " +
                " ###### " +
                " #...E# " +
                " #.#### " +
                " #.#    " +
                " #S#    " +
                " ###    " +
                "        ";
    }

}
