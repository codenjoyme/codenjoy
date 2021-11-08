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
import java.util.LinkedList;
import java.util.List;

public interface Level {

    static List<String> extendBefunge(Level level, String... commands) {
        return new LinkedList<String>(){{
            addAll(level.befungeCommands());
            addAll(Arrays.asList(commands));
        }};
    }

    default String help() {
        return "You should check all cases.<br><br>" +
                "Remember ! Your program should work for all previous levels too.";
    }

    default String defaultCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    // TODO write your code here\n" +
                "}";
    }

    String winCode();

    default String refactoringCode() {
        return winCode();
    }

    String map();

    default String autocomplete() {
        return "{}";
    }

    default List<String> befungeCommands() {
        return Arrays.asList();
    }
}
