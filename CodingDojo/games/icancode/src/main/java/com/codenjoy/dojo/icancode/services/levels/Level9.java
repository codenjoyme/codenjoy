package com.codenjoy.dojo.icancode.services.levels;

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
