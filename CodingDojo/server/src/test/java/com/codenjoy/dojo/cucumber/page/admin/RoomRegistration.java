package com.codenjoy.dojo.cucumber.page.admin;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2021 Codenjoy
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

import com.codenjoy.dojo.cucumber.page.Page;
import com.codenjoy.dojo.cucumber.page.Server;
import com.codenjoy.dojo.cucumber.page.WebDriverWrapper;
import com.codenjoy.dojo.web.controller.admin.AdminSettings;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.codenjoy.dojo.cucumber.utils.Assert.assertEquals;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class RoomRegistration {

    // page objects
    private final Page page;
    private final WebDriverWrapper web;
    private final Server server;

    private WebElement status() {
        return web.element("#roomRegistration td b");
    }

    private WebElement closeButton() {
        return web.button("#roomRegistration", AdminSettings.CLOSE_ROOM_REGISTRATION);
    }

    private WebElement openButton() {
        return web.button("#roomRegistration", AdminSettings.OPEN_ROOM_REGISTRATION);
    }

    public void assertClosed() {
        assertEquals("Room registration was closed", status().getText());
        assertEquals(AdminSettings.OPEN_ROOM_REGISTRATION, openButton().getAttribute("value"));
    }

    public void assertOpened() {
        assertEquals("Room registration is active", status().getText());
        assertEquals(AdminSettings.CLOSE_ROOM_REGISTRATION, closeButton().getAttribute("value"));
    }

    public void close() {
        closeButton().click();
    }

    public void open() {
        openButton().click();
    }

}
