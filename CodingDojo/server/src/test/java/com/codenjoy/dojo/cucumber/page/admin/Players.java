package com.codenjoy.dojo.cucumber.page.admin;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.codenjoy.dojo.cucumber.utils.PageUtils.xpath;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class Players extends PageObject {

    // selectors
    public static final By ALL_PLAYERS = xpath("//table[@id='players']");
    public static final BiFunction<String, String, By> FOUND_PLAYER_BY = (key, value) -> xpath(".//input[@class='%s'][@value='%s']/../..", key, value);
    public static final Function<String, By> UPDATE_PLAYER_FIELD = key -> xpath(".//input[@class='%s']", key);
    public static final By BOARD_LINK = xpath(".//td[17]/a"); // TODO to use attribute in the td instead of index
    public static final By SAVE = xpath("./../button");
    public static final Function<String, By> LOAD_ALL_BUTTON = name -> xpath("//table[@id='players']//input[@value='%s']", name);

    public String playerBoardLink(String selectorKey, String selectorValue) {
        WebElement player = player(selectorKey, selectorValue);
        WebElement link = player.findElement(BOARD_LINK);
        return server.relative(link.getAttribute("href"));
    }

    private WebElement player(String selectorKey, String selectorValue) {
        WebElement table = web.elementBy(ALL_PLAYERS);
        return table.findElement(FOUND_PLAYER_BY.apply(selectorKey, selectorValue));
    }

    public void updatePlayer(String selectorKey, String selectorValue, String key, String value) {
        WebElement player = player(selectorKey, selectorValue);
        WebElement field = player.findElement(UPDATE_PLAYER_FIELD.apply(key));
        field.clear();
        field.sendKeys(value);
        WebElement save = field.findElement(SAVE);
        save.click();
    }

    private WebElement loadAllButton() {
        return web.elementBy(LOAD_ALL_BUTTON.apply(actions.loadAllPlayers));
    }

    public void loadAllPlayers() {
        loadAllButton().click();
    }

}
