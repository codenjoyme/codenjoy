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

public class LevelF2 implements Level {
    
    @Override
    public String help() {
        return "Zombie in your way. You will meet !<br><br>\n" +

                "By the way, we did not tell you, but through Zombie you can also jump over:\n" +
                "<pre>robot.jump();\n" +
                "robot.jumpLeft();\n" +
                "robot.jump(\"RIGHT\");</pre>\n" +

                "Don't forget the \"JUMP_PERK\" to jump.<br><br>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return new LevelB1().defaultCode();
    }

    @Override
    public String winCode() {
        return new LevelF1().winCode(); // TODO реализовать
    }

    @Override
    public String map() {
        return  "              \n" +
                "              \n" +
                " ############ \n" +
                " #S.j.......# \n" +
                " ##########.# \n" +
                "          #.# \n" +
                " ##########.# \n" +
                " #....Z.....# \n" +
                " #.########## \n" +
                " #.#          \n" +
                " #.########## \n" +
                " #.........E# \n" +
                " ############ \n" +
                "              \n";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelF1());
    }

}
