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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.codenjoy.dojo.cucumber.utils.PageUtils.pair;
import static com.codenjoy.dojo.cucumber.utils.PageUtils.xpath;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class ActiveGames {

    // selectors
    public static final By ALL_GAMES = xpath("//form/table[@id='activeGames']//tr[@game]");
    public static final By ROOMS = xpath(".//td/a[count(span)=1]");
    public static final Function<String, By> ROOM_LINK = room -> xpath("//a[@room='%s']", room);
    public static final Function<String, By> ROOM_PLAYERS = room -> xpath("//a[@room='%s']/span", room);

    // page objects
    private final Page page;
    private final WebDriverWrapper web;

    public Multimap<String, String> getGamesRooms() {
        List<WebElement> gamesRows = web.elementsBy(ALL_GAMES);
        return gamesRows.stream()
                .flatMap(game -> game.findElements(ROOMS).stream()
                        .map(room -> pair(game, room)))
                .collect(Multimaps.toMultimap(
                        pair -> pair.getLeft().getAttribute("game"),
                        pair -> pair.getRight().getAttribute("room"),
                        HashMultimap::create));
    }

    public List<String> getRooms() {
        return getGamesRooms().asMap().entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .sorted()
                .collect(toList());
    }

    public Map<String, Integer> getPlayersInRooms() {
        return getRooms().stream()
                .map(room -> roomCount(room))
                .collect(toMap(pair -> pair.getLeft(),
                        pair -> pair.getRight(),
                        (value1, value2) -> value2,
                        LinkedHashMap::new));
    }

    private ImmutablePair<String, Integer> roomCount(String room) {
        return pair(room, Integer.valueOf(web.elementBy(ROOM_PLAYERS.apply(room)).getText()));
    }

    public void selectRoom(String room) {
        web.elementBy(ROOM_LINK.apply(room)).click();
    }
}
