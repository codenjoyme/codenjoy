package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

public class LevelC2 implements Level {
    
    @Override
    public String help() {
        return "Oops ! Looks like we didn’t predict this situation.<br>\n" +
                "Think how to adapt the code to these new conditions.<br>\n" +
                "Use IF construction to make your code more safely for Robot.<br><br>\n" +

                "You can use this method to detect Elements throughout the world:<br>\n" +
                "<pre>var scanner = robot.getScanner();\n" +
                "var point = new Point(4, 8);\n" +
                "if (scanner.at(point) == \"HOLE\") {\n" +
                "    // some statement here\n" +
                "}</pre>\n" +

                "Also you can use this method:<br>\n" +
                "<pre>var scanner = robot.getScanner();\n" +
                "var xOffset = 1;\n" +
                "var yOffset = -2;\n" +
                "if (scanner.atNearRobot(xOffset, yOffset) == \"HOLE\") {\n" +
                "    // some statement here\n" +
                "}</pre>\n" +
                "If Robot at {x:10, y:10}, this code will check {x:10 + 1, y:10 - 2} cell.<br><br>\n" +

                "Remember ! Your program should work for all previous levels too.";
    }

    @Override
    public String defaultCode() {
        return new LevelB1().defaultCode();
    }

    @Override
    public String winCode() {
        return new LevelC1().winCode();
    }

    @Override
    public String map() {
        return  "             \n" +
                "   #######   \n" +
                "   #S.O..#   \n" +
                "   ####..#   \n" +
                "      #..#   \n" +
                "   ####..### \n" +
                "   #$B.OO..# \n" +
                "   #.###...# \n" +
                "   #.# #...# \n" +
                "   #.###..E# \n" +
                "   #.......# \n" +
                "   ######### \n" +
                "             \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "	'scanner.':{" +
                "		'synonyms':['robot.getScanner().']," +
                "		'values':['atNearRobot()']" +
                "	}" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelC1());
    }
}
