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

import com.codenjoy.dojo.cucumber.WebDriverWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static org.junit.Assert.assertEquals;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class AdminPage {

    @Autowired
    private Page page;

    @Autowired
    private WebDriverWrapper web;

    @Autowired
    private AutoSaver autoSaver;

    @Autowired
    private DebugService debugService;

    @Autowired
    private TimerService timerService;

    @Autowired
    private ActionLogger actionLogger;

    @Autowired
    private PlayerService playerService;

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
}
