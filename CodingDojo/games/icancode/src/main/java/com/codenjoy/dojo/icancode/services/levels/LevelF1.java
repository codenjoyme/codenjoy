package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class LevelF1 implements Level {
    
    @Override
    public String help() {
        return "There is a lot of gold on this maze. But it seems we are not alone here.<br>\n" +
                "Hurry, the Zombie are not as fast as you.<br><br>\n" +

                "Scanner will help Robot to detect Zombie also. If you want to find Zombie on map - try use \"ZOMBIE\" element.<br>\n" +
                "<pre>var scanner = robot.getScanner();\n" +
                "if (scanner.at(\"LEFT\") == \"ZOMBIE\") {\n" +
                "    // some statement here\n" +
                "}</pre>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return new LevelB1().defaultCode();
    }

    @Override
    public String winCode() {
        return new LevelE1().winCode(); // TODO реализовать
    }

    @Override
    public String map() {
        return  "                \n" +
                "  ##### ####### \n" +
                "  #S..# #....Z# \n" +
                "  #...###...### \n" +
                "  #...$...$.#   \n" +
                "  ###.......#   \n" +
                "    #..$....#   \n" +
                "    #.....$.### \n" +
                "    #.###....E# \n" +
                "    #$# #.$...# \n" +
                "    #.###...### \n" +
                "    #...$..##   \n" +
                "    #.######    \n" +
                "    ###         \n" +
                "                \n" +
                "                \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "	' == ':{" +
                "		'synonyms':[' != ']," +
                "		'values':['\\'ZOMBIE\\'']" +
                "	}" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelE1(),
                "value-zombie");
    }
}
