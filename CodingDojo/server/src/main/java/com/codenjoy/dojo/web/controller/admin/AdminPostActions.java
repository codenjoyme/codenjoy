package com.codenjoy.dojo.web.controller.admin;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Component
public class AdminPostActions {

    public String deleteRoom;
    public String createRoom;
    public String saveActiveGames;
    public String pauseGame;
    public String resumeGame;
    public String stopRecording;
    public String startRecording;
    public String stopDebug;
    public String startDebug;
    public String updateLoggers;
    public String stopAutoSave;
    public String startAutoSave;
    public String closeRegistration;
    public String openRegistration;
    public String closeRoomRegistration;
    public String openRoomRegistration;
    public String cleanAllScores;
    public String reloadAllRooms;
    public String reloadAllPlayers;
    public String setTimerPeriod;

    @Autowired
    private MessageSource messageSource;

    @PostConstruct
    public void init() {
        deleteRoom = get("admin.post.room.delete");
        createRoom = get("admin.post.room.create");
        saveActiveGames = get("admin.post.games.active.save");
        pauseGame = get("admin.post.game.pause");
        resumeGame = get("admin.post.game.resume");
        stopRecording = get("admin.post.recording.stop");
        startRecording = get("admin.post.recording.start");
        stopDebug = get("admin.post.debug.stop");
        startDebug = get("admin.post.debug.start");
        updateLoggers = get("admin.post.loggers.update");
        stopAutoSave = get("admin.post.autoSave.stop");
        startAutoSave = get("admin.post.autoSave.start");
        closeRegistration = get("admin.post.registration.close");
        openRegistration = get("admin.post.registration.open");
        closeRoomRegistration = get("admin.post.registration.room.close");
        openRoomRegistration = get("admin.post.registration.room.open");
        cleanAllScores = get("admin.post.scores.clean");
        reloadAllRooms = get("admin.post.rooms.reload");
        reloadAllPlayers = get("admin.post.players.reload");
        setTimerPeriod = get("admin.post.timer.setPeriod");
    }

    private String get(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

}
