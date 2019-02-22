package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import org.slf4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerCommand {

    private static Logger logger = DLoggerFactory.getLogger(PlayerCommand.class);

    public static final int MAX_COMMAND_LENGTH = 100;
    private static final String COMMAND = "(left|right|up|down|(act(\\((-?\\d*,?)+\\))?)|(message(\\('(.*)'\\))?))";
    private static Pattern PATTERN = Pattern.compile(COMMAND, Pattern.CASE_INSENSITIVE);

    private Joystick joystick;
    private String command;

    public PlayerCommand(Joystick joystick, String command) {
        this.joystick = joystick;
        if (!command.startsWith("message('")) {
            this.command = command.replaceAll(", +", ",").replaceAll(" +,", ",");
        } else {
            this.command = command.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r");
        }
    }

    public static boolean isValid(String command) {
        return command != null
                && command.length() < MAX_COMMAND_LENGTH
                && PATTERN.matcher(command).find();
    }

    public void execute(){
        Matcher matcher = PATTERN.matcher(command);
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
                        int[] parameters = new int[0];
                        if (split.length != 0) {
                            parameters = new int[split.length - 1];
                            for (int index = 1; index < split.length; index++) {
                                parameters[index - 1] = Integer.parseInt(split[index]);
                            }
                        }
                        joystick.act(parameters);
                    }
                } else if (command.startsWith("message")) {
                    String p = matcher.group(7);
                    if (p == null) {
                        joystick.message("");
                    } else {
                        joystick.message(p.replaceAll("\\\\n", "\n").replaceAll("\\\\r", "\r"));
                    }
                } else {
                    logger.warn(String.format("Unexpected command part '%s' in command '%s'", command, this.command));
                }
            } catch (Exception e) {
                logger.error(String.format("Error during process command '%s'", this.command), e);
            }
        }
    }
}
