package com.codenjoy.dojo.icancode.services.levels;

import java.util.Arrays;
import java.util.List;

public class Level1 implements Level {
    
    @Override
    public String help() {
        return "Robot asks for new orders every second. He should know where to go.<br>\n" +
                "Help him - write program and save him from the Maze. <br><br>\n" +

                "The code looks like this:<br>\n" +
                "<pre>function program(robot) {\n" +
                "    // TODO Uncomment one line that will help\n" +
                "    // robot.goDown();\n" +
                "    // robot.goUp();\n" +
                "    // robot.goLeft();\n" +
                "    // robot.goRight();\n" +
                "}</pre>\n" +
                "Send program to Robot by clicking the Commit button.<br>\n" +
                "If something is wrong - check Robot message in the Console (the rightmost field).<br><br>\n" +

                "You can always stop the program by clicking the Reset button.";
    }

    @Override
    public String defaultCode() {
        return "function program(robot) {\n" +
                "    // TODO Uncomment one line that will help\n" +
                "    // robot.goDown();\n" +
                "    // robot.goUp();\n" +
                "    // robot.goLeft();\n" +
                "    // robot.goRight();\n" +
                "}";
    }

    @Override
    public String winCode() {
        return "function program(robot) {\n" +
                "    robot.goRight();\n" +
                "}";
    }

    @Override
    public String map() {
        return  "        \n" +
                "        \n" +
                " ###### \n" +
                " #S..E# \n" +
                " ###### \n" +
                "        \n" +
                "        \n" +
                "        \n";
    }

    @Override
    public String autocomplete() {
        return "{" +
                "    \"robot.\":{" +
                "        \"synonyms\":[]," +
                "        \"values\":[\"goDown()\", \"goUp()\", \"goLeft()\", \"goRight()\"]" +
                "    }" +
                "}";
    }

    @Override
    public List<String> befungeCommands() {
        return Arrays.asList("start", "finish", "robot-right");
    }
}
