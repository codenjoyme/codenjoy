package com.codenjoy.dojo.cucumber.page.admin;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2021 Codenjoy
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

import com.codenjoy.dojo.cucumber.page.PageObject;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.codenjoy.dojo.cucumber.utils.Assert.assertEquals;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class GameRoomStatus extends PageObject {

    public void createNew(String room) {
        newRoomInput().clear();
        newRoomInput().sendKeys(room);
        web.button("#gameRoomStatus", actions.createRoom).click();
    }

    private WebElement newRoomInput() {
        return web.element("#gameRoomStatus input[name=\"newRoom\"]");
    }

    public void removeRoom() {
        web.button("#gameRoomStatus", actions.deleteRoom).click();
    }

    public void assertRoom(String room) {
        assertEquals(room, web.element("#room").getText());
    }

    public void assertGameVersion(String version) {
        assertEquals(version, web.element("#gameRoomStatus textarea").getAttribute("value"));
    }

    public void assertGame(String game) {
        assertEquals(game, web.element("#game").getText());
    }
}
