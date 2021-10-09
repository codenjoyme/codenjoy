package com.codenjoy.dojo.cucumber.page.admin;

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

import com.codenjoy.dojo.cucumber.page.Page;
import com.codenjoy.dojo.cucumber.page.WebDriverWrapper;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static com.codenjoy.dojo.cucumber.utils.Assert.assertEquals;
import static com.codenjoy.dojo.cucumber.utils.PageUtils.xpath;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class Inactivity {

    // selectors
    public static final By KICK_ENABLED = xpath("//input[@name='inactivity.kickEnabled']");
    public static final By TIMEOUT_INPUT = xpath("//input[@name='inactivity.inactivityTimeout']");
    public static final By SAVE_BUTTON = xpath("//table[@id='inactivity']//input[@value='Save']");

    public static final By PLAYER_INACTIVE_TICKS = xpath("//span[@class='input-ticks-inactive']");
    public static final Function<String, By> PLAYER_INACTIVE_TICK = email -> xpath("//tr[@player='%s']//span[@class='input-ticks-inactive']", email);

    // page objects
    private final Page page;
    private final WebDriverWrapper web;

    public List<WebElement> playersInactiveTicks() {
        return web.elementsBy(PLAYER_INACTIVE_TICKS);
    }

    public WebElement playerInactiveTicks(String email) {
        return web.elementBy(PLAYER_INACTIVE_TICK.apply(email));
    }

    public void kickEnabled(boolean enabled) {
        web.setChecked(KICK_ENABLED, enabled);
    }

    public boolean kickEnabled() {
        return web.elementBy(KICK_ENABLED).isEnabled();
    }

    public String timeout() {
        return web.elementBy(TIMEOUT_INPUT).getAttribute("value");
    }

    public void timeout(int ticks) {
        web.text(TIMEOUT_INPUT, String.valueOf(ticks));
    }

    public void submit() {
        web.elementBy(SAVE_BUTTON).click();
    }

    @Override
    public String toString() {
        return new ImmutableMap.Builder<String, Object>()
            .put("kickEnabled", kickEnabled())
            .put("timeout", timeout())
            .build().toString();
    }

    public void assertPlayerKicked(String email, boolean isKicked) {
        assertEquals(isKicked, !playerInactiveTicks(email).isDisplayed());
    }
}
