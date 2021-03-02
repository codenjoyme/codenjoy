package com.codenjoy.dojo.cucumber.page;

import com.codenjoy.dojo.cucumber.WebDriverWrapper;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.security.GameAuthoritiesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.codenjoy.dojo.cucumber.page.Page.CODE;
import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class RegistrationPage {

    @Autowired
    private Page page;

    @Autowired
    private WebDriverWrapper web;

    @Autowired
    private Registration registration;

    public void clear() {
        registration.removeAll();
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
        web.click("#submit-button");
    }

    public void name(String name) {
        web.text("#readableName input", name);
    }

    public void password(String password) {
        web.text("#password input", password);
    }

    public void email(String email) {
        web.text("#email input", email);
    }

    public void confirmPassword(String password) {
        web.text("#passwordConfirmation input", password);
    }

    public void country(String country) {
        web.text("#data1 input", country);
    }

    public void tech(String techSkills) {
        web.text("#data2 input", techSkills);
    }

    public void company(String company) {
        web.text("#data3 input", company);
    }

    public void experience(String experience) {
        web.text("#data4 input", experience);
    }

    public void game(String game) {
        web.select("#game select", game);
    }
}
