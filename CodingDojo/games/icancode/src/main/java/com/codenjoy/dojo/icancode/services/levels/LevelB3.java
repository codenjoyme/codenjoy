package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class LevelB3 implements Level {
    
    @Override
    public String help() {
        return "Try another one gold-maze...<br><br>\n" +

                "Coordinate {x:0, y:0} in the left-bottom corner of board.<br><br>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    var dest = scanner.getGold();\n" +
                "    var next = scanner.getShortestWay(dest[0])[1];\n" +
                "    var exit = scanner.getExit();\n" +
                "    var from = scanner.getMe();\n" +
                "    // TODO write your code here\n" +
                "}";
    }

    @Override
    public String winCode() {
        return new LevelB2().winCode();
    }

    @Override
    public String map() {
        return  "          \n" +
                " ######## \n" +
                " #S...$.# \n" +
                " #..###.# \n" +
                " #..# #.# \n" +
                " #.$###$# \n" +
                " #......# \n" +
                " #$.$..E# \n" +
                " ######## \n" +
                "          \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "	'scanner.':{" +
                "		'synonyms':['robot.getScanner().']," +
                "		'values':['getShortestWay()']" +
                "	}" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelB2(),
                "shortest-way", "check-stack",
                "print-stack");
    }
}
