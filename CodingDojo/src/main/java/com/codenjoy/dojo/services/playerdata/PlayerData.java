package com.codenjoy.dojo.services.playerdata;

import com.codenjoy.dojo.services.Plot;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:55 AM
 */
public class PlayerData {

    public PlayerData(int boardSize, List<Plot> plots, int score,
                      int maxLength, int length, int level, String info)
    {
        this.plots = plots;
        this.score = score;
        this.maxLength = maxLength;
        this.length = length;
        this.level = level;
        this.boardSize = boardSize;
        this.info = info;
    }

    private List<Plot> plots;
    private int score;
    private int maxLength;
    private int length;
    private int level;
    private int boardSize;
    private String info;

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
                        "MaxLength:%s, " +
                        "Length:%s, " +
                        "CurrentLevel:%s, " +
                        "Info:'%s']",
                boardSize,
                plots.toString(),
                score,
                maxLength,
                length,
                level,
                getInfo());
    }

    public String getInfo() {
        return (info == null)?"":info;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getLength() {
        return length;
    }
}
