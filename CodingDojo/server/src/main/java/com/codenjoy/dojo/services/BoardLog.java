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


import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardLog {

    private long time;
    private String playerName;
    private String board;
    private String command;
    private String gameType;
    private Object score;

    public BoardLog(long time, String playerName, String gameType, Object score, String board, String command) {
        this.time = time;
        this.playerName = playerName;
        this.gameType = gameType;
        this.score = score;
        this.board = board;
        this.command = command;
    }

    public BoardLog(ResultSet resultSet) {
        try {
            time = JDBCTimeUtils.getTimeLong(resultSet);
            playerName = resultSet.getString("player_name");
            gameType = resultSet.getString("game_type");
            score = resultSet.getInt("score");
            board = resultSet.getString("board");
            command = resultSet.getString("command");
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

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "BoardLog{" +
                "playerName='" + playerName + '\'' +
                ", time='" + time + '\'' +
                ", board='" + board + '\'' +
                ", command='" + command + '\'' +
                ", gameType='" + gameType + '\'' +
                ", score=" + score +
                '}';
    }

    public Object getScore() {
        return score;
    }

    public String getGameType() {
        return gameType;
    }

    public long getTime() {
        return time;
    }

    public void setBoard(String board) {
        this.board = board;
    }
}
