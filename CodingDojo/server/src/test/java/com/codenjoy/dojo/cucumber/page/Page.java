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

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.codenjoy.dojo.cucumber.utils.Assert.assertEquals;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

// Please use this manual for xpath
// http://www.zvon.org/xxl/XPathTutorial/Output_rus/example1.html
@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class Page {

    public static final String CODE = "code";
    public static final String GAME = "game";
    public static final String PLAYER_ID = "playerId";

    // page objects
    private final WebDriverWrapper web;
    private final Storage storage;

    public void refresh() {
        web.refresh();
    }

    public String injectSettings(String data) {
        data = replace(data, "<PLAYER_ID>", PLAYER_ID);
        data = replace(data, "<CODE>", CODE);
        data = replace(data, "<GAME>", GAME);
        for (Map.Entry<String, String> entry : storage.all()) {
            data = data.replaceAll(String.format("<%s>", entry.getKey()), entry.getValue());
        }
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

    public WebElement logLink() {
        return web.element(".logout-link");
    }

    public void assertPage(String name) {
        assertEquals(name, pageSetting("page"));
    }

    public void assertUrl(String url) {
        assertEquals(injectSettings(url), url());
    }

    public String url() {
        return web.url();
    }

    public void logout() {
        assertLogoutLink();
        logLink().click();
    }

    public void login() {
        assertLoginLink();
        logLink().click();
    }

    public void assertLoginLink() {
        assertEquals("Login", logLink().getText());
    }

    public void assertLogoutLink() {
        assertEquals("Logout", logLink().getText());
    }

    public void open(String url) {
        web.open(injectSettings(url));
    }

    public void assertGame(String game) {
        assertEquals(game, pageSetting("game"));
    }

    public void assertRoom(String room) {
        assertEquals(room, pageSetting("room"));
    }
}
