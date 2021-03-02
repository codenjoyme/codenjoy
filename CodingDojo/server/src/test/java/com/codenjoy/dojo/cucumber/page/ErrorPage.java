package com.codenjoy.dojo.cucumber.page;

import com.codenjoy.dojo.cucumber.WebDriverWrapper;
import com.codenjoy.dojo.cucumber.definitions.StepDefinitions;
import com.codenjoy.dojo.services.ErrorTicketService;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static org.junit.Assert.assertEquals;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class ErrorPage extends Page {

    @Autowired
    private WebDriverWrapper web;

    @Autowired
    private ErrorTicketService tickets;

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

    public void assertErrorPage() {
        assertPage("error");
    }

    public void assertTicketNumber() {
        assertEquals("Your ticket number is: " + tickets().get(0),
                ticketMessage().getText());
    }

    public void assertErrorMessage(String message) {
        assertEquals(message, errorMessage().getText());
    }
}