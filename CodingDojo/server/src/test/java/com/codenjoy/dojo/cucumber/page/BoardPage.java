package com.codenjoy.dojo.cucumber.page;

import com.codenjoy.dojo.cucumber.WebDriverWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class BoardPage {

    public static final String URL = "/board/player/<PLAYER_ID>?code=<CODE>&game=<GAME>";

    @Autowired
    private Page page;

    @Autowired
    private WebDriverWrapper web;

    public void open() {
        web.open("/board");
    }

    public void assertOnPage() {
        page.assertPage("board");
    }
}
