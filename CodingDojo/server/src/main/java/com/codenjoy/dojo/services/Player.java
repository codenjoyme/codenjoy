package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.transport.screen.ScreenRecipient;

public class Player implements ScreenRecipient {

    private String name;
    private String code;
    private String data;
    private String callbackUrl;
    private Protocol protocol;
    private String gameName;
    private String password;
    private PlayerScores scores;
    private Information info;
    private GameType gameType;

    public Player() {
    }

    public Player(String name, String callbackUrl, GameType gameType, PlayerScores scores, Information info, Protocol protocol) {
        this.name = name;
        this.callbackUrl = callbackUrl;
        this.gameType = gameType;
        this.scores = scores;
        this.info = info;
        this.protocol = protocol;
    }

    public GameType getGameType() {
        return gameType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == NullPlayer.INSTANCE && (o != NullPlayer.INSTANCE && o != NullPlayerGame.INSTANCE)) return false;

        if (o instanceof Player) {
            Player p = (Player)o;

            return (p.name.equals(name));
        }

        if (o instanceof PlayerGame) {
            PlayerGame pg = (PlayerGame)o;

            return pg.getPlayer().equals(this);
        }

        return false;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public int hashCode() {
        return (name + code).hashCode();
    }

    public String getName() {
        return name;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int clearScore() {
        return scores.clear();
    }

    public int getScore() {
        return scores.getScore();
    }

    public String getMessage() {
        return info.getMessage();
    }

    public int getCurrentLevel() {
        return 0;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getCode() {
        return code;
    }

    public String getGameName() {
        return (gameType != null)?gameType.name():gameName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
