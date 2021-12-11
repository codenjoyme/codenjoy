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

import com.codenjoy.dojo.client.Closeable;
import com.codenjoy.dojo.cucumber.page.admin.*;
import com.codenjoy.dojo.services.AutoSaver;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.TimerService;
import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.log.DebugService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

import static com.codenjoy.dojo.cucumber.utils.Assert.assertEquals;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@RequiredArgsConstructor
@Scope(SCOPE_CUCUMBER_GLUE)
public class AdminPage implements Closeable {

    // selectors
    public static final String URL = "/admin?room=";
    public static final BiFunction<String, String, String> CREATE_ROOM_URL =
            (room, game) -> String.format("/admin?room=%s&game=%s", room, game);

    // application services
    private final AutoSaver autoSaver;
    private final DebugService debugService;
    private final TimerService timerService;
    private final ActionLogger actionLogger;
    private final PlayerService playerService;

    // page objects
    private final Page page;
    private final WebDriverWrapper web;
    private final ActiveGames activeGames;
    private final Inactivity inactivity;
    private final Players players;
    private final Levels levels;
    private final TimerPeriod timerPeriod;
    private final PauseResume pauseResume;
    private final ServerRegistration serverRegistration;
    private final RoomRegistration roomRegistration;
    private final GameRoomStatus gameRoomStatus;
    private final GameRecording gameRecording;
    private final Debug debug;
    private final AutoSave autoSave;

    @Override
    public void close() {
        actionLogger.pause();
        autoSaver.resume();
        debugService.pause();
        timerService.resume();
        playerService.openRegistration();
        timerService.changePeriod(1000);
    }

    public void open() {
        web.open("/admin");
    }

    public void open(String room) {
        web.open(URL + room);
    }

    public void assertOnPage() {
        page.assertPage("admin");
    }

    public void selectRoom(String room) {
        activeGames.selectRoom(room);
    }

    public void assertGameAndRoom(String game, String room) {
        page.assertGame(game);
        page.assertRoom(room);
        page.assertUrl(URL + room);
    }

    public void assertRoomsAvailable(String expected) {
        assertEquals(expected, activeGames.getRooms().toString());
    }

    public void assertPlayersInRooms(String expected) {
        assertEquals(expected, activeGames.getPlayersInRooms().toString());
    }

    public Inactivity inactivity() {
        return inactivity;
    }

    public Players players() {
        return players;
    }

    public void clickLoadAll() {
        players.loadAllLink().click();
    }

    public Levels levels() {
        return levels;
    }

    public TimerPeriod timerPeriod() {
        return timerPeriod;
    }

    public PauseResume pauseResume() {
        return pauseResume;
    }

    public ServerRegistration serverRegistration() {
        return serverRegistration;
    }

    public RoomRegistration roomRegistration() {
        return roomRegistration;
    }

    public GameRoomStatus gameRoomStatus() {
        return gameRoomStatus;
    }

    public GameRecording gameRecording() {
        return gameRecording;
    }

    public Debug debug() {
        return debug;
    }

    public AutoSave autoSave() {
        return autoSave;
    }
}