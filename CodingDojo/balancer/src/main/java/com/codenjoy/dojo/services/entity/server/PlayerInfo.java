package com.codenjoy.dojo.services.entity.server;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

public class PlayerInfo {

    private String gameType;
    private String callbackUrl;
    private String name;
    private String score;
    private String code;

    public PlayerInfo() {
        // do nothing
    }

    public PlayerInfo(String gameType, String callbackUrl, String name, String score, String code) {
        this.gameType = gameType;
        this.callbackUrl = callbackUrl;
        this.name = name;
        this.score = score;
        this.code = code;
    }

    public PlayerInfo(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getGameType() {
        return gameType;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "gameType='" + gameType + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", name='" + name + '\'' +
                ", score='" + score + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }
}
