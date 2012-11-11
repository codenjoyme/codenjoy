package net.tetris.services;

import com.fasterxml.jackson.annotation.JsonValue;
import net.tetris.dom.GameLevel;
import net.tetris.dom.Levels;

public class Player {
    private String name;
    private String callbackUrl;
    private PlayerScores scores;
    private Levels levels;

    public Player() {
    }

    public Player(String name, String callbackUrl, PlayerScores scores, Levels levels) {
        this.name = name;
        this.callbackUrl = callbackUrl;
        this.scores = scores;
        this.levels = levels;
    }

    public String getName() {
        return name;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getScore() {
        return scores.getScore();
    }

    public int getCurrentLevel() {
        return levels.getCurrentLevelNumber();
    }

    public GameLevel getNextLevel() {
        return levels.getNextLevel();
    }

    public int getTotalRemovedLines() {
        return levels.getTotalRemovedLines();
    }
}
