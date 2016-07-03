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


import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardLog {

    private long time;
    private String playerName;
    private String board;
    private String gameType;
    private int score;

    public BoardLog(long time, String playerName, String gameType, int score, String board) {
        this.time = time;
        this.playerName = playerName;
        this.gameType = gameType;
        this.score = score;
        this.board = board;
    }

    public BoardLog(ResultSet resultSet) {
        try {
            time = resultSet.getTime(1).getTime();
            playerName = resultSet.getString(2);
            gameType = resultSet.getString(3);
            score = resultSet.getInt(4);
            board = resultSet.getString(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "BoardLog{" +
                "playerName='" + playerName + '\'' +
                ", board='" + board + '\'' +
                ", gameType='" + gameType + '\'' +
                ", score=" + score +
                '}';
    }

    public int getScore() {
        return score;
    }

    public String getGameType() {
        return gameType;
    }

    public long getTime() {
        return time;
    }
}
