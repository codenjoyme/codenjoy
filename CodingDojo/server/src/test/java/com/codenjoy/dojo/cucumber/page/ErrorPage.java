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

import com.codenjoy.dojo.services.ErrorTicketService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.cucumber.utils.Assert.assertEquals;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class ErrorPage {

    // page objects
    private final Page page;
    private final WebDriverWrapper web;

    // application services
    private final ErrorTicketService tickets;

    public WebElement errorMessage() {
        return web.element("#error-message");
    }

    public WebElement ticketMessage() {
        return web.element("#ticket-number");
    }

    public List<String> tickets() {
        return new LinkedList<>(tickets.getErrors().keySet());
    }

    public void clear() {
        tickets.clear();
    }

    public void assertOnPage() {
        page.assertPage("error");
    }

    public void assertTicketNumber() {
        assertEquals("Your ticket number is: " + tickets().get(0),
                ticketMessage().getText());
    }

    public void assertErrorMessage(String message) {
        assertEquals(message, errorMessage().getText());
    }
}
