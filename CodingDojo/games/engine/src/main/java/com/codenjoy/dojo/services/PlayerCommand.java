package com.codenjoy.dojo.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerCommand {

    private Joystick joystick;
    private String commandString;

    public PlayerCommand(Joystick joystick, String commandString) {
        this.joystick = joystick;
        this.commandString = commandString.replaceAll(" ", "");
    }

    public void execute(){
        Pattern pattern = Pattern.compile("(left|right|up|down|(act(\\((-?\\d*,?)+\\))?))", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(commandString);
        while (matcher.find()) {
            String command = matcher.group(0);
            if (command == null) {
                continue;
            }
            command = command.toLowerCase();

            try {
                if (command.equals("left")) {
                    joystick.left();
                } else if (command.equals("right")) {
                    joystick.right();
                } else if (command.equals("up")) {
                    joystick.up();
                } else if (command.equals("down")) {
                    joystick.down();
                } else if (command.startsWith("act")) {
                    String p = matcher.group(3);
                    if (p == null) {
                        joystick.act();
                    } else {
                        String[] split = p.split("[\\(,\\)]");
                        int[] parameters = new int[split.length - 1];
                        for (int index = 1; index < split.length; index++) {
                            parameters[index - 1] = Integer.valueOf(split[index]);
                        }
                        joystick.act(parameters);
                    }
                } else {
                    System.out.println(commandString);
                }
            } catch (Exception e) {
                System.out.println("Error durring process command + " + command);
                e.printStackTrace();
            }
        }
    }
}