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
import com.codenjoy.dojo.web.controller.AdminSettings;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.codenjoy.dojo.cucumber.utils.Assert.assertEquals;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class GameRecording {

    // page objects
    private final Page page;
    private final WebDriverWrapper web;
    private final Server server;

    public WebElement recordingStatus() {
        return web.element("#recordGame td b");
    }

    private WebElement stopButton() {
        return web.button("#recordGame", AdminSettings.STOP_RECORDING);
    }

    private WebElement startButton() {
        return web.button("#recordGame", AdminSettings.START_RECORDING);
    }

    public void assertRecordingIsSuspended() {
        assertEquals("The recording was suspended", recordingStatus().getText());
        assertEquals(AdminSettings.START_RECORDING, startButton().getAttribute("value"));
    }

    public void assertRecordingIsStarted() {
        assertEquals("The recording is active", recordingStatus().getText());
        assertEquals(AdminSettings.STOP_RECORDING, stopButton().getAttribute("value"));
    }

    public void stop() {
        stopButton().click();
    }

    public void start() {
        startButton().click();
    }
}
