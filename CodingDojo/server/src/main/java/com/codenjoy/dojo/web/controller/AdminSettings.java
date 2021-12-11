package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.PlayerInfo;
import com.codenjoy.dojo.services.StatisticService;
import com.codenjoy.dojo.services.incativity.InactivitySettingsImpl;
import com.codenjoy.dojo.services.level.LevelsSettingsImpl;
import com.codenjoy.dojo.services.room.GamesRooms;
import com.codenjoy.dojo.services.round.RoundSettingsImpl;
import com.codenjoy.dojo.services.semifinal.SemifinalSettingsImpl;
import com.codenjoy.dojo.services.settings.Parameter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AdminSettings {

    // actions
    public static final String DELETE_ROOM = "Delete room";
    public static final String CREATE_ROOM = "Create room";
    public static final String SAVE_ACTIVE_GAMES = "Save active games";
    public static final String PAUSE_GAME = "Pause game";
    public static final String RESUME_GAME = "Resume game";
    public static final String STOP_RECORDING = "Stop recording";
    public static final String START_RECORDING = "Start recording";
    public static final String STOP_DEBUG = "Stop debug";
    public static final String START_DEBUG = "Start debug";
    public static final String UPDATE_LOGGERS = "Update loggers";
    public static final String STOP_AUTO_SAVE = "Stop auto save";
    public static final String START_AUTO_SAVE = "Start auto save";
    public static final String SET_TIMER_PERIOD = "Set timer period";

    private List<PlayerInfo> players;
    private List<Object> activeGames;

    // used to get data from jsp
    private String action; // submit button value
    
    private List<Object> otherValues;
    private List<Object> levelsValues;
    private List<Object> levelsKeys;
    private List<Object> levelsNewKeys;

    private String loggersLevels;
    private String game;
    private String room;
    private String newRoom;
    private String generateNameMask;
    private Integer generateCount;
    private String generateRoom;
    private Integer timerPeriod;
    private String progress;
    private Integer semifinalTick;
    private String gameVersion;
    private Boolean active;
    private Boolean roomOpened;
    private Boolean recording;
    private Boolean autoSave;
    private Boolean debugLog;
    private Boolean opened;
    private GamesRooms gamesRooms;
    private Map<String, Integer> playersCount;

    // used to send data to jsp
    private SemifinalSettingsImpl semifinal;
    private RoundSettingsImpl rounds;
    private InactivitySettingsImpl inactivity;
    private LevelsSettingsImpl levels;
    private List<Parameter> other;
    private StatisticService statistic;

}
