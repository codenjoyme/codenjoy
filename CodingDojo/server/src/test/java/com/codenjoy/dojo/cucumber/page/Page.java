package com.codenjoy.dojo.cucumber.page;

import com.codenjoy.dojo.cucumber.WebDriverWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class Page {

    public static final String CODE = "code";
    public static final String PLAYER_ID = "playerId";

    @Autowired
    private WebDriverWrapper web;

    public String injectSettings(String data) {
        data = replace(data, "<PLAYER_ID>", PLAYER_ID);
        data = replace(data, "<CODE>", CODE);
        return data;
    }

    private String replace(String data, String key, String attribute) {
        if (data.contains(key)) {
            String playerId = pageSetting(attribute);
            data = data.replaceAll(key, playerId);
        }
        return data;
    }

    public String pageSetting(String key) {
        return web.get("#settings", key);
    }
}
