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

public class LevelF1 implements Level {
    
    @Override
    public String help() {
        return "There is a lot of gold on this maze. But it seems we are not alone here.<br>\n" +
                "Hurry, the Zombie are not as fast as you.<br><br>\n" +

                "Scanner will help Robot to detect Zombie also. If you want to find Zombie on map - try use \"ZOMBIE\" element.<br>\n" +
                "<pre>var scanner = robot.getScanner();\n" +
                "if (scanner.at(\"LEFT\") == \"ZOMBIE\") {\n" +
                "    // some statement here\n" +
                "}</pre>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return new LevelB1().defaultCode();
    }

    @Override
    public String winCode() {
        return new LevelE1().winCode(); // TODO реализовать
    }

    @Override
    public String map() {
        return  "                \n" +
                "  ##### ####### \n" +
                "  #S..# #....Z# \n" +
                "  #...###...### \n" +
                "  #...$...$.#   \n" +
                "  ###.......#   \n" +
                "    #..$....#   \n" +
                "    #.....$.### \n" +
                "    #.###....E# \n" +
                "    #$# #.$...# \n" +
                "    #.###...### \n" +
                "    #...$..##   \n" +
                "    #.######    \n" +
                "    ###         \n" +
                "                \n" +
                "                \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "	' == ':{" +
                "		'synonyms':[' != ']," +
                "		'values':['\\'ZOMBIE\\'']" +
                "	}" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelE1(),
                "value-zombie");
    }
}
