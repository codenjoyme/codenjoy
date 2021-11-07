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

public class LevelF3 implements Level {
    
    @Override
    public String help() {
        return "The more gold you collect, the more points you earn. But zombies are on the way. <br><br>\n" +

                "Рay attention - the laser kills zombie. If it happens you will see \"ZOMBIE_DIE\" on board. <br><br>\n" +

                "Another way to get lasershow on board - fire. Yes you can do it ! " +
                "Before you can fire take the \"FIRE_PERK\" near you.<br><br>\n" +

                "There are several method for the Robot:\n" +
                "<pre>robot.fireLeft();\n" +
                "robot.fireRight();\n" +
                "robot.fireUp();\n" +
                "robot.fireDown();\n" +
                "robot.fire(\"LEFT\");</pre>\n" +
                "Good luck !<br><br>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return new LevelB1().defaultCode();
    }

    @Override
    public String winCode() {
        return new LevelF2().winCode(); // TODO реализовать
    }

    @Override
    public String map() {
        return  "    ############### \n" +
                "    #Z.....E...$.Z# \n" +
                "    #B...O###B....# \n" +
                "  ###.B.B.# #.....# \n" +
                "  #.$.....# #B.$..# \n" +
                "  #...B#### ##..O.# \n" +
                "  #.O..#     ###..# \n" +
                "  #..$.#####   #.O# \n" +
                "  #BB......#####..# \n" +
                "  ######˃.........# \n" +
                "       ##....###### \n" +
                " #####  #.O..#      \n" +
                " #.$.#  #.$.B###### \n" +
                " #...####.......O.# \n" +
                " #....O...####B...# \n" +
                " ####..$..#  ###### \n" +
                "    #...O.#         \n" +
                " ####....B########  \n" +
                " #S.a.O$........S#  \n" +
                " #################  \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "	'robot.': {" +
                "		'synonyms': []," +
                "		'values': ['fireLeft()', 'fireUp()', 'fireLeft()', 'fireRight()', 'fire()']" +
                "	}," +
                "	'.fire(': {" +
                "		'synonyms': []," +
                "		'values': ['\\'RIGHT\\'', '\\'DOWN\\'', '\\'LEFT\\'', '\\'UP\\'']" +
                "	}," +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelF2(),
                "value-fire-perk",
                "robot-fire");
    }
}
