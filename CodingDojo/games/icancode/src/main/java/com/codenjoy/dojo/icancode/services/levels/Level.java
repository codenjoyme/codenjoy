package com.codenjoy.dojo.icancode.services.levels;

import java.util.Arrays;
import java.util.List;

public interface Level {

    static List<String> extendBefunge(Level level, String... commands) {
        List<String> result = level.befungeCommands();
        result.addAll(Arrays.asList(commands));
        return result;
    }

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

    default List<String> befungeCommands() {
        return Arrays.asList();
    }
}
