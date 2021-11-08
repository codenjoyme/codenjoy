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
