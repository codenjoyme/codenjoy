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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;

@Component
@RequiredArgsConstructor
public class RegistrationValidator implements Validator {

    @Value("${registration.nickname.allowed}")
    private boolean nicknameAllowed;

    @Value("${registration.password.min-length}")
    private int minPasswordLen;

    private final com.codenjoy.dojo.web.controller.Validator validator;
    private final RoomsAliaser rooms;
    private final Registration registration;
    private final PlayerService playerService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Player.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target == null) {
            return;
        }

        if (!playerService.isRegistrationOpened()) {
            errors.rejectValue("readableName", "registration.closed");
        }

        Player player = (Player) target;

        String fullName = player.getFullName();
        String name = player.getReadableName();

        if (!validateNicknameStructure(fullName)) {
            errors.rejectValue("fullName", "registration.nickname.invalid", new Object[]{name}, null);
        }

        if (!checkNameUniqueness(name)) {
            errors.rejectValue("readableName", "registration.nickname.alreadyUsed", new Object[]{name}, null);
        }

        String email = player.getEmail();
        if (!validateEmailStructure(email)) {
            errors.rejectValue("email", "registration.email.invalid", new Object[]{email}, null);
        }

        if (!checkEmailUniqueness(email)) {
            errors.rejectValue("email", "registration.email.alreadyUsed");
        }

        String password = player.getPassword();

        if (!checkPasswordNotEmpty(password)) {
            errors.rejectValue("password", "registration.password.empty");
        }

        if (!checkPasswordLength(password)) {
            errors.rejectValue("password", "registration.password.length", new Object[]{minPasswordLen}, null);
        }

        if (!checkPasswordConfirmation(password, player.getPasswordConfirmation())) {
            errors.rejectValue("passwordConfirmation", "registration.password.invalidConfirmation");
        }

        String github = player.getGitHubUsername();
        if (!checkGitHubUniqueness(github)) {
            errors.rejectValue("gitHubUsername", "registration.gitHubUsername.alreadyUsed");
        }

        if (!checkValidGithub(github)) {
            errors.rejectValue("gitHubUsername", "registration.gitHubUsername.invalidGit");
        }

        String slackEmail = player.getSlackEmail();
        if (!checkSlackEmailUniqueness(slackEmail)) {
            errors.rejectValue("slackEmail", "registration.slackEmail.alreadyUsed");
        }
        if (!validateEmailStructure(slackEmail)) {
            errors.rejectValue("email", "registration.email.invalid", new Object[]{email}, null);
        }

        String game = rooms.getGameName(player.getGame());
        if (!validator.isGameName(game, CANT_BE_NULL)) {
            errors.rejectValue("game", "registration.game.invalid", new Object[]{game}, null);
        }
    }

    private boolean checkGitHubUniqueness(String github) {
        return !registration.githubIsUsed(github);
    }

    private boolean checkValidGithub(String github) {
        try {
            URL url = new URL("https://github.com/" + github);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            int statusCode = http.getResponseCode();
            return statusCode == 200;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean checkSlackEmailUniqueness(String slackEmail) {
        return !registration.slackEmailIsUsed(slackEmail);
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
