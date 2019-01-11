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


import java.util.List;

public class AdminSettings {

    private List<PlayerInfo> players;

    private List<Object> parameters;

    private String gameName;

    private String generateNameMask;
    private String generateCount;

    private String timerPeriod;

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public List<PlayerInfo> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerInfo> players) {
        this.players = players;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGenerateNameMask() {
        return generateNameMask;
    }

    public void setGenerateNameMask(String generateNameMask) {
        this.generateNameMask = generateNameMask;
    }

    public String getGenerateCount() {
        return generateCount;
    }

    public void setGenerateCount(String generateCount) {
        this.generateCount = generateCount;
    }

    public String getTimerPeriod() {
        return timerPeriod;
    }

    public void setTimerPeriod(String timerPeriod) {
        this.timerPeriod = timerPeriod;
    }
}
