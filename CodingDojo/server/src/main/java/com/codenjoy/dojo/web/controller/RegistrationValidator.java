package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.room.GamesRooms;
import com.codenjoy.dojo.services.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;

@Component
@RequiredArgsConstructor
public class RegistrationValidator implements Validator {

    @Value("${registration.nickname.allowed}")
    private boolean nicknameAllowed;

    @Value("${registration.password.min-length}")
    private int minPasswordLen;

    private final com.codenjoy.dojo.web.controller.Validator validator;
    private final Registration registration;
    private final PlayerService playerService;
    private final RoomService roomService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Player.class)
                // TODO разобраться откуда тут это возникло
                //      стало слетать после добавления
                //       model.addAttribute("gamesRooms", roomService.gamesRooms());
                || clazz.equals(GamesRooms.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target == null) {
            return;
        }

        Player player = (Player) target;

        // TODO #4FS тут проверить
        String room = player.getRoom();
        if (!validator.isRoomName(room, CANT_BE_NULL)) {
            errors.rejectValue("room", "registration.room.invalid", new Object[]{ room }, null);
        }

        String game = roomService.game(room);
        if (!validator.isGameName(game, CANT_BE_NULL)) {
            errors.rejectValue("game", "registration.game.invalid", new Object[]{ game }, null);
        }

        if (!playerService.isRegistrationOpened(room)) {
            if (playerService.isRegistrationOpened()) {
                errors.rejectValue("email", "registration.room.closed");
            } else {
                errors.rejectValue("email", "registration.closed");
            }
        }

        String name = player.getReadableName();

        if (!validateNicknameStructure(name)) {
            errors.rejectValue("readableName", "registration.nickname.invalid", new Object[]{ name }, null);
        }

        if (!checkNameUniqueness(name)) {
            errors.rejectValue("readableName", "registration.nickname.alreadyUsed", new Object[]{ name }, null);
        }

        String email = player.getEmail();
        if (!validateEmailStructure(email)) {
            errors.rejectValue("email", "registration.email.invalid", new Object[]{ email }, null);
        }

        if (!checkEmailUniqueness(email)) {
            errors.rejectValue("email", "registration.email.alreadyUsed");
        }

        String password = player.getPassword();

        if (!checkPasswordNotEmpty(password)) {
            errors.rejectValue("password", "registration.password.empty");
        }

        if (!checkPasswordLength(password)) {
            errors.rejectValue("password", "registration.password.length", new Object[] { minPasswordLen }, null);
        }

        if (!checkPasswordConfirmation(password, player.getPasswordConfirmation())) {
            errors.rejectValue("passwordConfirmation", "registration.password.invalidConfirmation");
        }
    }

    private boolean checkPasswordConfirmation(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

    private boolean checkPasswordLength(String password) {
        return password.length() >= minPasswordLen;
    }

    private boolean checkEmailUniqueness(String email) {
        boolean emailIsUsed = registration.emailIsUsed(email);
        String idByEmail = registration.getIdByEmail(email);
        return StringUtils.isEmpty(idByEmail) && !emailIsUsed;
    }

    private boolean checkPasswordNotEmpty(String password) {
        return StringUtils.hasText(password);
    }


    private boolean checkNameUniqueness(String name) {
        return !registration.nameIsUsed(name);
    }

    private boolean validateEmailStructure(String email) {
        return validator.isEmail(email, CANT_BE_NULL);
    }

    private boolean validateNicknameStructure(String name) {
        if (nicknameAllowed) {
            return validator.isNickName(name);
        } else {
            return validator.isReadableName(name);
        }
    }
}
