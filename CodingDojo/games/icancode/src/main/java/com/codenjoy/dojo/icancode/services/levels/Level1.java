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

import java.util.Arrays;
import java.util.List;

public class Level1 implements Level {
    
    @Override
    public String help() {
        return "Robot asks for new orders every second. He should know where to go.<br>\n" +
                "Help him - write program and save him from the Maze. <br><br>\n" +

                "The code looks like this:<br>\n" +
                "<pre>function program(robot) {\n" +
                "    // TODO Uncomment one line that will help\n" +
                "    // robot.goDown();\n" +
                "    // robot.goUp();\n" +
                "    // robot.goLeft();\n" +
                "    // robot.goRight();\n" +
                "}</pre>\n" +
                "Send program to Robot by clicking the Commit button.<br>\n" +
                "If something is wrong - check Robot message in the Console (the rightmost field).<br><br>\n" +

                "You can always stop the program by clicking the Reset button.";
    }

    @Override
    public String defaultCode() {
        return "function program(robot) {\n" +
                "    // TODO Uncomment one line that will help\n" +
                "    // robot.goDown();\n" +
                "    // robot.goUp();\n" +
                "    // robot.goLeft();\n" +
                "    // robot.goRight();\n" +
                "}";
    }

    @Override
    public String winCode() {
        return "function program(robot) {\n" +
                "    robot.goRight();\n" +
                "}";
    }

    @Override
    public String map() {
        return  "        \n" +
                "        \n" +
                " ###### \n" +
                " #S..E# \n" +
                " ###### \n" +
                "        \n" +
                "        \n" +
                "        \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "    \"robot.\":{" +
                "        \"synonyms\":[]," +
                "        \"values\":[\"goDown()\", \"goUp()\", \"goLeft()\", \"goRight()\"]" +
                "    }" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Arrays.asList("start", "finish", "robot-right");
    }
}
