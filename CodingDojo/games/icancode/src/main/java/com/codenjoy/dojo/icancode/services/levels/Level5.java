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

public class Level5 implements Level {
    
    @Override
    public String help() {
        return "Oops ! Looks like we didn’t predict this situation.<br>\n" +
                "Think how to adapt the code to these new conditions.<br>\n" +
                "Use refactoring to make your code more abstract.<br><br>\n" +

                "Уou can complicate IF conditions by using operators AND/OR/NOT:<br>\n" +
                "<pre>if (variable1 && !variable2 || variable3) {\n" +
                "    // this code will run IF\n" +
                "    //          variable1 IS true AND variable2 IS true\n" +
                "    //       OR variable3 IS true (ignoring variable1, variable2)\n" +
                "}</pre>" +
                "These operators allow you to use any combination.<br><br>\n" +

                "Уou can extract functions, create new local variables:<br>\n" +
                "<pre>function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    var localVariable = newFunction(scanner);\n" +
                "}\n" +
                "function newFunction(scanner) {\n" +
                "    return \"some data\";\n" +
                "}</pre>" +
                "New function used for encapsulate algorithm.<br>\n" +
                "Local variable saves value only during current step.<br><br>\n" +

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
                "        robot.go(freeDirection(scanner));\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "function freeDirection(scanner) {\n" +
                "    if (scanner.atRight() != \"WALL\") {\n" +
                "        return \"RIGHT\";\n" +
                "    } else if (scanner.atDown() != \"WALL\") {\n" +
                "        return \"DOWN\";\n" +
                "    } else if (scanner.atLeft() != \"WALL\") {\n" +
                "        return \"LEFT\";\n" +
                "    } else {\n" +
                "        return \"UP\";\n" +
                "    }\n" +
                "}";
    }

    @Override
    public String map() {
        return  "        \n" +
                " ###### \n" +
                " #S...# \n" +
                " ####.# \n" +
                "    #.# \n" +
                "    #E# \n" +
                "    ### \n" +
                "        \n";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new Level4(),
                "if", "scanner-at",
                "value-right", "value-down",
                "value-wall", "value-ground");
    }

}
