package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.Information;
import com.codenjoy.dojo.snake.services.PlayerScores;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:49 AM
 */
public class Player {
    private String name;
    private String callbackUrl;
    private PlayerScores scores;
    private Information info;

    public Player() {
    }

    public Player(String name, String callbackUrl, PlayerScores scores, Information info) {
        this.name = name;
        this.callbackUrl = callbackUrl;
        this.scores = scores;
        this.info = info;
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

    public String getMessage() {
        return info.getMessage();
    }

    public int getCurrentLevel() {
        return 0;
    }
}
