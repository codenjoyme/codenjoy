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

import com.codenjoy.dojo.services.AutoSaver;
import com.codenjoy.dojo.services.DebugService;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.TimerService;
import com.codenjoy.dojo.services.dao.ActionLogger;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static org.junit.Assert.assertEquals;

@Component
@RequiredArgsConstructor
@Scope(SCOPE_CUCUMBER_GLUE)
public class AdminPage {

    public static final String URL = "/admin?room=";

    private final Page page;
    private final WebDriverWrapper web;
    private final AutoSaver autoSaver;
    private final DebugService debugService;
    private final TimerService timerService;
    private final ActionLogger actionLogger;
    private final PlayerService playerService;

    public void cleanUp() {
        actionLogger.pause();
        autoSaver.resume();
        debugService.pause();
        timerService.resume();
        playerService.openRegistration();
        timerService.changePeriod(1000);
    }

    public void open() {
        web.open("/admin");
    }

    public void assertOnPage() {
        page.assertPage("admin");
    }

    public void assertRegistrationActive(boolean active) {
        String status = registrationActiveStatus().getText();
        String linkText = registrationChangeActiveLink().getText();
        if (active) {
            assertEquals("Registration is active", status);
            assertEquals("Close registration", linkText);
        } else {
            assertEquals("Registration was closed", status);
            assertEquals("Open registration", linkText);
        }
    }

    public WebElement registrationActiveStatus() {
        return web.element("#closeRegistration td b");
    }

    public WebElement registrationChangeActiveLink() {
        return web.element("#closeRegistration td a");
    }

    public WebElement pauseResumeGameStatus() {
        return web.element("#pauseGame td b");
    }

    public WebElement pauseResumeGameLink() {
        return web.element("#pauseGame td a");
    }

    public void assertGameIsActive(boolean active) {
        String status = pauseResumeGameStatus().getText();
        String linkText = pauseResumeGameLink().getText();
        if (active) {
            assertEquals("Game in this room is active", status);
            assertEquals("Pause game", linkText);
        } else {
            assertEquals("Game in this room was suspended", status);
            assertEquals("Resume game", linkText);
        }
    }

    public void selectRoom(String room) {
        web.element("#game-" + room + "-room-0").click();
    }

    public void assertRoom(String room) {
        assertEquals(room, page.pageSetting("game"));
        assertEquals(room, page.pageSetting("room"));
        page.assertUrl(URL + room);
    }
}
