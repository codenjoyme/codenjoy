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
import com.codenjoy.dojo.services.VerificationType;
import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.properties.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static com.codenjoy.dojo.services.entity.Player.APPROVED;
import static com.codenjoy.dojo.services.entity.Player.NOT_APPROVED;

/**
 * Created by Oleksandr_Baglai on 2018-06-26.
 */
@Controller
public class BalancerValidator {

    public static final String PHONE_PLUS_PREFIX = "+";
    public static final String PHONE_COUNTRY_CODE_PREFIX = "38";
    public static final String PHONE_FULL_COUNTRY_CODE_PREFIX = PHONE_PLUS_PREFIX + PHONE_COUNTRY_CODE_PREFIX;

    public static final boolean CAN_BE_NULL = true;
    public static final boolean CANT_BE_NULL = !CAN_BE_NULL;

    public static final String EMAIL = "^[A-Za-z0-9+_.-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String GAME = "^[A-Za-z0-9+_.-]{1,50}$";
    public static final String USER_NAME_CHARS = "[a-zA-Zа-яА-Я'іІєЄґҐїЇ]";
    public static final String USER_NAME_LENGTH = "50";
    public static final String USER_NAME = "^" + USER_NAME_CHARS + "{1," + USER_NAME_LENGTH + "}$";
    public static final String CODE = "^[0-9]{1,50}$";
    public static final String MD5 = "^[A-Fa-f0-9]{32}$";
    public static final String ID = "^[a-z0-9]{" + Hash.ID_LENGTH + "}$";
    public static final String DAY = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";
    public static final String PHONE_NUMBER = "^(\\+38|38)?0([0-9]{2})([0-9]{7})$";

    @Autowired protected Players players;
    @Autowired protected ConfigProperties properties;
    @Autowired protected Messages messages;
    @Autowired protected PasswordEncoder passwordEncoder;

    private final Pattern email;
    private final Pattern id;
    private final Pattern gameName;
    private final Pattern userName;
    private final Pattern code;
    private final Pattern md5;
    private final Pattern day;
    private final Pattern phoneNumber;

    public BalancerValidator() {
        email = Pattern.compile(EMAIL);
        id = Pattern.compile(ID);
        gameName = Pattern.compile(GAME);
        userName = Pattern.compile(USER_NAME);
        code = Pattern.compile(CODE);
        md5 = Pattern.compile(MD5);
        day = Pattern.compile(DAY);
        phoneNumber = Pattern.compile(PHONE_NUMBER);
    }

    public void checkId(String input, boolean canBeNull) {
        boolean empty = StringUtils.isEmpty(input);
        if (!(empty && canBeNull ||
                !empty && id.matcher(input).matches()))
        {
            throwError(messages.getInvalidPlayerId(), input);
        }
    }

    private void throwError(String message, String... args) {
        throw error(message, args);
    }

    private IllegalArgumentException error(String message, String[] args) {
        return new IllegalArgumentException(String.format(message, args));
    }

    public void checkEmail(String input, boolean canBeNull) {
        boolean empty = StringUtils.isEmpty(input);
        if (!(empty && canBeNull ||
                !empty && email.matcher(input).matches()))
        {
            throwError(messages.getInvalidPlayerEmail(), input);
        }
    }

    public void checkCode(String input, boolean canBeNull) {
        boolean empty = StringUtils.isEmpty(input);
        if (!(empty && canBeNull ||
                !empty && code.matcher(input).matches()))
        {
            throwError(messages.getInvalidPlayerCode(), input);
        }
    }

    public void checkGameName(String input, boolean canBeNull) {
        boolean empty = StringUtils.isEmpty(input);
        if (!(empty && canBeNull ||
                !empty && gameName.matcher(input).matches()))
        {
            throwError(messages.getInvalidGameName(), input);
        }
    }

    public void checkMD5(String input, boolean canBeNull) {
        boolean empty = StringUtils.isEmpty(input);
        if (!(empty && canBeNull ||
                !empty && md5.matcher(input).matches()))
        {
            throwError(messages.getInvalidPassword(), input);
        }
    }

    public void checkDay(String input) {
        if (StringUtils.isEmpty(input) ||
                !day.matcher(input).matches())
        {
            throwError(messages.getInvalidDay(), input);
        }
    }

    public void checkName(String name, String input) {
        checkString(name, input);

        if (!userName.matcher(input).matches()) {
            throwError(messages.getInvalidStringFormat(),
                    name, USER_NAME_CHARS, USER_NAME_LENGTH, input);
        }
    }

    public void checkString(String name, String input) {
        if (StringUtils.isEmpty(input)) {
            throwError(messages.getInvalidString(), name, input);
        }
    }

