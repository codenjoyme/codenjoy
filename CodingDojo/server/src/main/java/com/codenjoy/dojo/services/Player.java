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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import static com.codenjoy.dojo.services.GameServiceImpl.removeNumbers;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player implements ScreenRecipient, Closeable {

    public static final Player ANONYMOUS = new Player("anonymous");

    private String id;
    private String email;
    private String readableName;
    private String code;
    private String data;
    private String callbackUrl;
    private String gameName;
    private String roomName;
    private String password;
    private String passwordConfirmation;
    private PlayerScores scores;
    private Object score;
    private Information info;
    private GameType gameType;
    private InformationCollector eventListener;
    private Closeable ai;

    public Player(String id) {
        this.id = id;
    }

    public Player(String id, String callbackUrl, GameType gameType, PlayerScores scores, Information info) {
        this.id = id;
        this.callbackUrl = callbackUrl;
        this.gameType = gameType;
        this.scores = scores;
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == NullPlayer.INSTANCE && (o != NullPlayer.INSTANCE && o != NullPlayerGame.INSTANCE)) return false;

        if (o instanceof Player) {
            Player p = (Player)o;

            if (p.id == null) {
                return id == null;
            }

            return (p.id.equals(id));
        }

        if (o instanceof PlayerGame) {
            PlayerGame pg = (PlayerGame)o;

            return pg.getPlayer().equals(this);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (id + code).hashCode();
    }
    
    public String getNotNullReadableName() {
        return StringUtils.isEmpty(readableName) ? id : readableName;
    }

    public int clearScore() {
        return scores.clear();
    }

    public Object getScore() {
        return (scores != null) ? scores.getScore() : score;
    }

    public void setScore(Object score) {
        this.score = score;
        if (scores != null) {
            scores.update(score);
        }
    }

    public String getMessage() {
        return info.getMessage();
    }

    public String getGameName() {
        return (gameType != null) ? gameType.name() : gameName;
    }

    // TODO test me
    public String getGameNameOnly() {
        return removeNumbers(getGameName());
    }

    @Override
    public void close() {
        if (ai != null) {
            ai.close();
        }
    }

    public boolean hasAi() {
        return ai != null;
    }

    @Override
    public String toString() {
        return id;
    }

}
