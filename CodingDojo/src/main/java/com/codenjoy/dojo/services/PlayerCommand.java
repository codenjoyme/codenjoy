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
        this.commandString = commandString;
        this.player = player;
    }

    public void execute(){
        Pattern pattern = Pattern.compile("([^,]+)", Pattern.CASE_INSENSITIVE);
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

            if (command.contains("left")) {
                joystick.left();
            } else if (command.contains("right")) {
                joystick.right();
            } else if (command.contains("up")) {
                joystick.up();
            } else if (command.contains("down")) {
                joystick.down();
            } else if (command.contains("act")) {
                joystick.act();
            } else {
                wrongCommand(commandString);
            }
        }
    }

    private void wrongCommand(String responseContent) {
        logger.error(String.format("Player %s (%s) sent wrong command. Response is '%s'",
                player.getName(), player.getCallbackUrl(), responseContent));
    }
}
