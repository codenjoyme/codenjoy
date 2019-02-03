package com.codenjoy.dojo.web.rest.pojo;

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

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerSave;
import com.codenjoy.dojo.services.dao.Registration;

import java.util.List;

public class PlayerDetailInfo {

    private String name;
    private String readableName;
    private String callbackUrl;
    private String gameType;
    private PMuptiplayerType multiplayer;
    private String score;
    private String save;
    private PHeroData hero;
    private PLevelProgress progress;
    private List<String> group;
    private Registration.User registration;

    public PlayerDetailInfo() {
        // do nothing
    }

    public PlayerDetailInfo(Player player, Registration.User registration,
                            Game game, List<String> group)
    {
        gameType = player.getGameType().name();
        multiplayer = new PMuptiplayerType(player.getGameType().getMultiplayerType());

        callbackUrl = player.getCallbackUrl();
        score = String.valueOf(player.getScore());
        name = player.getName();

        this.registration = registration;

        progress = new PLevelProgress(game.getProgress());
        save = game.getSave().toString();
        hero = new PHeroData(game.getHero());
        this.group = group;
    }

    public PlayerSave buildPlayerSave() {
        return PlayerSave.get(name, callbackUrl, gameType, Integer.valueOf(score), save);
    }

    public String getReadableName() {
        return readableName;
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

    public Registration.User getRegistration() {
        return registration;
    }

    public String getName() {
        return name;
    }

    public PLevelProgress getProgress() {
        return progress;
    }

    public String getSave() {
        return save;
    }

    public PHeroData getHero() {
        return hero;
    }

    public List<String> getGroup() {
        return group;
    }

    public PMuptiplayerType getMultiplayer() {
        return multiplayer;
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

    public void setMultiplayer(PMuptiplayerType multiplayer) {
        this.multiplayer = multiplayer;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public void setHero(PHeroData hero) {
        this.hero = hero;
    }

    public void setProgress(PLevelProgress progress) {
        this.progress = progress;
    }

    public void setGroup(List<String> group) {
        this.group = group;
    }

    public void setRegistration(Registration.User registration) {
        this.registration = registration;
    }

    public void setReadableName(String readableName) {
        this.readableName = readableName;
    }
}
