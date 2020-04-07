package com.codenjoy.dojo.icancode.services.levels;

public class Level9 implements Level {
    
    @Override
    public String help() {
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

}
