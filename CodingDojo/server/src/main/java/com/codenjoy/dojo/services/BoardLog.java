package com.codenjoy.dojo.services;

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
