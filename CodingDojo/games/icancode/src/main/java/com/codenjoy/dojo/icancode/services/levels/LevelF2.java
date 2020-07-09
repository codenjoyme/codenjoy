package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class LevelF2 implements Level {
    
    @Override
    public String help() {
        return "Zombie in your way. You will meet !<br><br>\n" +

                "By the way, we did not tell you, but through Zombie you can also jump over:\n" +
                "<pre>robot.jump();\n" +
                "robot.jumpLeft();\n" +
                "robot.jump(\"RIGHT\");</pre>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return new LevelB1().defaultCode();
    }

    @Override
    public String winCode() {
        return new LevelF1().winCode(); // TODO реализовать
    }

    @Override
    public String map() {
        return  "              \n" +
                "              \n" +
                " ############ \n" +
                " #S.........# \n" +
                " ##########.# \n" +
                "          #.# \n" +
                " ##########.# \n" +
                " #....Z.....# \n" +
                " #.########## \n" +
                " #.#          \n" +
                " #.########## \n" +
                " #.........E# \n" +
                " ############ \n" +
                "              \n";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelF1());
    }

}
