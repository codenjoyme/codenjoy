package com.codenjoy.dojo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: serhiy.zelenin
 * Date: 10/28/12
 * Time: 9:58 AM
 */
public class PlayerCommand {
    private static Logger logger = LoggerFactory.getLogger(PlayerCommand.class);

    private Joystick joystick;
    private String commandString;
    private Player player;

    public PlayerCommand(Joystick joystick, String commandString, Player player) {
        this.joystick = joystick;
        this.commandString = commandString.replaceAll(" ", "");
        this.player = player;
    }

    public void execute(){
        Pattern pattern = Pattern.compile("(left|right|up|down|(act(\\((\\d,?)+\\))?))", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(commandString);
        while (matcher.find()) {
            String command = matcher.group(0);
            if (command == null) {
                continue;
            }
            command = command.toLowerCase();

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("For player %s (%s) command is '%s'",
                        player.getName(), player.getCallbackUrl(), command));
            }

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
                    wrongCommand(commandString);
                }
            } catch (Exception e) {
                logger.error("Error durring process command + " + command, e);
            }
        }
    }

    private void wrongCommand(String responseContent) {
        logger.error(String.format("Player %s (%s) sent wrong command. Response is '%s'",
                player.getName(), player.getCallbackUrl(), responseContent));
    }
}
