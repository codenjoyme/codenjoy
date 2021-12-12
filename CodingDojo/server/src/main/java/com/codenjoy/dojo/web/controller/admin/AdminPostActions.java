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

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Component
@Getter
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
    public String loadSaveForAll;
    public String saveRegistrationFormSettings;
    public String createDummyUsers;

    @Autowired
    private MessageSource messages;

    @PostConstruct
    public void init() {
        deleteRoom = get("admin.post.deleteRoom");
        createRoom = get("admin.post.createRoom");
        saveActiveGames = get("admin.post.saveActiveGames");
        pauseGame = get("admin.post.pauseGame");
        resumeGame = get("admin.post.resumeGame");
        stopRecording = get("admin.post.stopRecording");
        startRecording = get("admin.post.startRecording");
        stopDebug = get("admin.post.stopDebug");
        startDebug = get("admin.post.startDebug");
        updateLoggers = get("admin.post.updateLoggers");
        stopAutoSave = get("admin.post.stopAutoSave");
        startAutoSave = get("admin.post.startAutoSave");
        closeRegistration = get("admin.post.closeRegistration");
        openRegistration = get("admin.post.openRegistration");
        closeRoomRegistration = get("admin.post.closeRoomRegistration");
        openRoomRegistration = get("admin.post.openRoomRegistration");
        cleanAllScores = get("admin.post.cleanAllScores");
        reloadAllRooms = get("admin.post.reloadAllRooms");
        reloadAllPlayers = get("admin.post.reloadAllPlayers");
        setTimerPeriod = get("admin.post.setTimerPeriod");
        loadSaveForAll = get("admin.post.loadSaveForAll");
        saveRegistrationFormSettings = get("admin.post.saveRegistrationFormSettings");
        createDummyUsers = get("admin.post.createDummyUsers");
    }

    private String get(String key) {
        return messages.getMessage(key, null, Locale.getDefault());
    }

}
