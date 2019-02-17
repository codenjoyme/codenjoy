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

public class PlayerDetailInfo {

    private String name;
    private String readableName;
    private String callbackUrl;
    private String gameType;
    private String score;
    private String save;
    private User registration;

    public PlayerDetailInfo() {
        // do nothing
    }

    public PlayerDetailInfo(String name, String readableName, String callbackUrl, String gameType,
                            String score, String save, User registration)
    {
        this.name = name;
        this.readableName = readableName;
        this.callbackUrl = callbackUrl;
        this.gameType = gameType;
        this.score = score;
        this.save = save;
        this.registration = registration;
    }


    public String getGameType() {
        return gameType;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getScore() {
        return score;
    }

    public User getRegistration() {
        return registration;
    }

    public String getName() {
        return name;
    }

    public String getSave() {
        return save;
    }

    public String getReadableName() {
        return readableName;
    }

    public void setReadableName(String readableName) {
        this.readableName = readableName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public void setRegistration(User registration) {
        this.registration = registration;
    }

    @Override
    public String toString() {
        return "PlayerDetailInfo{" +
                "name='" + name + '\'' +
                ", id='" + readableName + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", gameType='" + gameType + '\'' +
                ", score='" + score + '\'' +
                ", save='" + save + '\'' +
                ", registration=" + registration +
                '}';
    }
}
