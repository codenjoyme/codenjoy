package net.tetris.services;

import net.tetris.dom.TetrisJoystik;
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
    private static Pattern pattern = Pattern.compile("((left)=(-?\\d*))|((right)=(-?\\d*))|((rotate)=(-?\\d*))|(drop)", Pattern.CASE_INSENSITIVE);
    private static Logger logger = LoggerFactory.getLogger(PlayerCommand.class);

    private TetrisJoystik joystick;
    private String commandString;
    private Player player;

    public PlayerCommand(TetrisJoystik joystick, String commandString, Player player) {
        this.joystick = joystick;
        this.commandString = commandString;
        this.player = player;
    }

    public void execute() {
        Matcher matcher = pattern.matcher(commandString);
        while (matcher.find()) {
            int groupsCount = matcher.groupCount();
            for (int i = 0; i <= groupsCount; i++) {
                String group = matcher.group(i);
                if (null == group) {
                    continue;
                }
                if (recognizeCommand(matcher, i, group)) {
                    break;
                }
            }
        }
    }


    private boolean recognizeCommand(Matcher matcher, int i, String group) {
        try {
            switch (group.toLowerCase()) {
                case "left":
                    joystick.left(Integer.parseInt(matcher.group(i + 1)));
                    return true;
                case "right":
                    joystick.right(Integer.parseInt(matcher.group(i + 1)));
                    return true;
                case "rotate":
                    joystick.act(Integer.parseInt(matcher.group(i + 1)));
                    return true;
                case "drop":
                    joystick.down();
                    return true;
            }
        } catch (NumberFormatException e) {
            logger.warn("Player " + player.getName() + " sent wrong command. Player URL: " +
                    player.getCallbackUrl() + " response: " + commandString, e);
        }
        return false;
    }


}
