package com.codenjoy.dojo.services;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: sanja
 * Date: 11.01.14
 * Time: 2:58
 */
public class BoardLog {

    private String playerName;
    private String board;
    private String gameType;
    private int score;

    public BoardLog(ResultSet resultSet) {
        try {
            playerName = resultSet.getString(1);
            gameType = resultSet.getString(2);
            score = resultSet.getInt(3);
            board = resultSet.getString(4);
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
}
