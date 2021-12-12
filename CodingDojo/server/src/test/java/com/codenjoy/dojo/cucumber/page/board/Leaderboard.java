package com.codenjoy.dojo.cucumber.page.board;

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

import com.codenjoy.dojo.cucumber.page.PageObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.cucumber.utils.Assert.assertEquals;
import static com.codenjoy.dojo.cucumber.utils.PageUtils.pair;
import static com.codenjoy.dojo.cucumber.utils.PageUtils.xpath;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static java.util.stream.Collectors.toMap;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class Leaderboard extends PageObject {

    // selectors
    public static final By ALL_BOARDS_LINK = xpath("//table[@id='table-logs']//a[text() = '#']");
    public static final By ALL_PLAYERS = xpath("//table/tbody[@id='table-logs-body']/tr");
    public static final By NUMBER = xpath("./td[1]");
    public static final By NAME = xpath("./td[2]/a");
    public static final By TEAM = xpath("./td[2]/span[1]");
    public static final By YOU = xpath("./td[2]/span[2]");
    public static final By SCORE = xpath("./td[3]");

    public Map<String, Map<String, Object>> all() {
        List<WebElement> players = web.elementsBy(ALL_PLAYERS);
        return players.stream()
                .map(element -> pair(name(element), map(element)))
                .collect(toMap(ImmutablePair::getLeft, ImmutablePair::getRight,
                                (value1, value2) -> value2,
                                LinkedHashMap::new));
    }

    private Map<String, Object> map(WebElement element) {
        return new LinkedHashMap<>(){{
            String team = team(element);
            if (team != null) {
                put("team", team);
            }

            String you = you(element);
            if (you != null) {
                put("you", you);
            }

            put("score", score(element));
        }};
    }

    private Integer score(WebElement element) {
        return Integer.valueOf(element.findElement(SCORE).getText());
    }

    private String team(WebElement element) {
        String text = element.findElement(TEAM).getText();
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return text;
    }

    private String you(WebElement element) {
        String text = element.findElement(YOU).getText();
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return text;
    }

    private String name(WebElement element) {
        return element.findElement(NAME).getText();
    }

    public void waitUntilNotEmpty() {
        WebDriverWait wait = new WebDriverWait(web.driver(), 100, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(ALL_PLAYERS));
    }

    public void assertPlayers(String players) {
        assertEquals(players, all().toString());
    }

    public String allBoardsLink() {
        return server.relative(web.elementBy(ALL_BOARDS_LINK).getAttribute("href"));
    }
}
