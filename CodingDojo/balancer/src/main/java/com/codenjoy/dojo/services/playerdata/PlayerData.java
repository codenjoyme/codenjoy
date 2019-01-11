package com.codenjoy.dojo.services.playerdata;

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


import com.codenjoy.dojo.transport.screen.ScreenData;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;

public class PlayerData implements ScreenData {

    private Object board;
    private String gameName;
    private Object score;
    private int boardSize;
    private String info;
    private JSONObject scores;
    private JSONObject heroesData;

    public PlayerData(int boardSize, Object board, String gameName, Object score,
                      String info, JSONObject scores, JSONObject heroesData) {
        this.board = board;
        this.gameName = gameName;
        this.score = score;
        this.boardSize = boardSize;
        this.info = info;
        this.scores = scores;
        this.heroesData = heroesData;
    }

    public JSONObject getHeroesData() {
        return heroesData;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public JSONObject getScores() {
        return scores;
    }

    public Object getBoard() {
        return board;
    }

    public Object getScore() {
        return score;
    }

    public int getBoardSize() {
        return boardSize;
    }

    @Override
    public String toString() {
        return String.format(
                "PlayerData[BoardSize:%s, " +
                        "Board:'%s', " +
                        "GameName:'%s', " +
                        "Score:%s, " +
                        "Info:'%s', " +
                        "Scores:'%s', " +
                        "HeroesData:'%s']",
                boardSize,
                board,
                gameName,
                score,
                getInfo(),
                JsonUtils.toStringSorted(scores.toString()),
                JsonUtils.toStringSorted(heroesData.toString()));
    }

    public String getInfo() {
        return (info == null) ? "" : info;
    }

}
