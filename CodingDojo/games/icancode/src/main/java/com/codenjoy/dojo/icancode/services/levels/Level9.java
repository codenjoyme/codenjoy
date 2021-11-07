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

public class Level9 implements Level {
    
    @Override
    public String help() {
        return "Let's complicate the task. If you have developed your algorithm \n" +
                "well, the Robot will not get lost here. Check it out!<br><br>\n" +
                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String winCode() {
        return new Level8().winCode();
    }

    @Override
    public String map() {
        return  "              \n" +
                "              \n" +
                " ############ \n" +
                " #..........# \n" +
                " #.########.# \n" +
                " #.#      #.# \n" +
                " #.# #### #.# \n" +
                " #.# #.S# #.# \n" +
                " #.# #.## #.# \n" +
                " #.# #.#  #.# \n" +
                " #.# #.####.# \n" +
                " #E# #......# \n" +
                " ### ######## \n" +
                "              \n";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new Level8(),
                "value-null", "robot-came-from",
                "robot-go", "robot-previous-direction",
                "mirror-top-bottom", "mirror-bottom-top");
    }

}
