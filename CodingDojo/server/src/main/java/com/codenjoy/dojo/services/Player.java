package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.client.Closeable;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.nullobj.NullPlayerGame;
import com.codenjoy.dojo.transport.screen.ScreenRecipient;
import org.apache.commons.lang.StringUtils;

public class Player implements ScreenRecipient, Closeable {

    public static final Player ANONYMOUS = new Player("anonymous");

    private String name;
    private String readableName;
    private String code;
    private String data;
    private String callbackUrl;
    private String gameName;
    private String password;
    private PlayerScores scores;
    private Information info;
    private GameType gameType;
    private InformationCollector eventListener;
    private Closeable ai;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, String callbackUrl, GameType gameType, PlayerScores scores, Information info) {
        this.name = name;
        this.callbackUrl = callbackUrl;
        this.gameType = gameType;
        this.scores = scores;
        this.info = info;
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

            if (p.name == null) {
                return name == null;
            }

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

    public Object getScore() {
        return scores.getScore();
    }

    public String getMessage() {
        return info.getMessage();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getGameName() {
        return (gameType != null) ? gameType.name() : gameName;
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

    public void setEventListener(InformationCollector eventListener) {
        this.eventListener = eventListener;
    }

    public InformationCollector getEventListener() {
        return eventListener;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setAI(Closeable ai) {
        this.ai = ai;
    }

    @Override
    public void close() {
        if (ai != null) {
            ai.close();
        }
    }

    public boolean hasAI() {
        return ai != null;
    }

    public Closeable getAI() {
        return ai;
    }

    public String getReadableName() {
        return readableName;
    }

    public String getNotNullReadableName() {
        return StringUtils.isEmpty(readableName) ? name : readableName;
    }

    public void setReadableName(String readableName) {
        this.readableName = readableName;
    }
}
