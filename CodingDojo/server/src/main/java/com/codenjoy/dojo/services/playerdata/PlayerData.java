package com.codenjoy.dojo.services.playerdata;

import com.codenjoy.dojo.transport.screen.ScreenData;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:55 AM
 */
public class PlayerData implements ScreenData {

    public PlayerData(int boardSize, String board, String gameName, int score,
                      int maxLength, int length, int level,
                      String info, String chatLog, String scores, String coordinates) {
        this.board = board;
        this.gameName = gameName;
        this.score = score;
        this.maxLength = maxLength;
        this.length = length;
        this.level = level;
        this.boardSize = boardSize;
        this.info = info;
        this.chatLog = chatLog;
        this.scores = scores;
        this.coordinates = coordinates;
    }

    private String board;
    private String gameName;
    private int score;
    private int maxLength;
    private int length;
    private int level;
    private int boardSize;
    private String info;
    private String chatLog;
    private String scores;
    private String coordinates;

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }


    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getScores() {
        return scores;
    }

    public String getChatLog() {
        return chatLog;
    }

    public String getBoard() {
        return board;
    }

    public int getScore() {
        return score;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return String.format(
                "PlayerData[BoardSize:%s, " +
                        "Board:'%s', " +
                        "GameName:'%s', " +
                        "Score:%s, " +
                        "MaxLength:%s, " +
                        "Length:%s, " +
                        "CurrentLevel:%s, " +
                        "Info:'%s', " +
                        "ChatLog:'%s', " +
                        "Scores:'%s', " +
                        "Coordinates:'%s']",
                boardSize,
                board,
                gameName,
                score,
                maxLength,
                length,
                level,
                getInfo(),
                chatLog,
                scores,
                coordinates);
    }

    public String getInfo() {
        return (info == null) ? "" : info;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getLength() {
        return length;
    }
}
