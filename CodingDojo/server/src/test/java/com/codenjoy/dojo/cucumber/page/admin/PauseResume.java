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

import com.codenjoy.dojo.cucumber.page.WebDriverWrapper;
import com.codenjoy.dojo.web.controller.admin.AdminPostActions;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.codenjoy.dojo.cucumber.utils.Assert.assertEquals;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class PauseResume {

    // application services
    private final AdminPostActions actions;

    // page objects
    private final WebDriverWrapper web;

    public WebElement gameStatus() {
        return web.element("#pauseResumeGame td b");
    }

    private WebElement pauseButton() {
        return web.button("#pauseResumeGame", actions.pauseGame);
    }

    private WebElement resumeButton() {
        return web.button("#pauseResumeGame", actions.resumeGame);
    }

    public void assertPaused() {
        assertEquals("Game in this room was suspended", gameStatus().getText());
        assertEquals(actions.resumeGame, resumeButton().getAttribute("value"));
    }

    public void assertActive() {
        assertEquals("Game in this room is active", gameStatus().getText());
        assertEquals(actions.pauseGame, pauseButton().getAttribute("value"));
    }

    public void pauseGame() {
        pauseButton().click();
    }

    public void resumeGame() {
        resumeButton().click();
    }
}
