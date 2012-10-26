package net.tetris.online.service;

import net.tetris.dom.Levels;

/**
 * User: serhiy.zelenin
 * Date: 10/26/12
 * Time: 2:46 PM
 */
public class Score implements Comparable<Score>{
    private int score;
    private String levelsClass;
    private String timestamp;
    private String playerName;

    public Score(String playerName, int score, String levelsClass, String timestamp) {
        this.playerName = playerName;
        this.score = score;
        this.levelsClass = levelsClass;
        this.timestamp = timestamp;
    }

    public int getScore() {
        return score;
    }

    public String getLevelsClass() {
        return levelsClass;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public int compareTo(Score score) {
        return score.getScore() - this.score;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
