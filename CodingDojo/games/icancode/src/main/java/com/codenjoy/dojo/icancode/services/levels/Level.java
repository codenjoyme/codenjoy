package com.codenjoy.dojo.icancode.services.levels;

public interface Level {

    default String help() {
        return "You should check all cases.<br><br>" +
                "Remember ! Your program should work for all previous levels too.";
    }

    default String defaultCode() {
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    // TODO write your code here\n" +
                "}";
    }

    String winCode();

    default String refactoringCode() {
        return winCode();
    }

    String map();

    default String autocomplete() {
        return "{}";
    }
}
