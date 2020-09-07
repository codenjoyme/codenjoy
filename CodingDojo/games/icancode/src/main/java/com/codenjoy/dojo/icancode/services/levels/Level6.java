package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class Level6 implements Level {

    @Override
    public String winCode() {
        return new Level5().winCode();
    }

    @Override
    public String map() {
        return  "        \n" +
                "    ### \n" +
                "    #S# \n" +
                "    #.# \n" +
                " ####.# \n" +
                " #E...# \n" +
                " ###### \n" +
                "        \n";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new Level5(),
                "value-left");
    }

}
