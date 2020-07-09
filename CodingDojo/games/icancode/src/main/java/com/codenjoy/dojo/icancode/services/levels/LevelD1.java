package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class LevelD1 implements Level {
    
    @Override
    public String help() {
        return "On this Maze you can see a lot of boxes. \n" +
                "You can jump over box and pull/push any box.<br><br>\n" +

                "It is possible to move the box only forward or backward, \"side pulling\" is not allowed.<br><br>\n" +

                "There are 4 corresponding functions for each direction: \n" +
                "pullLeft, pullRight, pullUp and pullDown. Also you can use generic pull method:<br>\n" +
                "<pre>robot.pull(\"UP\");\n" +
                "// same as\n" +
                "robot.pullUp();</pre>\n" +

                "If you want to find box on map - try use \"BOX\" element.<br><br>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return new LevelB1().defaultCode();
    }

    @Override
    public String winCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    var dest = destination(scanner);\n" +
                "    var to = scanner.getShortestWay(dest[0])[1];\n" +
                "    var from = scanner.getMe();\n" +
                "\n" +
                "    robot.goOver = function(direction) {\n" +
                "        if (scanner.at(direction) != \"HOLE\" && \n" +
                "            scanner.at(direction) != \"BOX\") \n" +
                "        {\n" +
                "            robot.go(direction);\n" +
                "        } else {\n" +
                "            robot.jump(direction);\n" +
                "        }\n" +
                "    };\n" +
                "    \n" +
                "    var dx = to.getX() - from.getX(); \n" +
                "    var dy = to.getY() - from.getY(); \n" +
                "    if (dx > 0) {\n" +
                "        robot.goOver(\"RIGHT\");\n" +
                "    } else if (dx < 0) {\n" +
                "        robot.goOver(\"LEFT\");\n" +
                "    } else if (dy > 0) {\n" +
                "        robot.goOver(\"UP\");\n" +
                "    } else if (dy < 0) {\n" +
                "        robot.goOver(\"DOWN\");\n" +
                "    }\n" +
                "}\n" +
                "\n" +
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
        return  "              \n" +
                "   ########   \n" +
                "   #S...B.#   \n" +
                "   ###B...#   \n" +
                "     #B...#   \n" +
                "   ###$B..####\n" +
                "   #$....B..B#\n" +
                "   #.#####...#\n" +
                "   #.#   #...#\n" +
                "   #.#####.B.#\n" +
                "   #.E.....B$#\n" +
                "   ###########\n" +
                "              \n" +
                "              \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "	'robot.':{" +
                "		'synonyms':[]," +
                "		'values':['pull()', 'pullLeft()', 'pullRight()', 'pullUp()', 'pullDown()']" +
                "	}," +
                "	'.pull(':{" +
                "		'synonyms':[]," +
                "		'values':['\\'RIGHT\\'', '\\'DOWN\\'', '\\'LEFT\\'', '\\'UP\\'']" +
                "	}" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelC2(),
                "value-box",
                "robot-pull");
    }
}
