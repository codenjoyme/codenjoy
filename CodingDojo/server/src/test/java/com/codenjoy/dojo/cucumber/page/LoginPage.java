package com.codenjoy.dojo.cucumber.page;

import com.codenjoy.dojo.cucumber.WebDriverWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static org.junit.Assert.assertEquals;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class LoginPage {

    @Autowired
    private Page page;

    @Autowired
    private WebDriverWrapper web;


    public void email(String email) {
        web.text("#email input", email);
    }

    public void password(String password) {
        web.text("#password input", password);
    }

    public void game(String game) {
        web.select("#game select", game);
    }

    public void submit() {
        web.click("#submit-button");
    }

    public void assertErrorMessage(String error) {
        assertEquals(error, web.element("#error-message").getText());
    }

    public void open() {
        web.open("/login");
    }
}
