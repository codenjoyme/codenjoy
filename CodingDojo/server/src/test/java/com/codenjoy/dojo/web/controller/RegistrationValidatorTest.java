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

import com.codenjoy.dojo.CodenjoyContestApplication;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.dao.Registration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Igor Petrov
 * Created at 4/29/2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SQLiteProfile.NAME)
@TestPropertySource(properties = {
        "registration.nickname.allowed=false",
        "registration.password.min-length=5"
})
public class RegistrationValidatorTest {

    @Autowired
    private RegistrationValidator validator;

    @MockBean
    private Registration registration;

    @SpyBean
    private Validator commonValidator;

    private Player player;
    private Errors errors;

    @Before
    public void setUp() {
        errors = makeErrors();
        player = new Player(){{
            setEmail("someuser@sample.org");
            setReadableName("Readable Name");
            setPassword("12345");
            setPasswordConfirmation("12345");
            setGameName("dummy");
        }};
    }

    @Test
    public void shouldPassValidUser() {
        // when
        validator.validate(player, errors);

        // then
        assertTrue("Valid player binding result must contain no errors", !errors.hasErrors());
    }

    @Test
    public void shouldValidateNicknameStructure() {
        // given
        player.setReadableName("Unreadablename");

        // when
        validator.validate(player, errors);
        
        // then
        assertError(errors, "readableName", "registration.nickname.invalid");
    }

    @Test
    public void shouldValidateUsernameUniqueness() {
        // given
        String nonUniqueName = "Nonunique Name";
        when(registration.nameIsUsed(nonUniqueName)).thenReturn(true);
        player.setReadableName(nonUniqueName);

        // when
        validator.validate(player, errors);
        
        // then
        assertError(errors,"readableName", "registration.nickname.alreadyUsed");
    }

    @Test
    public void shouldValidateEmailUniqueness() {
        // given
        String nonUniqueEmail = "duplicate@sample.org";
        when(registration.emailIsUsed(nonUniqueEmail)).thenReturn(true);
        player.setEmail(nonUniqueEmail);

        // when
        validator.validate(player, errors);
        
        // then
        assertError(errors, "email", "registration.email.alreadyUsed");
    }

    @Test
    public void shouldRejectEmptyPassword() {
        // given
        player.setPassword("");

        // when
        validator.validate(player, errors);
        
        // then
        assertError(errors, "password", "registration.password.empty");
    }

    @Test
    public void shouldRejectShortPassword() {
        // given
        player.setPassword("1234");

        // when
        validator.validate(player, errors);
        
        // then
        assertError(errors, "password", "registration.password.length");
    }

    @Test
    public void shouldCheckPasswordConfirmation() {
        // given
        player.setPassword("12345");
        player.setPasswordConfirmation("1234");

        // when
        validator.validate(player, errors);
        
        // then
        assertError(errors,"passwordConfirmation", "registration.password.invalidConfirmation");
    }

    @Test
    public void shouldValidateGameName() {
        // given
        String invalidGameName = "invalidGame";
        when(commonValidator.isGameName(invalidGameName, Validator.CANT_BE_NULL)).thenReturn(false);
        player.setGameName(invalidGameName);

        // when
        validator.validate(player, errors);

        // then
        assertError(errors, "gameName", "registration.game.invalid");
    }

    private void assertError(Errors errors, String field, String expectedCode) {
        assertTrue(errors.getErrorCount() > 0);

        FieldError fieldError = errors.getFieldError(field);
        assertNotNull("Error field must have been set", fieldError);

        assertTrue(String.format("Missing expected error code for field '%s'\nExpected: '%s', actual ones: %s",
                field, expectedCode, Arrays.toString(fieldError.getCodes())),
                Arrays.asList(fieldError.getCodes()).contains(expectedCode));
    }

    private static Errors makeErrors() {
        return new MapBindingResult(new HashMap<>(), "dummy");
    }
}
