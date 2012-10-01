package com.globallogic.snake.services.playerdata;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:55 AM
 */
public class PlayerData {
    public PlayerData(int boardSize, List<Plot> plots, int score, int level) {
        this.plots = plots;
        this.score = score;
        this.level = level;
        this.boardSize = boardSize;
    }

    private List<Plot> plots;
    private int score;
    private int level;
    private int boardSize;

    public List<Plot> getPlots() {
        return plots;
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
                        "Plots:%s, " +
                        "Score:%s, " +
                        "CurrentLevel:%s]",
                boardSize,
                plots.toString(),
                score,
                level);
    }
}
