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
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

@Component
@RequiredArgsConstructor
public class RegistrationValidator implements Validator {

    public static final String READABLE_NAME = "readableName";
    public static final String FULL_NAME = "fullName";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String PASSWORD_CONFORMATION = "passwordConfirmation";
    public static final String GITHUB_USERNAME = "gitHubUsername";
    public static final String SLACK_EMAIL = "slackEmail";
    public static final String GAME = "game";

    public static final String GITHUB_URL = "https://github.com/";

    public static final String REGISTRATION_CLOSED = "registration.closed";
    public static final String INVALID_FULL_NAME = "registration.fullName.invalid";
    public static final String ALREADY_USED_NICKNAME = "registration.nickname.alreadyUsed";
    public static final String INVALID_EMAIL = "registration.email.invalid";
    public static final String ALREADY_USED_EMAIL = "registration.email.alreadyUsed";
    public static final String EMPTY_PASSWORD = "registration.password.empty";
    public static final String PASSWORD_LENGTH = "registration.password.length";
    public static final String INVALID_PASSWORD_CONFIRMATION = "registration.password.invalidConfirmation";
    public static final String ALREADY_USED_GITHUB_USERNAME = "registration.gitHubUsername.alreadyUsed";
    public static final String INVALID_GITHUB_USERNAME = "registration.gitHubUsername.invalidGit";
    public static final String ALREADY_USED_SLACK_EMAIL = "registration.slackEmail.alreadyUsed";
    public static final String INVALID_GAME = "registration.game.invalid";
    public static final String NICKNAME_INVALID = "registration.nickname.invalid";
    private final com.codenjoy.dojo.web.controller.Validator validator;
    private final RoomsAliaser rooms;
    private final Registration registration;
    private final PlayerService playerService;
    @Value("${registration.nickname.allowed}")
    private boolean nicknameAllowed;
    @Value("${registration.password.min-length}")
    private int minPasswordLen;

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
            errors.rejectValue(READABLE_NAME, REGISTRATION_CLOSED);
        }

        Player player = (Player) target;

        validateFullName(errors, player);
        validateNickName(errors, player);
        validateEmail(errors, player);
        validatePassword(errors, player);
        validateGithub(errors, player);
        validateSlackEmail(errors, player);
        validateGameName(errors, player);
    }


    private void validateSlackEmail(Errors errors, Player player) {
        String slackEmail = player.getSlackEmail();
        if (!checkSlackEmailUniqueness(slackEmail)) {
            errors.rejectValue(SLACK_EMAIL, ALREADY_USED_SLACK_EMAIL);
        }
        if (!validateSlackEmailStructure(slackEmail)) {
            errors.rejectValue(SLACK_EMAIL, INVALID_EMAIL, new Object[]{slackEmail}, null);
        }
    }

    private void validateGameName(Errors errors, Player player) {
        String gameName = player.getGame();
        if (!validator.isGameName(gameName, CANT_BE_NULL)) {
            errors.rejectValue(GAME, INVALID_GAME, new Object[]{gameName}, null);
        }
    }

    private void validateGithub(Errors errors, Player player) {
        String github = player.getGitHubUsername();
        if (!checkGitHubUniqueness(github)) {
            errors.rejectValue(GITHUB_USERNAME, ALREADY_USED_GITHUB_USERNAME);
        }

        if (!checkValidGithub(github)) {
            errors.rejectValue(GITHUB_USERNAME, INVALID_GITHUB_USERNAME);
        }
    }

    private void validatePassword(Errors errors, Player player) {
        String password = player.getPassword();
        if (!checkPasswordNotEmpty(password)) {
            errors.rejectValue(PASSWORD, EMPTY_PASSWORD);
        }

        if (!checkPasswordLength(password)) {
            errors.rejectValue(PASSWORD, PASSWORD_LENGTH, new Object[]{minPasswordLen}, null);
        }

        if (!checkPasswordConfirmation(password, player.getPasswordConfirmation())) {
            errors.rejectValue(PASSWORD_CONFORMATION, INVALID_PASSWORD_CONFIRMATION);
        }
    }

    private void validateEmail(Errors errors, Player player) {
        String email = player.getEmail();
        if (!validateEmailStructure(email)) {
            errors.rejectValue(EMAIL, INVALID_EMAIL, new Object[]{email}, null);
        }

        if (!checkEmailUniqueness(email)) {
            errors.rejectValue(EMAIL, ALREADY_USED_EMAIL);
        }
    }

    private void validateNickName(Errors errors, Player player) {
        String name = player.getReadableName();
        if (!checkNameUniqueness(name)) {
            errors.rejectValue(READABLE_NAME, ALREADY_USED_NICKNAME, new Object[]{name}, null);
        }
        if (!validator.isReadableName(name)) {
            errors.rejectValue(READABLE_NAME, NICKNAME_INVALID, new Object[]{name}, null);
        }
    }

    private void validateFullName(Errors errors, Player player) {
        String fullName = player.getFullName();
        if (!validateNicknameStructure(fullName)) {
            errors.rejectValue(FULL_NAME, INVALID_FULL_NAME, new Object[]{fullName}, null);
        }
    }

    private boolean checkGitHubUniqueness(String github) {
        return !registration.githubIsUsed(github);
    }

    private boolean checkValidGithub(String github) {
        try {
            URL url = new URL(GITHUB_URL + github);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            int statusCode = http.getResponseCode();
            return statusCode == 200;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean checkSlackEmailUniqueness(String slackEmail) {
        if (slackEmail.equals("")) {
            return true;
        }
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

    private boolean validateSlackEmailStructure(String slackEmail) {
        return validator.isEmail(slackEmail, CAN_BE_NULL);
    }

    private boolean validateNicknameStructure(String name) {
        if (nicknameAllowed) {
            return validator.isNickName(name);
        } else {
            return validator.isReadableName(name);
        }
    }
}
