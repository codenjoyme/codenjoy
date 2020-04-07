package com.codenjoy.dojo.icancode.services.levels;

public class Level6 implements Level {

    @Override
    public String winCode() {
        return new Level5().winCode();
    }

    @Override
    public String map() {
        return "        " +
                "    ### " +
                "    #S# " +
                "    #.# " +
                " ####.# " +
                " #E...# " +
                " ###### " +
                "        ";
    }

}
