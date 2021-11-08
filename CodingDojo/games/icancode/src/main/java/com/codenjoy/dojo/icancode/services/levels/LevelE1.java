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

public class LevelE1 implements Level {
    
    @Override
    public String help() {
        return "On this Maze you can see a lot of laser machines (\"LASER_MACHINE\").<br>\n" +
                "Each machine is periodically fired lasers.<br><br>\n" +

                "When laser machine is ready to fire (\"LASER_MACHINE_READY\") it shoots after the second.\n" +
                "You can check the direction of laser by \"LASER_UP\", \"LASER_DOWN\",\n" +
                "\"LASER_LEFT\" or \"LASER_RIGHT\" element.<br><br>\n" +

                "There are 3 ways to cheat laser: move the box in front of laser,\n" +
                "jump over laser and jump in place:<br>\n" +
                "<pre>robot.jump();\n" +
                "robot.jumpLeft();\n" +
                "robot.pullLeft();</pre>\n" +

                "Don't forget the \"JUMP_PERK\" to jump and the \"MOVE_BOXES_PERK\" to push/pull boxes.<br><br>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return new LevelB1().defaultCode();
    }

    @Override
    public String winCode() {
        return new LevelD1().winCode(); // TODO реализовать
    }

    @Override
    public String map() {
        return  "                \n" +
                "  #####         \n" +
                "  #S.j#         \n" +
                "  #..B#######   \n" +
                "  #m..B˃...$#   \n" +
                "  ###....BBB#   \n" +
                "    #.B....$#   \n" +
                "    #...˄B..### \n" +
                "    #.###˃....# \n" +
                "    #.# #B.B.$# \n" +
                "    #.# #...### \n" +
                "    #.# #.$##   \n" +
                "    #E# ####    \n" +
                "    ###         \n" +
                "                \n" +
                "                \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "	' == ':{" +
                "		'synonyms':[' != ']," +
                "		'values':['\\'LASER_UP\\'', '\\'LASER_DOWN\\'', '\\'LASER_LEFT\\'', '\\'LASER_RIGHT\\'', '\\'LASER_MACHINE\\'', '\\'LASER_MACHINE_READY\\'']" +
                "	}" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelD1(),
                // TODO не указывается напраление движения лазера
                // TODO не указывается напраление движения лазермашины
                // TODO не указывается готовность лазермашины стрелять
                "value-laser-machine", "value-laser");
    }
}
