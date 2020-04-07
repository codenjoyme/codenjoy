package com.codenjoy.dojo.icancode.services.levels;

public class LevelE1 implements Level {
    
    @Override
    public String help() {
        return "It looks like this labyrinth is much bigger than it seems.<br>\n" +
                "But look how much gold there is ! See if you can collect all this gold.<br><br>\n" +

                "Remember that boxes can be moved:\n" +
                "<pre>robot.pull(\"UP\");\n" +
                "// same as\n" +
                "robot.pullUp();</pre>\n" +

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
        return "                " +
                "  #####         " +
                "  #S..#         " +
                "  #..B#######   " +
                "  #B..B˃...$#   " +
                "  ###....BBB#   " +
                "    #.B....$#   " +
                "    #...˄B..### " +
                "    #.###˃....# " +
                "    #.# #B.B.$# " +
                "    #.# #...### " +
                "    #.# #.$##   " +
                "    #E# ####    " +
                "    ###         " +
                "                " +
                "                ";
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
}