    public void checkNull(String name, String input) {
        if (!StringUtils.isEmpty(input)) {
            throwError(messages.getShouldBeEmpty(), name, input);
        }
    }

    public Player checkPlayerCode(String email, String code) {
        checkEmail(email, BalancerValidator.CANT_BE_NULL);
        checkCode(code, BalancerValidator.CANT_BE_NULL);

        Player player = players.getByEmail(email);
        if (player == null || !code.equals(player.getCode())) {
            throwError(messages.getInvalidCodeLoginData(), code);
        }
        return player;
    }

    public String checkPhoneNumber(String phone) {
        String normalized = phoneNormalize(phone);

        if (!phoneNumber.matcher(normalized).matches()) {
            throwError(messages.getInvalidPhoneNumber(), phone);
        }

        return normalized;
    }

    public Player checkPlayerByEmailAndPhone(String email, String phone) {
        Player player = checkPlayerByEmail(email, false);
        String normalized = checkPhoneNumber(phone);

        if (!player.getPhone().equals(normalized)) {
            throwError(messages.getInvalidPhoneLoginData());
        }
        return player;
    }

    public void all(Runnable... validators) {
        List<String> messages = new LinkedList<>();
        Arrays.stream(validators)
                .forEach(v -> {
                    try {
                        v.run();
                    } catch (IllegalArgumentException e) {
                        messages.add(e.getMessage());
                    }
                });
        if (messages.isEmpty()) {
            return;
        }

        if (messages.size() == 1) {
            throw new IllegalArgumentException(messages.iterator().next());
        } else {
            throwError(this.messages.getInvalidParameters(), messages.toString());
        }
    }

    public String phoneNormalize(String phone) {  // TODO работает только для UA
        if (phone.startsWith(PHONE_COUNTRY_CODE_PREFIX)) {
            return PHONE_PLUS_PREFIX + phone;
        } else if(phone.startsWith("0")) {
            return PHONE_FULL_COUNTRY_CODE_PREFIX + phone;
        } else {
            return phone;
        }
    }

    public void checkEmailOrId(String email, boolean canBeNull) {
        try {
            checkEmail(email, canBeNull);
        } catch (IllegalArgumentException e) {
            checkId(email, canBeNull);
        }
    }

    public void checkNotRegisteredEmail(String email) {
        if (players.getCode(email) != null) {
            throwError(messages.getAlreadyRegisteredEmail(), email);
        }
    }

    public void checkNotRegisteredPhone(String phone) {
        if (players.getByPhone(phone) != null) {
            throwError(messages.getAlreadyRegisteredPhone(), phone);
        }
    }

    public Player checkPlayerLogin(String email, String code, String password, boolean ignorePass) {
        checkCode(code, CAN_BE_NULL);

        Player exist = checkPlayerByEmail(email, true);

        if (exist.getApproved() == Player.NOT_APPROVED) {
            throw new LoginException(messages.getNotApproved());
        }

        if (!isAuthorized(exist, password, code, ignorePass)) {
            throw new LoginException(messages.getInvalidEmailLoginData());
        }

        return exist;
    }

    public Player checkPlayerByEmail(String email, boolean isWithPassword) {
        checkEmail(email, CANT_BE_NULL);

        Player exist = players.getByEmail(email);

        if (exist == null) {
            String message = isWithPassword
                    ? messages.getInvalidEmailLoginData()
                    : String.format(messages.getNotFoundEmail(), email);
            throw new LoginException(message);
        }
        return exist;
    }

    private boolean isAuthorized(Player exist, String password, String code, boolean ignorePass) {
        if (ignorePass) { // TODO test me
            checkNull("password", password);
        } else {
            checkString("password", password);

            if (passwordEncoder.matches(password, exist.getPassword())) {
                return true;
            }
        }

        // используется для update юзера по code если надо обновить пароль
        return exist.getCode().equals(code);
    }

    public void checkApproved(Player player) {
        if (player.getApproved() == NOT_APPROVED) {
            throwError(messages.getNotApproved());
        }
    }

    public void checkNotApproved(Player player) {
        if (player.getApproved() == APPROVED) {
            throwError(messages.getAlreadyApproved());
        }
    }

    public void checkVerificationCode(Player player, VerificationType type, String code) {
        if (StringUtils.isEmpty(code)
                || type == null
                || !code.equals(player.getVerificationCode())
                || !type.name().equals(player.getVerificationType()))
        {
            throwError(messages.getInvalidVerificationCode(), code);
        }
    }

    public Player checkPlayerByPhone(String phone) {
        String normalized = checkPhoneNumber(phone);

        Player player = players.getByPhone(normalized);
        if (player == null) {
            throwError(messages.getNotFoundPhone(), normalized);
        }

        return player;
    }

}
