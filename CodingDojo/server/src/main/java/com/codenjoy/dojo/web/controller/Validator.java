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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

import static com.codenjoy.dojo.transport.auth.SecureAuthenticationService.MAX_PLAYER_CODE_LENGTH;
import static com.codenjoy.dojo.transport.auth.SecureAuthenticationService.MAX_PLAYER_ID_LENGTH;

/**
 * Created by Oleksandr_Baglai on 2018-06-26.
 */
@Controller
public class Validator {

    public static final boolean CAN_BE_NULL = true;
    public static final boolean CANT_BE_NULL = !CAN_BE_NULL;

    private static final String EMAIL = "^(?:[A-Za-z0-9+_.-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6})$";
    private static final String ID = "^[A-Za-z0-9-]{1," + MAX_PLAYER_ID_LENGTH + "}$";
    private static final String GAME_NAME = "^[A-Za-z][A-Za-z0-9+_.-]{0,48}[A-Za-z0-9]$";
    private static final String ROOM_NAME = GAME_NAME;
    private static final String CODE = "^[0-9]{1," + MAX_PLAYER_CODE_LENGTH + "}$";
    private static final String MD5 = "^[A-Za-f0-9]{32}$";
    private static final String READABLE_NAME_LAT = "^[A-Za-z]{1,50}$";
    private static final String READABLE_NAME_CYR = "^[А-Яа-яЁёҐґІіІіЄє]{1,50}$";
    private static final String NICK_NAME = "^[0-9A-Za-zА-Яа-яЁёҐґІіІіЄє ]{1,50}$";

    @Autowired protected Registration registration;
    @Autowired protected ConfigProperties properties;
    @Autowired protected GameService gameService;
    @Autowired protected PlayerService playerService;

    private Pattern email;
    private Pattern id;
    private Pattern readableNameLat;
    private Pattern readableNameCyr;
    private Pattern nickName;
    private Pattern gameName;
    private Pattern roomName;
    private Pattern code;
    private Pattern md5;

    public Validator() {
        email = Pattern.compile(EMAIL);
        id = Pattern.compile(ID);
        readableNameLat = Pattern.compile(READABLE_NAME_LAT);
        readableNameCyr = Pattern.compile(READABLE_NAME_CYR);
        nickName = Pattern.compile(NICK_NAME);
        gameName = Pattern.compile(GAME_NAME);
        roomName = Pattern.compile(ROOM_NAME);
        code = Pattern.compile(CODE);
        md5 = Pattern.compile(MD5);
    }

    public void checkPlayerId(String input) {
        boolean empty = isEmpty(input);
        if (empty || !id.matcher(input).matches()) {
            throw new IllegalArgumentException(String.format("Player id is invalid: '%s'", input));
        }
    }

    public boolean isReadableName(String input) {
        boolean empty = isEmpty(input);
        if (empty || !isFullName(input)) {
            return false;
        }
        return true;
    }

    public boolean isNickName(String input) {
        boolean empty = isEmpty(input);
        if (empty || !nickName.matcher(input).matches()) {
            return false;
        }

        return true;
    }

    public boolean isFullName(String input) {
        String[] parts = input.split(" ");
        if (parts == null || parts.length != 2) {
            return false;
        }
        String firstName = parts[0];
        String lastName = parts[1];
        if (readableNameLat.matcher(firstName).matches()
                && readableNameLat.matcher(lastName).matches())
        {
            return true;
        }

        if (readableNameCyr.matcher(firstName).matches()
                && readableNameCyr.matcher(lastName).matches())
        {
            return true;
        }

        return false;
    }

    public void checkPlayerId(String input, boolean canBeNull) {
        if (!isPlayerId(input, canBeNull)) {
            throw new IllegalArgumentException(String.format("Player id is invalid: '%s'", input));
        }
    }

    public boolean isPlayerId(String input, boolean canBeNull) {
        return is(input, canBeNull, id);
    }

    public boolean isEmail(String input, boolean canBeNull) {
        return is(input, canBeNull, email);
    }

    public void checkEmail(String input, boolean canBeNull) {
        if (!isEmail(input, canBeNull)) {
            throw new IllegalArgumentException(String.format("Player email is invalid: '%s'", input));
        }
    }

    public boolean isCode(String input, boolean canBeNull) {
        return is(input, canBeNull, code);
    }

    public void checkCode(String input, boolean canBeNull) {
        if (!isCode(input, canBeNull)) {
            throw new IllegalArgumentException(String.format("Player code is invalid: '%s'", input));
        }
    }

    public boolean isEmpty(String input) {
        return StringUtils.isEmpty(input) || input.equalsIgnoreCase("null");
    }

    public boolean isGameName(String input, boolean canBeNull) {
        return is(input, canBeNull, gameName);
    }

    public boolean isRoomName(String input, boolean canBeNull) {
        return is(input, canBeNull, roomName);
    }

    public boolean is(String input, boolean canBeNull, Pattern pattern) {
        boolean empty = isEmpty(input);
        if (!(empty && canBeNull || !empty && pattern.matcher(input).matches())) {
            return false;
        }
        return true;
    }

    public void checkRoomName(String input, boolean canBeNull) {
        if (!isRoomName(input, canBeNull)) {
            throw new IllegalArgumentException(String.format("Room name is invalid: '%s'", input));
        }
    }

    public void checkGameName(String input, boolean canBeNull) {
        if (!isGameName(input, canBeNull)) {
            throw new IllegalArgumentException(String.format("Game name is invalid: '%s'", input));
        }
    }

    public void checkNotEmpty(String name, String input) {
        if (isEmpty(input)) {
            throw new IllegalArgumentException(String.format("Parameter %s is empty: '%s'", name, input));
        }
    }

    // TODO возможно хорошая идея использовать его всегда вместо checkGameName везде по коду
    public GameType checkGameType(String input) {
        checkGameName(input, Validator.CANT_BE_NULL);

        GameType type = gameService.getGame(input);
        if (type == NullGameType.INSTANCE) {
            throw new IllegalArgumentException("Game not found: " + input);
        }
        return type;
    }

    public void checkMD5(String input) {
        if (input == null || !md5.matcher(input).matches()) {
            throw new IllegalArgumentException(String.format("Hash is invalid: '%s'", input));
        }
    }

    public void checkCommand(String input) {
        if (!PlayerCommand.isValid(input)) {
            throw new IllegalArgumentException(String.format("Command is invalid: '%s'", input));
        }
    }

    public void checkPlayerCode(String id, String code) {
        checkPlayerId(id, CANT_BE_NULL);
        checkCode(code, CANT_BE_NULL);
        if (registration.checkUser(id, code) == null) {
            throw new IllegalArgumentException(String.format("Player code is invalid: '%s' for player: '%s'", code, id));
        }
    }

    public void checkNotNull(String name, Object input) {
        if (input == null) {
            throw new IllegalArgumentException(String.format("Object '%s' null", name));
        }
    }

    public void checkPlayerInRoom(String id, String roomName) {
        checkRoomName(roomName, CANT_BE_NULL);
        checkPlayerId(id, CANT_BE_NULL);

        if (!playerService.get(id).getRoomName().equals(roomName)) {
            throw new IllegalArgumentException(String.format("Player '%s' is not in room '%s'", id, roomName));
        }
    }
}
