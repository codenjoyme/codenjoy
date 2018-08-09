package com.codenjoy.dojo.web.controller;

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


import com.codenjoy.dojo.services.PlayerCommand;
import org.springframework.stereotype.Controller;

import java.util.regex.Pattern;

/**
 * Created by Oleksandr_Baglai on 2018-06-26.
 */
@Controller
public class Validator {

    public static final boolean CAN_BE_NULL = true;
    public static final boolean CANT_BE_NULL = !CAN_BE_NULL;

    public static final String EMAIL = "^[A-Za-z0-9+_.-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String GAME = "^[A-Za-z0-9+_.-]{1,50}$";
    public static final String CODE = "^[0-9]{1,50}$";
    public static final String MD5 = "^[A-Za-f0-9]{32}$";
    public static final String COMMAND = PlayerCommand.COMMAND;

    private final Pattern email;
    private final Pattern gameName;
    private final Pattern code;
    private final Pattern md5;
    private final Pattern command;

    public Validator() {
        email = Pattern.compile(EMAIL);
        gameName = Pattern.compile(GAME);
        code = Pattern.compile(CODE);
        md5 = Pattern.compile(MD5);
        command = Pattern.compile(COMMAND, Pattern.CASE_INSENSITIVE);
    }

    public void checkPlayerName(String input, boolean canBeNull) {
        if (!(input == null && canBeNull ||
                input != null && email.matcher(input).matches())) {
            throw new IllegalArgumentException("Player name is invalid: " + input);
        }
    }

    public void checkCode(String input, boolean canBeNull) {
        if (!(input == null && canBeNull ||
                input != null && code.matcher(input).matches())) {
            throw new IllegalArgumentException("Player code is invalid: " + input);
        }
    }

    public void checkGameName(String input, boolean canBeNull) {
        if (!(input == null && canBeNull ||
                input != null && gameName.matcher(input).matches()))
        {
            throw new IllegalArgumentException("Game name is invalid: " + input);
        }
    }

    public void checkMD5(String input) {
        if (input == null || !md5.matcher(input).matches()) {
            throw new IllegalArgumentException("Link hash is invalid: " + input);
        }
    }

    public void checkCommand(String input) {
        if (input == null || !command.matcher(input).matches()) {
            throw new IllegalArgumentException("Command is invalid: " + input);
        }
    }
}
