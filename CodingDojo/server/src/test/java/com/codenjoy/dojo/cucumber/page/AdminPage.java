package com.codenjoy.dojo.cucumber.page;

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

    public void open() {
        web.open("/admin");
    }

}
