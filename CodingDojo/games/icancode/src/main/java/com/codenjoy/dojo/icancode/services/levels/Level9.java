package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class Level9 implements Level {
    
    @Override
    public String help() {
        // TODO другое сообщение дать
        return "This is final LevelA Maze. Good luck !<br><br>\n" +
                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String winCode() {
        return new Level8().winCode();
    }

    @Override
    public String map() {
        return "              " +
                "              " +
                " ############ " +
                " #..........# " +
                " #.########.# " +
                " #.#      #.# " +
                " #.# #### #.# " +
                " #.# #.S# #.# " +
                " #.# #.## #.# " +
                " #.# #.#  #.# " +
                " #.# #.####.# " +
                " #E# #......# " +
                " ### ######## " +
                "              ";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new Level8(),
                "value-null", "robot-came-from",
                "robot-go", "robot-previous-direction",
                "mirror-top-bottom", "mirror-bottom-top");
    }

}
