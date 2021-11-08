package com.codenjoy.dojo.icancode.services.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.List;

public class Level3 implements Level {
    
    @Override
    public String help() {
        return "This Maze is very similar to the previous. But it’s not so easy.<br>\n" +
                "Try to solve it by adding new IF.<br>\n" +
                "You can use new methods for refactoring:<br>\n" +
                "<pre>scanner.at(\"RIGHT\");\n" +
                "robot.go(\"LEFT\");</pre>\n" +

                "If you want to know where we came from - use this expression:<br>\n" +
                "<pre>robot.cameFrom() == \"LEFT\"</pre>\n" +

                "If you want to know where we came to on our previous step, use:<br>\n" +
                "<pre>robot.previousDirection() == \"RIGHT\"</pre>\n" +

                "You can use these commands with previous to tell robot to go on one direction, like:<br>\n" +
                "<pre>robot.go(robot.previousDirection());</pre>\n" +

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
                "        } else {\n" +
                "            robot.goLeft();\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    @Override
    public String map() {
        return  "        \n" +
                "        \n" +
                " ###### \n" +
                " #E..S# \n" +
                " ###### \n" +
                "        \n" +
                "        \n" +
                "        \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "    'robot.':{" +
                "        'synonyms':[]," +
                "        'values':['cameFrom()', 'previousDirection()', 'go()']" +
                "    }," +
                "    'scanner.':{" +
                "        'synonyms':['robot.getScanner().']," +
                "        'values':['at()']" +
                "    }," +
                "    ' == ':{" +
                "        'synonyms':[' != ']," +
                "        'values':['\\'RIGHT\\'', '\\'DOWN\\'', '\\'LEFT\\'', '\\'UP\\'', 'null']" +
                "    }," +
                "    '.at(':{" +
                "        'synonyms':['.go(']," +
                "        'values':['\\'RIGHT\\'', '\\'DOWN\\'', '\\'LEFT\\'', '\\'UP\\'']" +
                "    }" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new Level2(), "robot-left");
    }
}
