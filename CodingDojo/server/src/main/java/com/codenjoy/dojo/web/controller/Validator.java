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


import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.PlayerCommand;
import com.codenjoy.dojo.services.dao.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by Oleksandr_Baglai on 2018-06-26.
 */
@Controller
public class Validator {

    public static final boolean CAN_BE_NULL = true;
    public static final boolean CANT_BE_NULL = !CAN_BE_NULL;

    public static final String EMAIL_OR_ID = "^(?:[A-Za-z0-9+_.-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6})|(?:[A-Za-z0-9]+)$";
    public static final String EMAIL = "^[A-Za-z0-9+_.-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String ID = "^[A-Za-z0-9]+$";
    public static final String GAME = "^[A-Za-z0-9+_.-]{1,50}$";
    public static final String CODE = "^[0-9]{1,50}$";
    public static final String MD5 = "^[A-Za-f0-9]{32}$";

    @Autowired private Registration registration;
    @Autowired private ConfigProperties properties;

    private final Pattern email;
    private final Pattern id;
    private final Pattern gameName;
    private final Pattern code;
    private final Pattern md5;

    public Validator() {
        email = Pattern.compile(EMAIL);
        id = Pattern.compile(ID);
        gameName = Pattern.compile(GAME);
        code = Pattern.compile(CODE);
        md5 = Pattern.compile(MD5);
    }

    public void checkPlayerId(String input) {
        if (isEmpty(input)) {
            throw new IllegalArgumentException("Player id is invalid: " + input);
        }
    }

    public void checkPlayerName(String input, boolean canBeNull) {
        boolean empty = isEmpty(input);
        if (!(empty && canBeNull ||
                !empty && (email.matcher(input).matches() || id.matcher(input).matches())))
        {
            throw new IllegalArgumentException("Player name/id is invalid: " + input);
        }
    }

    public void checkCode(String input, boolean canBeNull) {
        boolean empty = isEmpty(input);
        if (!(empty && canBeNull ||
                !empty && code.matcher(input).matches()))
        {
            throw new IllegalArgumentException("Player code is invalid: " + input);
        }
    }

    private boolean isEmpty(String input) {
        return StringUtils.isEmpty(input) || input.equalsIgnoreCase("null");
    }

    public void checkGameName(String input, boolean canBeNull) {
        boolean empty = isEmpty(input);
        if (!(empty && canBeNull ||
                !empty && gameName.matcher(input).matches()))
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
        if (!PlayerCommand.isValid(input)) {
            throw new IllegalArgumentException("Command is invalid: " + input);
        }
    }

    public void checkPlayerCode(String playerName, String code) {
        checkPlayerName(playerName, CANT_BE_NULL);
        checkCode(code, CANT_BE_NULL);
        if (registration.checkUser(playerName, code) == null) {
            throw new IllegalArgumentException("Player code is invalid: " + code + " for player: " + playerName);
        }
    }

    public void checkIsAdmin(String adminPassword) {
        if (!DigestUtils.md5DigestAsHex(properties.getAdminPassword().getBytes()).equals(adminPassword)){
            throw new RuntimeException("Unauthorized admin access");
        }
    }
}
