package net.tetris.online.web.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.tetris.online.service.Score;

import java.util.List;

/**
 * User: serhiy.zelenin
 * Date: 11/7/12
 * Time: 11:27 PM
 */
public class ScoresData {
    @JsonProperty("aaData")
    private List<Score> scores;

    public ScoresData(List<Score> scores) {
        this.scores = scores;
    }

    public List<Score> getScores() {
        return scores;
    }
}
