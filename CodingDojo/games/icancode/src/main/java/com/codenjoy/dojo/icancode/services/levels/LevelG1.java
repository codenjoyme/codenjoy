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

public class LevelG1 implements Level {
    
    @Override
    public String help() {
        return "This level is too big to fit on the screen. " +
                "You have to learn how to store a complete map as you move. <br><br>\n" +

                "Don't forget the perks, they are on the way !<br><br>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return new LevelB1().defaultCode();
    }

    @Override
    public String winCode() {
        return new LevelF3().winCode(); // TODO реализовать
    }

    @Override
    public String map() {
        return  "  ############  ############# \n" +
                "  #..OB..$.BB#  #...B..B.O..# \n" +
                "  #.####..O..####.###...$...# \n" +
                "###.#  #B.....$...# #.####B.# \n" +
                "#.$.#  ###..B..O..# #O#  #..# \n" +
                "#.###    #.$.E....###.## #BB# \n" +
                "#O#      #.......B..$..# #..# \n" +
                "#.###    #B...O###B...$# #.$# \n" +
                "#.$$#  ###.B.B.# #.....# #B$# \n" +
                "#####  #.$.....# #B.$..# #..# \n" +
                "       #...B#### ##..O.# #.B# \n" +
                " #######.O..#     ###..# #..# \n" +
                " #$...B...$.#####   #.O# #..# \n" +
                " #..####BB......#####..# #.O# \n" +
                " #OB#  ######...$......# #..# \n" +
                " #..#       ##....###### #B.# \n" +
                " #$$# #####  #.O..#      #$$# \n" +
                " #..# #.$.#  #.$.B###### #.$# \n" +
                " #BB# #...####.......O.# #..# \n" +
                " #..# #....O...####B..## #### \n" +
                " #.O# ####..$..#  #####       \n" +
                " #..#    #$..O.#        ##### \n" +
                " #$$# ####m...B######## #$$.# \n" +
                " #### #....O$...O...$.# #...# \n" +
                "      #.#####.a######## ###B# \n" +
                "   ####$#   #..#          #.# \n" +
                "   #..O.#   #..#####  #####.# \n" +
                "####.####   #.O....####..$..# \n" +
                "#S.j.#      #...$..B.....#### \n" +
                "######      ##############    \n";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelF3(),
                "procedure-1", "procedure-2", "procedure-3");
    }

}
