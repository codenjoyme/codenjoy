package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.dao.Registration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;

/**
 * @author Igor_Petrov@epam.com
 * Created at 3/27/2019
 */
@Component
@RequiredArgsConstructor
public class RegistrationValidator implements Validator {

    @Value("${registration.nickname.allowed}")
    private boolean nicknameAllowed;

    private final com.codenjoy.dojo.web.controller.Validator validator;
    private final RoomsAliaser rooms;
    private final Registration registration;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Player.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target == null) {
            return;
        }
        Player player = (Player) target;
        String name = player.getReadableName();

        if (!validateNicknameStructure(name)) {
            errors.rejectValue("readableName", "registration.nickname.invalid", new Object[] {name}, null);
        }

        if (!validateUniqueName(name)) {
            errors.rejectValue("readableName", "registration.nickname.alreadyUsed", new Object[] {name}, null);
        }


        String email = player.getEmail();
        if (!validator.checkEmail(email, CANT_BE_NULL)) {
            errors.rejectValue("email", "registration.email.invalid", new Object[] {email}, null);
        }

        String idByName = registration.getIdByName(name);
        String idByEmail = registration.getIdByEmail(email);

        boolean emailIsUsed = registration.emailIsUsed(email);


        if (StringUtils.isEmpty(idByName) && StringUtils.isEmpty(idByEmail)) {
            if (emailIsUsed) {
                errors.rejectValue("email", "registration.email.alreadyUsed");
            }
        }

        String gameName = rooms.getGameName(player.getGameName());
        if (!validator.checkGameName(gameName, CANT_BE_NULL)) {
            errors.rejectValue("gameName", "registration.game.invalid", new Object[] {gameName}, null);
        }
    }

    private boolean validateUniqueName(String name) {
        return !registration.nameIsUsed(name);
    }

    private boolean validateNicknameStructure(String name) {
        if (nicknameAllowed) {
            return validator.checkNickName(name);
        } else {
            return validator.checkReadableName(name);
        }
    }
}
