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

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.codenjoy.dojo.cucumber.utils.Assert.assertEquals;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class LoginPage extends PageObject {

    // selectors
    public static final String PASSWORD_INPUT = "#password input";
    public static final String GAME_SELECT = "#game select";
    public static final String ROOM_SELECT = "#room select";
    public static final String SUBMIT_BUTTON = "#submit-button";
    public static final String REGISTER_BUTTON = "#register-button";
    public static final String ERROR_MESSAGE = "#error-message";
    public static final String EMAIL_INPUT = "#email input";

    public void email(String email) {
        web.text(EMAIL_INPUT, email);
    }

    public void password(String password) {
        web.text(PASSWORD_INPUT, password);
    }

    public void game(String game) {
        web.select(GAME_SELECT, game);
    }

    public void submit() {
        web.click(SUBMIT_BUTTON);
    }

    public void assertErrorMessage(String error) {
        assertEquals(error, web.element(ERROR_MESSAGE).getText());
    }

    public void adminOpen() {
        web.open("/login/admin");
    }

    public void open() {
        web.open("/login");
    }

    public void clickRegister() {
        web.element(REGISTER_BUTTON).click();
    }

    public void assertFormHidden() {
        assertEquals(false, web.exists(EMAIL_INPUT));
        assertEquals(false, web.exists(PASSWORD_INPUT));
        assertEquals(false, web.exists(GAME_SELECT));
        assertEquals(false, web.exists(SUBMIT_BUTTON));
        assertEquals(false, web.exists(REGISTER_BUTTON));
    }

    public void assertRoomsAvailable(String rooms) {
        assertEquals(rooms, web.options(ROOM_SELECT).toString());
    }
}
