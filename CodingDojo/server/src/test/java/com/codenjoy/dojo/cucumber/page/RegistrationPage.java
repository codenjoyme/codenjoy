package com.codenjoy.dojo.cucumber.page;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.client.Closeable;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.SaveService;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.security.GameAuthoritiesConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.codenjoy.dojo.cucumber.page.Page.CODE;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static org.junit.Assert.assertEquals;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class RegistrationPage implements Closeable {

    // selectors
    public static final String SUBMIT_BUTTON = "#submit-button";
    public static final String READABLE_NAME_INPUT = "#readableName input";
    public static final String PASSWORD_INPUT = "#password input";
    public static final String EMAIL_INPUT = "#email input";
    public static final String PASSWORD_CONFIRMATION_INPUT = "#passwordConfirmation input";
    public static final String COUNTRY_INPUT = "#data1 input";
    public static final String TECH_INPUT = "#data2 input";
    public static final String COMPANY_INPUT = "#data3 input";
    public static final String EXPERIENCE_INPUT = "#data4 input";
    public static final String GAME_SELECT = "#game select";
    public static final String ROOM_SELECT = "#room select";
    public static final String ERROR_MESSAGE = "#error-message";

    // page objects
    private final Page page;
    private final WebDriverWrapper web;

    // application services
    private final Registration registration;
    private final PlayerService playerService;
    private final GameService gameService;
    private final SaveService saveService;

    @Override
    public void close() {
        registration.removeAll();
        playerService.removeAll();
        gameService.removeAll();
        saveService.removeAllSaves();
    }

    public void assertUserInDatabase(String user) {
        assertEquals(page.injectSettings(user),
                registration.getUserByCode(page.pageSetting(CODE)).toString());
    }

    public void registerInDatabase(String name, String email, String password, String country, String techSkills, String company, String experience) {
        String data = String.join("%s|%s|%s|%s", country, techSkills, company, experience);
        String id = Hash.getRandomId();
        registration.register(id, email, name, Hash.md5(password), data,
                Arrays.asList(GameAuthoritiesConstants.ROLE_USER));

    }

    public void submit() {
        web.click(SUBMIT_BUTTON);
    }

    public void name(String name) {
        web.text(READABLE_NAME_INPUT, name);
    }

    public void password(String password) {
        web.text(PASSWORD_INPUT, password);
    }

    public void email(String email) {
        web.text(EMAIL_INPUT, email);
    }

    public void confirmPassword(String password) {
        web.text(PASSWORD_CONFIRMATION_INPUT, password);
    }

    public void country(String country) {
        web.text(COUNTRY_INPUT, country);
    }

    public void tech(String techSkills) {
        web.text(TECH_INPUT, techSkills);
    }

    public void company(String company) {
        web.text(COMPANY_INPUT, company);
    }

    public void experience(String experience) {
        web.text(EXPERIENCE_INPUT, experience);
    }

    public void game(String game) {
        web.select(GAME_SELECT, game);
    }

    public void room(String room) {
        web.select(ROOM_SELECT, room);
    }

    public void open() {
        web.open("/register");
    }

    public void assertErrorMessage(String error) {
        assertEquals(error, web.element(ERROR_MESSAGE).getText());
    }

    public void assertFormHidden() {
        assertEquals(false, web.exists(READABLE_NAME_INPUT));
        assertEquals(false, web.exists(PASSWORD_INPUT));
        assertEquals(false, web.exists(EMAIL_INPUT));
        assertEquals(false, web.exists(PASSWORD_CONFIRMATION_INPUT));
        assertEquals(false, web.exists(COUNTRY_INPUT));
        assertEquals(false, web.exists(TECH_INPUT));
        assertEquals(false, web.exists(COMPANY_INPUT));
        assertEquals(false, web.exists(EXPERIENCE_INPUT));
        assertEquals(false, web.exists(GAME_SELECT));
        assertEquals(false, web.exists(ROOM_SELECT));
        assertEquals(false, web.exists(SUBMIT_BUTTON));
    }

    public void assertRoomsAvailable(String rooms) {
        assertEquals(rooms, web.options(ROOM_SELECT).toString());
    }
}
